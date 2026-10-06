package br.pucminas.sige.tickets.application;
import br.pucminas.sige.tickets.domain.*;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.shared.domain.*;
import java.time.*;
import java.util.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
public final class TicketQuery {
  private TicketQuery(){}
  public static Specification<Ticket> visible(AppUser user){return (root,q,cb)->switch(user.getRole()){case CLIENT->cb.equal(root.get("client").get("id"),user.getClient().getId());case TRAFFIC_MANAGER->cb.equal(root.get("assignee").get("id"),user.getId());case SERVICE,ADMIN->cb.conjunction();};}
  public static Specification<Ticket> overdue(Instant now){return (root,q,cb)->cb.and(cb.notEqual(root.get("status"),TicketStatus.CANCELLED),cb.or(cb.and(cb.isNotNull(root.get("responseDueAt")),cb.greaterThan(cb.coalesce(root.<Instant>get("responseCompletedAt"),cb.literal(now)),root.get("responseDueAt"))),cb.and(cb.isNotNull(root.get("resolutionDueAt")),cb.or(cb.and(cb.isNotNull(root.get("completedAt")),cb.greaterThan(root.<Instant>get("completedAt"),root.get("resolutionDueAt"))),cb.and(cb.isNull(root.get("completedAt")),cb.isNull(root.get("resolutionPausedAt")),cb.lessThan(root.<Instant>get("resolutionDueAt"),now))))));}
  public static Specification<Ticket> filters(AppUser user,String search,TicketStatus status,UUID clientId,UUID campaignId,UUID typeId,Priority priority,UUID assigneeId,LocalDate from,LocalDate to,Boolean onlyOverdue){
    if(from!=null&&to!=null&&from.isAfter(to))throw new IllegalArgumentException("Período inválido");
    Specification<Ticket> spec=visible(user).and((root,q,cb)->{List<Predicate> p=new ArrayList<>();
      if(search!=null&&!search.isBlank()){String value="%"+search.toLowerCase(Locale.ROOT).replace("\\","\\\\").replace("%","\\%").replace("_","\\_")+"%";p.add(cb.or(cb.like(cb.lower(root.get("number")),value,'\\'),cb.like(cb.lower(root.get("subject")),value,'\\'),cb.like(cb.lower(root.get("client").get("name")),value,'\\')));}
      if(status!=null)p.add(cb.equal(root.get("status"),status));if(priority!=null)p.add(cb.equal(root.get("priority"),priority));
      if(clientId!=null)p.add(cb.equal(root.get("client").get("id"),clientId));if(campaignId!=null)p.add(cb.equal(root.get("campaign").get("id"),campaignId));if(typeId!=null)p.add(cb.equal(root.get("demandType").get("id"),typeId));if(assigneeId!=null)p.add(cb.equal(root.get("assignee").get("id"),assigneeId));
      ZoneId zone=ZoneId.of("America/Sao_Paulo");if(from!=null)p.add(cb.greaterThanOrEqualTo(root.get("createdAt"),from.atStartOfDay(zone).toInstant()));if(to!=null)p.add(cb.lessThan(root.get("createdAt"),to.plusDays(1).atStartOfDay(zone).toInstant()));
      return cb.and(p.toArray(Predicate[]::new));});
    return Boolean.TRUE.equals(onlyOverdue)?spec.and(overdue(Instant.now())):spec;
  }
}
