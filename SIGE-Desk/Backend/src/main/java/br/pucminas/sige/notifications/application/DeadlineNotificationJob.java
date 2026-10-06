package br.pucminas.sige.notifications.application;
import br.pucminas.sige.tickets.domain.TicketRepository;
import java.time.Instant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
@Component
public class DeadlineNotificationJob {
  private final TicketRepository tickets;private final DeadlineNotificationService service;
  public DeadlineNotificationJob(TicketRepository tickets,DeadlineNotificationService service){this.tickets=tickets;this.service=service;}
  @Scheduled(fixedDelayString="${sige.notifications.overdue-interval-ms:60000}",initialDelayString="${sige.notifications.overdue-initial-delay-ms:5000}")
  public void run(){Instant now=Instant.now();for(int batch=0;batch<100;batch++){var ids=tickets.findPendingDeadlineNotices(now,PageRequest.of(0,100));if(ids.isEmpty())return;for(var id:ids){try{service.inspect(id,now);}catch(RuntimeException ex){org.slf4j.LoggerFactory.getLogger(getClass()).error("Não foi possível publicar vencimento do ticket {}",id,ex);}}if(ids.size()<100)return;}}
}
