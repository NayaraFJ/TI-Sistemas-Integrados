package br.pucminas.sige.notifications.domain;
import br.pucminas.sige.tickets.domain.Ticket;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="ticket_deadline_notices")
public class DeadlineNotice {
  @Id private UUID id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id",nullable=false) private Ticket ticket;
  @Column(nullable=false) private String kind;
  @Column(nullable=false) private int cycle;
  @Column(nullable=false) private Instant deadline;
  @Column(name="created_at",nullable=false) private Instant createdAt;
  protected DeadlineNotice(){}
  public DeadlineNotice(Ticket ticket,String kind,int cycle,Instant deadline){id=UUID.randomUUID();this.ticket=ticket;this.kind=kind;this.cycle=cycle;this.deadline=deadline;createdAt=Instant.now();}
}
