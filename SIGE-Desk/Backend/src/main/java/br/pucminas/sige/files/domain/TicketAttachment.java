package br.pucminas.sige.files.domain;

import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="ticket_attachments")
public class TicketAttachment {
  public enum Kind { ATTACHMENT, EVIDENCE }
  @Id private UUID id;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="ticket_id",nullable=false) private Ticket ticket;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author_id",nullable=false) private AppUser author;
  @Column(name="original_name",nullable=false) private String originalName;
  @Column(name="storage_key",nullable=false) private String storageKey;
  @Column(name="content_type",nullable=false) private String contentType;
  @Column(name="size_bytes",nullable=false) private long sizeBytes;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Kind kind;
  @Column(name="evidence_description") private String evidenceDescription;
  @Column(name="evidence_url") private String evidenceUrl;
  @Column(name="created_at",nullable=false) private Instant createdAt;
  protected TicketAttachment(){}
  public TicketAttachment(Ticket ticket,AppUser author,String originalName,String storageKey,String contentType,long sizeBytes,Kind kind,String evidenceDescription,String evidenceUrl){id=UUID.randomUUID();this.ticket=ticket;this.author=author;this.originalName=originalName;this.storageKey=storageKey;this.contentType=contentType;this.sizeBytes=sizeBytes;this.kind=kind;this.evidenceDescription=evidenceDescription;this.evidenceUrl=evidenceUrl;createdAt=Instant.now();}
  public UUID getId(){return id;} public Ticket getTicket(){return ticket;} public AppUser getAuthor(){return author;} public String getOriginalName(){return originalName;} public String getStorageKey(){return storageKey;} public String getContentType(){return contentType;} public long getSizeBytes(){return sizeBytes;} public Kind getKind(){return kind;} public String getEvidenceDescription(){return evidenceDescription;} public String getEvidenceUrl(){return evidenceUrl;} public Instant getCreatedAt(){return createdAt;}
}
