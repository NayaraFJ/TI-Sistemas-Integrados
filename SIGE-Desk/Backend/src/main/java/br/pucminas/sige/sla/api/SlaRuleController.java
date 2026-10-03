package br.pucminas.sige.sla.api;

import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.sla.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalTime;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/sla-rules") @Transactional
public class SlaRuleController {
  private final SlaRuleRepository rules; private final ClientRepository clients; private final DemandTypeRepository types; private final CurrentUser current;
  public SlaRuleController(SlaRuleRepository rules,ClientRepository clients,DemandTypeRepository types,CurrentUser current){this.rules=rules;this.clients=clients;this.types=types;this.current=current;}
  record Request(@NotBlank String name,@NotNull SlaRule.Scope scope,String clientId,String demandTypeId,@NotBlank String timezone,@NotBlank String businessDays,@NotNull LocalTime businessStart,@NotNull LocalTime businessEnd,@NotBlank String holidays,boolean pauseInValidation,@NotBlank String deadlines,boolean active){} record Item(String id,String name,String scope,String clientId,String demandTypeId,String timezone,String businessDays,LocalTime businessStart,LocalTime businessEnd,String holidays,boolean pauseInValidation,String deadlines,boolean active,int versionNumber){}
  @GetMapping public List<Item> list(){current.requireRole(Role.ADMIN);return rules.findAll().stream().map(this::item).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);validate(request);var rule=new SlaRule(request.name(),request.scope(),client(request.clientId()),type(request.demandTypeId()),request.deadlines());rule.update(request.name(),request.scope(),client(request.clientId()),type(request.demandTypeId()),request.timezone(),request.businessDays(),request.businessStart(),request.businessEnd(),request.holidays(),request.pauseInValidation(),request.deadlines(),request.active());return item(rules.save(rule));}
  @PutMapping("/{id}") public Item update(@PathVariable UUID id,@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);validate(request);var rule=rules.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Regra de SLA não encontrada"));if(rule.getScope()==SlaRule.Scope.DEFAULT&&!request.active()&&rules.findByActiveTrue().stream().filter(candidate->candidate.getScope()==SlaRule.Scope.DEFAULT).count()==1)throw new IllegalStateException("A última regra padrão não pode ser desativada");rule.update(request.name(),request.scope(),client(request.clientId()),type(request.demandTypeId()),request.timezone(),request.businessDays(),request.businessStart(),request.businessEnd(),request.holidays(),request.pauseInValidation(),request.deadlines(),request.active());return item(rule);}
  private Client client(String id){return id==null||id.isBlank()?null:clients.findById(UUID.fromString(id)).orElseThrow(()->new IllegalArgumentException("Cliente inválido"));} private DemandType type(String id){return id==null||id.isBlank()?null:types.findById(UUID.fromString(id)).orElseThrow(()->new IllegalArgumentException("Tipo de demanda inválido"));} private void validate(Request request){try{var mapper=new com.fasterxml.jackson.databind.ObjectMapper();mapper.readTree(request.businessDays());mapper.readTree(request.holidays());var deadlines=mapper.readTree(request.deadlines());for(var priority:br.pucminas.sige.shared.domain.Priority.values()){if(deadlines.path(priority.name()).path("responseHours").asInt()<=0||deadlines.path(priority.name()).path("resolutionHours").asInt()<=0)throw new IllegalArgumentException("Todos os prazos de SLA devem ser positivos");}}catch(IllegalArgumentException exception){throw exception;}catch(Exception exception){throw new IllegalArgumentException("Configuração JSON de SLA inválida");}switch(request.scope()){case DEFAULT->{if(request.clientId()!=null||request.demandTypeId()!=null)throw new IllegalArgumentException("Regra padrão não recebe cliente ou tipo");}case CLIENT->{if(request.clientId()==null||request.demandTypeId()!=null)throw new IllegalArgumentException("Regra por cliente inválida");}case DEMAND_TYPE->{if(request.clientId()!=null||request.demandTypeId()==null)throw new IllegalArgumentException("Regra por tipo inválida");}case CLIENT_AND_DEMAND_TYPE->{if(request.clientId()==null||request.demandTypeId()==null)throw new IllegalArgumentException("Regra por cliente e tipo inválida");}}}
  private Item item(SlaRule rule){return new Item(rule.getId().toString(),rule.getName(),rule.getScope().name(),rule.getClient()==null?null:rule.getClient().getId().toString(),rule.getDemandType()==null?null:rule.getDemandType().getId().toString(),rule.getTimezone(),rule.getBusinessDays(),rule.getBusinessStart(),rule.getBusinessEnd(),rule.getHolidays(),rule.isPauseInValidation(),rule.getDeadlines(),rule.isActive(),rule.getVersionNumber());}
}
