package br.pucminas.sige.demandtypes.application;

import com.fasterxml.jackson.databind.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public final class DynamicFieldValidator {
  private static final ObjectMapper JSON=new ObjectMapper();
  private static final Set<String> TYPES=Set.of("TEXT","NUMBER","DATE","SELECT","FILE");
  private DynamicFieldValidator(){}
  public static JsonNode definitions(String source){
    try {
      JsonNode fields=JSON.readTree(source);
      if(!fields.isArray()||fields.size()>50)throw invalid("Definição de campos inválida");
      Set<String> names=new HashSet<>();
      for(JsonNode field:fields){
        String name=field.path("name").asText(),type=field.path("type").asText("TEXT");
        if(!name.matches("[A-Za-z][A-Za-z0-9_]{0,99}")||!names.add(name)||!TYPES.contains(type)||field.path("label").asText().length()>160||(!field.path("required").isMissingNode()&&!field.path("required").isBoolean()))throw invalid("Definição de campos inválida: "+name);
        JsonNode options=field.path("options");
        if(type.equals("SELECT")&&(!options.isArray()||options.isEmpty()||options.size()>100))throw invalid("Informe opções de seleção: "+name);
        Set<String> unique=new HashSet<>();for(JsonNode option:options)if(!option.isTextual()||option.asText().isBlank()||option.asText().length()>1000||!unique.add(option.asText()))throw invalid("Opção inválida: "+name);
        JsonNode validation=field.path("validation");
        if(!validation.isMissingNode()&&!validation.isObject())throw invalid("Validação inválida: "+name);
        for(String key:List.of("min","max"))if(validation.has(key)&&(!type.equals("NUMBER")||!validation.get(key).isNumber()))throw invalid("Limite numérico inválido: "+name);
        if(validation.has("min")&&validation.has("max")&&validation.get("min").decimalValue().compareTo(validation.get("max").decimalValue())>0)throw invalid("Limites invertidos: "+name);
        for(String key:List.of("minLength","maxLength"))if(validation.has(key)&&(!validation.get(key).isIntegralNumber()||!validation.get(key).canConvertToInt()||validation.get(key).asInt()<0||validation.get(key).asInt()>1000))throw invalid("Comprimento inválido: "+name);
        if(validation.has("minLength")&&validation.has("maxLength")&&validation.get("minLength").asInt()>validation.get("maxLength").asInt())throw invalid("Comprimentos invertidos: "+name);
        for(String key:List.of("minDate","maxDate"))if(validation.has(key)){if(!type.equals("DATE"))throw invalid("Limite de data inválido: "+name);LocalDate.parse(validation.get(key).asText());}
        if(validation.has("minDate")&&validation.has("maxDate")&&LocalDate.parse(validation.get("minDate").asText()).isAfter(LocalDate.parse(validation.get("maxDate").asText())))throw invalid("Datas invertidas: "+name);
        for(Iterator<String> keys=validation.fieldNames();keys.hasNext();)if(!Set.of("min","max","minLength","maxLength","minDate","maxDate").contains(keys.next()))throw invalid("Regra de validação desconhecida: "+name);
      }
      for(JsonNode field:fields)if(field.has("requiredWhen")){JsonNode condition=field.get("requiredWhen");String other=condition.path("field").asText();if(!condition.isObject()||!names.contains(other)||other.equals(field.path("name").asText())||!condition.path("equals").isTextual())throw invalid("Condição inválida: "+field.path("name").asText());}
      return fields;
    }catch(IllegalArgumentException ex){throw ex;}catch(Exception ex){throw invalid("Definição de campos inválida");}
  }
  public static void validate(String source,Map<String,String> values,Set<String> suppliedFiles){
    JsonNode fields=definitions(source);Set<String> allowed=new HashSet<>();
    for(JsonNode field:fields){
      String name=field.path("name").asText(),label=field.path("label").asText(name),type=field.path("type").asText("TEXT"),value=values.getOrDefault(name,"");if(value==null)value="";allowed.add(name);
      JsonNode condition=field.path("requiredWhen");boolean required=field.path("required").asBoolean()||(condition.isObject()&&condition.path("equals").asText().equals(values.get(condition.path("field").asText())));
      if(required&&value.isBlank())throw invalid("Preencha o campo obrigatório: "+label);
      if(type.equals("FILE")&&!value.isBlank()&&!suppliedFiles.contains(name))throw invalid("Envie o arquivo do campo: "+label);
      if(suppliedFiles.contains(name)&&!type.equals("FILE"))throw invalid("Campo não aceita arquivo: "+label);
      if(value.isBlank())continue;
      if(value.length()>1000)throw invalid("Campo muito longo: "+label);
      JsonNode validation=field.path("validation");
      if(validation.has("minLength")&&value.length()<validation.get("minLength").asInt()||validation.has("maxLength")&&value.length()>validation.get("maxLength").asInt())throw invalid("Comprimento inválido: "+label);
      try {
        if(type.equals("NUMBER")){BigDecimal number=new BigDecimal(value);if(validation.has("min")&&number.compareTo(validation.get("min").decimalValue())<0||validation.has("max")&&number.compareTo(validation.get("max").decimalValue())>0)throw invalid("Número fora dos limites: "+label);}
        if(type.equals("DATE")){LocalDate date=LocalDate.parse(value);if(validation.has("minDate")&&date.isBefore(LocalDate.parse(validation.get("minDate").asText()))||validation.has("maxDate")&&date.isAfter(LocalDate.parse(validation.get("maxDate").asText())))throw invalid("Data fora dos limites: "+label);}
        if(type.equals("SELECT")){boolean found=false;for(JsonNode option:field.path("options"))if(option.asText().equals(value))found=true;if(!found)throw invalid("Opção não permitida: "+label);}
      }catch(NumberFormatException|java.time.DateTimeException ex){throw invalid("Valor inválido: "+label);}
    }
    if(!allowed.containsAll(values.keySet())||!allowed.containsAll(suppliedFiles))throw invalid("Campo adicional não permitido para este tipo");
  }
  private static IllegalArgumentException invalid(String message){return new IllegalArgumentException(message);}
}
