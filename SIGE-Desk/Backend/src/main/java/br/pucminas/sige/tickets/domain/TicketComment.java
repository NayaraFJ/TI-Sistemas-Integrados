package br.pucminas.sige.tickets.domain;

import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="ticket_comments")
public class TicketComment {
  @Id private UUID id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id", nullable=false) private Ticket ticket;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author_id", nullable=false) private AppUser author;
  @Column(nullable=false, columnDefinition="text") private String body;
  @Column(name="effective_response", nullable=false) private boolean effectiveResponse;
  @Column(name="created_at", nullable=false) private Instant createdAt;
  protected TicketComment() {}
  public TicketComment(Ticket ticket, AppUser author, String body, boolean effectiveResponse) { id=UUID.randomUUID(); this.ticket=ticket; this.author=author; this.body=body; this.effectiveResponse=effectiveResponse; createdAt=Instant.now(); }
  public UUID getId(){return id;} public AppUser getAuthor(){return author;} public String getBody(){return body;} public boolean isEffectiveResponse(){return effectiveResponse;} public Instant getCreatedAt(){return createdAt;}
}
