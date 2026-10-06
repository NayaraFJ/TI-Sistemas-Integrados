package br.pucminas.sige.sla.application;

import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.sla.domain.SlaRule;
import br.pucminas.sige.sla.domain.SlaRuleRepository;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class SlaService {
  public record Calculation(String snapshot, Instant responseDueAt, Instant resolutionDueAt) {}
  private record Calendar(ZoneId zone, JsonNode days, JsonNode holidays, LocalTime start, LocalTime end, boolean nationalHolidays) {}
  private final SlaRuleRepository rules;
  private final ObjectMapper json;
  public SlaService(SlaRuleRepository rules, ObjectMapper json) { this.rules=rules; this.json=json; }
  public Calculation calculate(Ticket ticket, Priority priority) {
    try {
      String snapshot=ticket.getSlaRuleSnapshot();
      if(snapshot==null) {
        SlaRule rule=rules.findByActiveTrue().stream().filter(item->matches(item,ticket)).max(Comparator.comparingInt(this::specificity)).orElseThrow(()->new IllegalStateException("Regra padrão de SLA não encontrada"));
        snapshot=snapshot(rule);
      }
      JsonNode config=json.readTree(snapshot);
      Calendar calendar=calendar(config);
      JsonNode deadline=config.path("deadlines").path(priority.name());
      int responseHours=deadline.path("responseHours").asInt(), resolutionHours=deadline.path("resolutionHours").asInt();
      if(responseHours<=0||resolutionHours<=0)throw new IllegalStateException("Prazos de SLA inválidos");
      Instant response=ticket.getResponseCompletedAt()!=null&&ticket.getResponseDueAt()!=null?ticket.getResponseDueAt():add(ticket.getCreatedAt(),Duration.ofHours(responseHours),calendar);
      Instant resolution=add(ticket.getResolutionCycleStartedAt(),Duration.ofHours(resolutionHours),calendar);
      for(JsonNode period:json.readTree(ticket.getResolutionPauseIntervals())) {
        Duration paused=businessDuration(Instant.parse(period.path("start").asText()),Instant.parse(period.path("end").asText()),calendar);
        resolution=add(resolution,paused,calendar);
      }
      return new Calculation(snapshot,response,resolution);
    }catch(java.io.IOException|DateTimeException ex){throw new IllegalStateException("Configuração de SLA inválida");}
  }
  public void resumeResolution(Ticket ticket) {
    if(ticket.getPriority()!=null&&ticket.getSlaRuleSnapshot()!=null)ticket.setResolutionDueAt(calculate(ticket,ticket.getPriority()).resolutionDueAt());
  }
  public boolean pauseInValidation(Ticket ticket) {
    try{return ticket.getSlaRuleSnapshot()!=null&&json.readTree(ticket.getSlaRuleSnapshot()).path("pauseInValidation").asBoolean();}catch(java.io.IOException ex){throw new IllegalStateException("Configuração de SLA inválida");}
  }
  public long elapsedBusinessMinutes(Ticket ticket, Instant now) {
    try {
      String source=ticket.getSlaRuleSnapshot();
      if(source==null)source=snapshot(rules.findByActiveTrue().stream().filter(rule->matches(rule,ticket)).max(Comparator.comparingInt(this::specificity)).orElseThrow(()->new IllegalStateException("Regra padrão de SLA não encontrada")));
      return businessDuration(ticket.getCreatedAt(),now,calendar(json.readTree(source))).toMinutes();
    }catch(java.io.IOException ex){throw new IllegalStateException("Calendário de SLA inválido");}
  }
  public static String responseState(Ticket ticket, Instant now) {
    if(ticket.getResponseDueAt()==null)return "PENDING_CLASSIFICATION";
    Instant end=ticket.getResponseCompletedAt();
    return (end==null?now:end).isAfter(ticket.getResponseDueAt())?"OVERDUE":end==null?"ON_TIME":"COMPLETED";
  }
  public static String resolutionState(Ticket ticket, Instant now) {
    if(ticket.getStatus()==TicketStatus.CANCELLED)return "CANCELLED";
    if(ticket.getResolutionDueAt()==null)return ticket.getResolutionCycle()>1?"PENDING_RECONFIRMATION":"PENDING_CLASSIFICATION";
    Instant end=ticket.getCompletedAt();
    if(end!=null)return end.isAfter(ticket.getResolutionDueAt())?"OVERDUE":"COMPLETED";
    if(ticket.getResolutionPausedAt()!=null)return "PAUSED";
    return now.isAfter(ticket.getResolutionDueAt())?"OVERDUE":"ON_TIME";
  }
  public static String state(Ticket ticket, Instant now) {
    if(ticket.getStatus()==TicketStatus.DONE)return "COMPLETED";
    String resolution=resolutionState(ticket,now);
    if("CANCELLED".equals(resolution)||resolution.startsWith("PENDING")||"PAUSED".equals(resolution))return resolution;
    return "OVERDUE".equals(resolution)||"OVERDUE".equals(responseState(ticket,now))?"OVERDUE":"ON_TIME";
  }
  private boolean matches(SlaRule rule,Ticket ticket) {
    return switch(rule.getScope()) {
      case DEFAULT->true;
      case CLIENT->rule.getClient()!=null&&rule.getClient().getId().equals(ticket.getClient().getId());
      case DEMAND_TYPE->rule.getDemandType()!=null&&rule.getDemandType().getId().equals(ticket.getDemandType().getId());
      case CLIENT_AND_DEMAND_TYPE->rule.getClient()!=null&&rule.getDemandType()!=null&&rule.getClient().getId().equals(ticket.getClient().getId())&&rule.getDemandType().getId().equals(ticket.getDemandType().getId());
    };
  }
  private int specificity(SlaRule rule){return switch(rule.getScope()){case DEFAULT->0;case DEMAND_TYPE->1;case CLIENT->2;case CLIENT_AND_DEMAND_TYPE->3;};}
  private String snapshot(SlaRule rule)throws java.io.IOException {
    var node=json.createObjectNode();node.put("id",rule.getId().toString());node.put("name",rule.getName());node.put("version",rule.getVersionNumber());node.put("timezone",rule.getTimezone());node.put("pauseInValidation",rule.isPauseInValidation());node.put("nationalHolidayPolicy","BR_FIXED_V1");node.set("deadlines",json.readTree(rule.getDeadlines()));node.set("businessDays",json.readTree(rule.getBusinessDays()));node.put("businessStart",rule.getBusinessStart().toString());node.put("businessEnd",rule.getBusinessEnd().toString());node.set("holidays",json.readTree(rule.getHolidays()));return json.writeValueAsString(node);
  }
  private Calendar calendar(JsonNode config) {
    JsonNode days=config.path("businessDays"),holidays=config.path("holidays");
    LocalTime start=LocalTime.parse(config.path("businessStart").asText()),end=LocalTime.parse(config.path("businessEnd").asText());
    if(!days.isArray()||days.isEmpty()||!holidays.isArray()||!start.isBefore(end))throw new IllegalStateException("Calendário de SLA inválido");
    for(JsonNode day:days)DayOfWeek.valueOf(day.asText());
    return new Calendar(ZoneId.of(config.path("timezone").asText()),days,holidays,start,end,"BR_FIXED_V1".equals(config.path("nationalHolidayPolicy").asText()));
  }
  private Instant add(Instant start,Duration remaining,Calendar calendar) {
    if(remaining.isZero())return start;
    ZonedDateTime cursor=start.atZone(calendar.zone());
    int traversed=0;
    while(!remaining.isZero()) {
      if(++traversed>40000)throw new IllegalStateException("Prazo excede o calendário suportado");
      LocalDate date=cursor.toLocalDate();ZonedDateTime from=date.atTime(calendar.start()).atZone(calendar.zone()),to=date.atTime(calendar.end()).atZone(calendar.zone());
      if(!working(date,calendar)||!cursor.isBefore(to)){cursor=date.plusDays(1).atStartOfDay(calendar.zone());continue;}
      if(cursor.isBefore(from))cursor=from;
      Duration available=Duration.between(cursor,to),used=remaining.compareTo(available)<0?remaining:available;
      cursor=cursor.plus(used);remaining=remaining.minus(used);
    }
    return cursor.toInstant();
  }
  private Duration businessDuration(Instant start,Instant end,Calendar calendar) {
    Duration result=Duration.ZERO;
    for(LocalDate date=start.atZone(calendar.zone()).toLocalDate();!date.isAfter(end.atZone(calendar.zone()).toLocalDate());date=date.plusDays(1)) {
      if(!working(date,calendar))continue;
      Instant from=date.atTime(calendar.start()).atZone(calendar.zone()).toInstant(),to=date.atTime(calendar.end()).atZone(calendar.zone()).toInstant();
      if(from.isBefore(start))from=start;if(to.isAfter(end))to=end;if(from.isBefore(to))result=result.plus(Duration.between(from,to));
    }
    return result;
  }
  private boolean working(LocalDate date,Calendar calendar){return contains(calendar.days(),date.getDayOfWeek().name())&&!contains(calendar.holidays(),date.toString())&&!(calendar.nationalHolidays()&&BrazilianHolidays.isHoliday(date));}
  private boolean contains(JsonNode values,String value){for(JsonNode node:values)if(value.equals(node.asText()))return true;return false;}
}
