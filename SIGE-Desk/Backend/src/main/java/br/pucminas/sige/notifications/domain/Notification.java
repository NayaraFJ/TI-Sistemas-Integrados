package br.pucminas.sige.notifications.domain;

import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="notifications")
public class Notification {
  @Id private UUID id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false) private AppUser user;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id", nullable=false) private Ticket ticket;
  @Column(name="event_type", nullable=false) private String eventType;
  @Column(nullable=false) private String summary;
  @Column(name="read_at") private Instant readAt;
  @Column(name="created_at", nullable=false) private Instant createdAt;
  protected Notification() {}
  public Notification(AppUser user, Ticket ticket, String eventType, String summary) { id=UUID.randomUUID(); this.user=user; this.ticket=ticket; this.eventType=eventType; this.summary=summary; createdAt=Instant.now(); }
  public UUID getId(){return id;} public Ticket getTicket(){return ticket;} public String getEventType(){return eventType;} public String getSummary(){return summary;} public Instant getReadAt(){return readAt;} public Instant getCreatedAt(){return createdAt;} public void markRead(){ if(readAt==null) readAt=Instant.now(); }
  public AppUser getUser(){return user;}
}
