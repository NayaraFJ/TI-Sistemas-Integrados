package br.pucminas.sige.dashboard.application;
import br.pucminas.sige.shared.security.QueryScope;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.dashboard.api.DashboardController.*;
import br.pucminas.sige.tickets.domain.TicketStatus;
import br.pucminas.sige.shared.domain.Priority;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.*;
import org.springframework.stereotype.Service;
@Service
public class DashboardMetrics {
  private final EntityManager em;
  public DashboardMetrics(EntityManager em){this.em=em;}
  private static final String ACTIVE="t.status not in ('DONE','CANCELLED')";
  private static final String OVERDUE="((t.responseDueAt is not null and coalesce(t.responseCompletedAt,:now)>t.responseDueAt) or (t.resolutionDueAt is not null and t.completedAt is null and t.resolutionPausedAt is null and t.resolutionDueAt<:now))";
  public Response read(AppUser user){
    Instant now=Instant.now();String scope=QueryScope.ticket(user,"t");
    var count=QueryScope.bind(em.createQuery("select sum(case when "+ACTIVE+" then 1 else 0 end),sum(case when "+ACTIVE+" and t.priority in ('HIGH','URGENT') then 1 else 0 end),sum(case when t.status='VALIDATION' then 1 else 0 end),sum(case when t.status='WAITING_FOR_CLIENT' then 1 else 0 end),sum(case when "+ACTIVE+" and t.resolutionDueAt is not null then 1 else 0 end),sum(case when "+ACTIVE+" and "+OVERDUE+" then 1 else 0 end) from Ticket t where "+scope,Object[].class),user).setParameter("now",now).getSingleResult();
    var statusRows=QueryScope.bind(em.createQuery("select t.status,count(t) from Ticket t where "+scope+" group by t.status",Object[].class),user).getResultList();
    List<StatusCount> statuses=Arrays.stream(TicketStatus.values()).map(status->new StatusCount(status.name(),statusRows.stream().filter(row->row[0]==status).mapToLong(row->n(row[1])).sum())).toList();
    var priorityRows=QueryScope.bind(em.createQuery("select t.priority,count(t) from Ticket t where "+scope+" group by t.priority",Object[].class),user).getResultList();
    List<PriorityCount> priorities=new ArrayList<>();for(Priority priority:Priority.values())priorities.add(new PriorityCount(priority.name(),priorityRows.stream().filter(row->row[0]==priority).mapToLong(row->n(row[1])).sum()));priorities.add(new PriorityCount("UNCLASSIFIED",priorityRows.stream().filter(row->row[0]==null).mapToLong(row->n(row[1])).sum()));
    var assigneeRows=QueryScope.bind(em.createQuery("select a.id,a.name,count(t) from Ticket t left join t.assignee a where "+scope+" group by a.id,a.name order by a.name",Object[].class),user).getResultList();
    List<AssigneeCount> assignees=assigneeRows.stream().map(row->new AssigneeCount(row[0]==null?null:row[0].toString(),row[1]==null?"Sem responsável":row[1].toString(),n(row[2]))).toList();
    var deadlines=QueryScope.bind(em.createQuery("select sum(case when "+OVERDUE+" then 1 else 0 end),sum(case when not "+OVERDUE+" and t.resolutionDueAt is not null and t.resolutionPausedAt is not null then 1 else 0 end),sum(case when not "+OVERDUE+" and t.resolutionDueAt is null then 1 else 0 end),sum(case when not "+OVERDUE+" and t.resolutionDueAt is not null and t.resolutionPausedAt is null then 1 else 0 end) from Ticket t where "+scope+" and "+ACTIVE,Object[].class),user).setParameter("now",now).getSingleResult();
    List<DeadlineCount> deadlineCounts=List.of(new DeadlineCount("OVERDUE",n(deadlines[0])),new DeadlineCount("PAUSED",n(deadlines[1])),new DeadlineCount("PENDING_CLASSIFICATION",n(deadlines[2])),new DeadlineCount("ON_TIME",n(deadlines[3])));
    var recent=QueryScope.bind(em.createQuery("select t.id,t.number,t.subject,t.status,t.client.name,t.updatedAt from Ticket t where "+scope+" order by t.updatedAt desc,t.id",Object[].class),user).setMaxResults(5).getResultList().stream().map(row->new Recent(row[0].toString(),row[1].toString(),row[2].toString(),row[3].toString(),row[4].toString(),row[5].toString())).toList();
    return new Response(n(count[0]),n(count[1]),n(count[2]),n(count[3]),n(count[4]),n(count[5]),statuses,recent,priorities,assignees,deadlineCounts);
  }
  private static long n(Object value){return value==null?0:((Number)value).longValue();}
}
