package br.pucminas.sige.dashboard.api;
import br.pucminas.sige.dashboard.application.DashboardMetrics;
import br.pucminas.sige.shared.security.CurrentUser;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
@RestController @RequestMapping("/api/v1/dashboard") @Transactional(readOnly=true)
public class DashboardController {
  private final DashboardMetrics metrics;private final CurrentUser current;
  public DashboardController(DashboardMetrics metrics,CurrentUser current){this.metrics=metrics;this.current=current;}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardStatusCount") public record StatusCount(@io.swagger.v3.oas.annotations.media.Schema(allowableValues={"OPEN","TRIAGE","EXECUTION","WAITING_FOR_CLIENT","VALIDATION","DONE","REOPENED","CANCELLED"}) String status,long count){}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardPriorityCount") public record PriorityCount(String priority,long count){}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardAssigneeCount") public record AssigneeCount(@io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String id,String name,long count){}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardDeadlineCount") public record DeadlineCount(String state,long count){}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardRecent") public record Recent(String id,String number,String subject,@io.swagger.v3.oas.annotations.media.Schema(allowableValues={"OPEN","TRIAGE","EXECUTION","WAITING_FOR_CLIENT","VALIDATION","DONE","REOPENED","CANCELLED"}) String status,String clientName,String updatedAt){}
  @io.swagger.v3.oas.annotations.media.Schema(name="DashboardResponse") public record Response(long activeCount,long highPriorityCount,long validationCount,long waitingCount,long classifiedCount,long overdueCount,List<StatusCount> statusDistribution,List<Recent> recent,List<PriorityCount> priorityDistribution,List<AssigneeCount> assigneeDistribution,List<DeadlineCount> deadlineDistribution){}
  @GetMapping public Response dashboard(){return metrics.read(current.require());}
}
