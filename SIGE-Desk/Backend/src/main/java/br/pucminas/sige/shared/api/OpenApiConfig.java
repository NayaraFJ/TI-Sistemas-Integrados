package br.pucminas.sige.shared.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  OpenAPI sigeDeskOpenApi() {
    return new OpenAPI()
      .info(new Info().title("SIGE Desk API").version("v1").description("API autenticada para gestão de demandas de tráfego.").license(new License().name("Uso acadêmico")))
      .addSecurityItem(new SecurityRequirement().addList("sessionCookie"))
      .schemaRequirement("sessionCookie", new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name("JSESSIONID"));
  }
  @Bean org.springdoc.core.customizers.OpenApiCustomizer commandContract() {
    return api -> {
      var error=new io.swagger.v3.oas.models.media.ObjectSchema()
        .addProperty("code",new io.swagger.v3.oas.models.media.StringSchema())
        .addProperty("message",new io.swagger.v3.oas.models.media.StringSchema())
        .addProperty("timestamp",new io.swagger.v3.oas.models.media.StringSchema().format("date-time"))
        .addProperty("fields",new io.swagger.v3.oas.models.media.MapSchema().additionalProperties(new io.swagger.v3.oas.models.media.StringSchema()));
      api.getComponents().addSchemas("ApiError",error);
      api.getPaths().addPathItem("/api/v1/auth/logout",new io.swagger.v3.oas.models.PathItem().post(new io.swagger.v3.oas.models.Operation().operationId("logout").summary("Encerrar a sessão").responses(new io.swagger.v3.oas.models.responses.ApiResponses().addApiResponse("204",new io.swagger.v3.oas.models.responses.ApiResponse().description("Sessão encerrada")))));
      api.getPaths().forEach((path,item)->item.readOperationsMap().forEach((method,operation)->{
        boolean mutation=method!=io.swagger.v3.oas.models.PathItem.HttpMethod.GET;
        boolean publicPath=path.equals("/api/v1/auth/login")||path.equals("/api/v1/auth/csrf");
        if(publicPath)operation.setSecurity(java.util.List.of());
        if(mutation&&!path.equals("/api/v1/auth/login"))operation.addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter().name("X-XSRF-TOKEN").in("header").required(true).description("Token obtido em GET /api/v1/auth/csrf").schema(new io.swagger.v3.oas.models.media.StringSchema()));
        if(mutation&&path.startsWith("/api/v1/tickets/{id}")&&!path.contains("attachments")){
          operation.addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter().name("If-Match").in("header").required(true).description("Versão do TicketItem lido ao abrir o formulário").schema(new io.swagger.v3.oas.models.media.StringSchema()));
          operation.getResponses().addApiResponse("428",new io.swagger.v3.oas.models.responses.ApiResponse().description("Versão esperada ausente"));
        }
        for(String status:java.util.List.of("400","403","404","409"))operation.getResponses().addApiResponse(status,new io.swagger.v3.oas.models.responses.ApiResponse().description(switch(status){case "400"->"Dados inválidos";case "403"->"Perfil ou CSRF não autorizado";case "404"->"Recurso fora do escopo ou inexistente";default->"Conflito de versão, transição ou cadastro";}).content(new io.swagger.v3.oas.models.media.Content().addMediaType("application/json",new io.swagger.v3.oas.models.media.MediaType().schema(new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/ApiError")))));
      }));
      var multipart=api.getPaths().get("/api/v1/tickets").getPost().getRequestBody().getContent().get("multipart/form-data");
      if(multipart!=null){var schema=new io.swagger.v3.oas.models.media.ObjectSchema().addProperty("request",new io.swagger.v3.oas.models.media.Schema<>().$ref("#/components/schemas/CreateTicketRequest")).addProperty("files",new io.swagger.v3.oas.models.media.ArraySchema().items(new io.swagger.v3.oas.models.media.StringSchema().format("binary")));schema.addRequiredItem("request");multipart.setSchema(schema);api.getPaths().get("/api/v1/tickets").getPost().setDescription("Criação JSON ou multipart atômica. Multipart: request com Content-Type application/json, files para anexos gerais e field.<name> para cada campo FILE. Limite de 10 MB por arquivo e 11 MB por envio. Valores dos campos FILE são preenchidos pelo servidor.");}
    };
  }
}
