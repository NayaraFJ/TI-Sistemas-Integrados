package br.pucminas.sige.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.DriverManager;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@Tag("mysql")
@EnabledIfEnvironmentVariable(named = "SIGE_TEST_DB_URL", matches = ".+")
class MySqlMigrationIntegrationTest {
  @Test
  void migratesAnEmptyMySqlDatabase() throws Exception {
    String url = System.getenv("SIGE_TEST_DB_URL");
    String username = required("SIGE_TEST_DB_USERNAME");
    String password = required("SIGE_TEST_DB_PASSWORD");

    Flyway.configure().dataSource(url, username, password).locations("classpath:db/migration").load().migrate();

    try (var connection = DriverManager.getConnection(url, username, password);
         var statement = connection.prepareStatement("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name IN ('clients','users','tickets','ticket_attachments','notifications','outbox_events','flyway_schema_history')");
         var result = statement.executeQuery()) {
      assertTrue(result.next());
      assertEquals(7, result.getInt(1));
    }
  }

  private String required(String variable) {
    String value = System.getenv(variable);
    if (value == null || value.isBlank()) throw new IllegalStateException("Defina " + variable + " para executar a integração MySQL");
    return value;
  }
}
