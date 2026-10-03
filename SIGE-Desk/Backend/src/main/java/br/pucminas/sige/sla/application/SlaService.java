package br.pucminas.sige.sla.application;

import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.sla.domain.SlaRule;
import br.pucminas.sige.sla.domain.SlaRuleRepository;
import br.pucminas.sige.tickets.domain.Ticket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class SlaService {
  public record Calculation(String snapshot, Instant responseDueAt, Instant resolutionDueAt) {}
  private final SlaRuleRepository rules;
  private final ObjectMapper json;
  public SlaService(SlaRuleRepository rules, ObjectMapper json) { this.rules=rules; this.json=json; }
  public Calculation calculate(Ticket ticket, Priority priority) {
    SlaRule rule=rules.findByActiveTrue().stream().filter(ruleItem -> matches(ruleItem,ticket)).max(Comparator.comparingInt(this::specificity)).orElseThrow(() -> new IllegalStateException("Regra padrão de SLA não encontrada"));
    try {
      JsonNode deadline=json.readTree(rule.getDeadlines()).path(priority.name());
      int responseHours=deadline.path("responseHours").asInt(); int resolutionHours=deadline.path("resolutionHours").asInt();
      if(responseHours <= 0 || resolutionHours <= 0) throw new IllegalStateException("Prazos de SLA inválidos");
      Instant start=ticket.getCreatedAt();
      return new Calculation(snapshot(rule), addBusinessHours(start,responseHours,rule), addBusinessHours(start,resolutionHours,rule));
    } catch (java.io.IOException exception) { throw new IllegalStateException("Configuração de SLA inválida"); }
  }
  private boolean matches(SlaRule rule, Ticket ticket) {
    return switch(rule.getScope()) {
      case DEFAULT -> true;
      case CLIENT -> rule.getClient()!=null && rule.getClient().getId().equals(ticket.getClient().getId());
      case DEMAND_TYPE -> rule.getDemandType()!=null && rule.getDemandType().getId().equals(ticket.getDemandType().getId());
      case CLIENT_AND_DEMAND_TYPE -> rule.getClient()!=null && rule.getDemandType()!=null && rule.getClient().getId().equals(ticket.getClient().getId()) && rule.getDemandType().getId().equals(ticket.getDemandType().getId());
    };
  }
  private int specificity(SlaRule rule) { return switch(rule.getScope()) { case DEFAULT -> 0; case CLIENT, DEMAND_TYPE -> 1; case CLIENT_AND_DEMAND_TYPE -> 2; }; }
  private String snapshot(SlaRule rule) { return "{\"id\":\""+rule.getId()+"\",\"name\":\""+rule.getName().replace("\"","\\\"")+"\",\"version\":"+rule.getVersionNumber()+",\"timezone\":\""+rule.getTimezone()+"\",\"pauseInValidation\":"+rule.isPauseInValidation()+",\"deadlines\":"+rule.getDeadlines()+"}"; }
  private Instant addBusinessHours(Instant start, int hours, SlaRule rule) throws java.io.IOException {
    ZoneId zone=ZoneId.of(rule.getTimezone()); JsonNode days=json.readTree(rule.getBusinessDays()); JsonNode holidays=json.readTree(rule.getHolidays());
    ZonedDateTime cursor=start.atZone(zone); Duration remaining=Duration.ofHours(hours);
    while(!remaining.isZero()) {
      LocalDate date=cursor.toLocalDate(); boolean day=contains(days,cursor.getDayOfWeek().name()) && !contains(holidays,date.toString());
      ZonedDateTime businessStart=ZonedDateTime.of(date,rule.getBusinessStart(),zone); ZonedDateTime businessEnd=ZonedDateTime.of(date,rule.getBusinessEnd(),zone);
      if(!day || !cursor.isBefore(businessEnd)) { cursor=ZonedDateTime.of(date.plusDays(1),LocalTime.MIDNIGHT,zone); continue; }
      if(cursor.isBefore(businessStart)) cursor=businessStart;
      Duration available=Duration.between(cursor,businessEnd);
      Duration used=remaining.compareTo(available)<0 ? remaining : available;
      cursor=cursor.plus(used); remaining=remaining.minus(used);
    }
    return cursor.toInstant();
  }
  private boolean contains(JsonNode values, String value) { for(JsonNode node: values) if(value.equals(node.asText())) return true; return false; }
}
