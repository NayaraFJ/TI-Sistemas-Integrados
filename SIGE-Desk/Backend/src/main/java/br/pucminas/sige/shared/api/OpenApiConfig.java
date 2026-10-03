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
}
