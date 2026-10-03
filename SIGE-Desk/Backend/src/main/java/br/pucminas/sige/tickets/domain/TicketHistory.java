package br.pucminas.sige.tickets.domain;

import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="ticket_history")
public class TicketHistory {
  @Id private UUID id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id", nullable=false) private Ticket ticket;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="actor_id") private AppUser actor;
  @Column(nullable=false) private String action;
  @Column(name="field_name") private String fieldName;
  @Column(name="old_value", columnDefinition="json") private String oldValue;
  @Column(name="new_value", columnDefinition="json") private String newValue;
  @Column(columnDefinition="text") private String reason;
  @Column(name="created_at", nullable=false) private Instant createdAt;
  protected TicketHistory() {}
  public TicketHistory(Ticket ticket, AppUser actor, String action, String fieldName, String oldValue, String newValue, String reason) { id=UUID.randomUUID(); this.ticket=ticket; this.actor=actor; this.action=action; this.fieldName=fieldName; this.oldValue=oldValue; this.newValue=newValue; this.reason=reason; createdAt=Instant.now(); }
  public UUID getId(){return id;} public AppUser getActor(){return actor;} public String getAction(){return action;} public String getFieldName(){return fieldName;} public String getOldValue(){return oldValue;} public String getNewValue(){return newValue;} public String getReason(){return reason;} public Instant getCreatedAt(){return createdAt;}
}
