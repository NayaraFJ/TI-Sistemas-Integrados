package br.pucminas.sige.tickets.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TicketNumberGenerator {
  private final JdbcTemplate jdbc;
  public TicketNumberGenerator(JdbcTemplate jdbc) { this.jdbc=jdbc; }
  @Transactional
  public String next() {
    Long next=jdbc.queryForObject("SELECT next_value FROM ticket_sequences WHERE name = 'SIGE' FOR UPDATE", Long.class);
    if(next==null) throw new IllegalStateException("Sequência de ticket indisponível");
    jdbc.update("UPDATE ticket_sequences SET next_value = ? WHERE name = 'SIGE'", next + 1);
    return "SIGE-" + next;
  }
}
