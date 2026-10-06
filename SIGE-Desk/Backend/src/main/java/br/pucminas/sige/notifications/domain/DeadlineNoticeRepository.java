package br.pucminas.sige.notifications.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.UUID;
public interface DeadlineNoticeRepository extends JpaRepository<DeadlineNotice,UUID>{boolean existsByTicketIdAndKindAndCycleAndDeadline(UUID ticketId,String kind,int cycle,Instant deadline);}
