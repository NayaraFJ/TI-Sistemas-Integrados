package br.pucminas.sige.tickets.domain;

import br.pucminas.sige.campaigns.domain.Campaign;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.AuditableEntity;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket extends AuditableEntity {
  @Id private UUID id;
  @Column(nullable=false, unique=true) private String number;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="client_id", nullable=false) private Client client;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="campaign_id") private Campaign campaign;
  @Column(name="pending_campaign") private String pendingCampaign;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="demand_type_id", nullable=false) private DemandType demandType;
  @Column(name="type_snapshot", columnDefinition="json", nullable=false) private String typeSnapshot;
  @Column(nullable=false) private String channel;
  @Column(nullable=false) private String subject;
  @Column(nullable=false, columnDefinition="text") private String description;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Priority urgency;
  @Enumerated(EnumType.STRING) private Priority priority;
  @Column(name="desired_date") private LocalDate desiredDate;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="requester_id", nullable=false) private AppUser requester;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author_id", nullable=false) private AppUser author;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="assignee_id") private AppUser assignee;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private TicketStatus status = TicketStatus.OPEN;
  @Enumerated(EnumType.STRING) @Column(name="wait_origin") private WaitOrigin waitOrigin;
  @Column(name="wait_reason", columnDefinition="text") private String waitReason;
  @Column(name="complement_received", nullable=false) private boolean complementReceived;
  @Column(name="execution_started", nullable=false) private boolean executionStarted;
  @Column(name="resolution_cycle", nullable=false) private int resolutionCycle = 1;
  @Column(name="resolution_cycle_started_at") private Instant resolutionCycleStartedAt;
  @Column(name="resolution_pause_intervals", columnDefinition="json", nullable=false) private String resolutionPauseIntervals = "[]";
  @Column(name="response_due_at") private Instant responseDueAt;
  @Column(name="resolution_due_at") private Instant resolutionDueAt;
  @Column(name="response_completed_at") private Instant responseCompletedAt;
  @Column(name="resolution_paused_at") private Instant resolutionPausedAt;
  @Column(name="completed_at") private Instant completedAt;
  @Column(name="sla_state", nullable=false) private String slaState = "PENDING_CLASSIFICATION";
  @Column(name="sla_rule_snapshot", columnDefinition="json") private String slaRuleSnapshot;
  @Column(columnDefinition="json") private String metrics;
  @Version @Column(name="version_number", nullable=false) private long versionNumber;

  protected Ticket() {}
  public Ticket(String number, Client client, Campaign campaign, String pendingCampaign, DemandType demandType,
      String channel, String subject, String description, Priority urgency, LocalDate desiredDate,
      AppUser requester, AppUser author, String metrics) {
    id=UUID.randomUUID(); this.number=number; this.client=client; this.campaign=campaign; this.pendingCampaign=pendingCampaign;
    this.demandType=demandType; typeSnapshot=typeSnapshot(demandType);
    this.channel=channel; this.subject=subject; this.description=description; this.urgency=urgency; this.desiredDate=desiredDate; this.requester=requester; this.author=author; this.metrics=metrics;
  }
  public UUID getId(){return id;} public String getNumber(){return number;} public Client getClient(){return client;} public Campaign getCampaign(){return campaign;}
  public String getPendingCampaign(){return pendingCampaign;} public DemandType getDemandType(){return demandType;} public String getChannel(){return channel;} public String getSubject(){return subject;}
  public String getDescription(){return description;} public Priority getUrgency(){return urgency;} public Priority getPriority(){return priority;} public LocalDate getDesiredDate(){return desiredDate;}
  public AppUser getRequester(){return requester;} public AppUser getAuthor(){return author;} public AppUser getAssignee(){return assignee;} public TicketStatus getStatus(){return status;}
  public WaitOrigin getWaitOrigin(){return waitOrigin;} public String getWaitReason(){return waitReason;} public boolean isComplementReceived(){return complementReceived;}
  public boolean isExecutionStarted(){return executionStarted;} public int getResolutionCycle(){return resolutionCycle;} public Instant getResponseDueAt(){return responseDueAt;}
  public Instant getResolutionDueAt(){return resolutionDueAt;} public Instant getResponseCompletedAt(){return responseCompletedAt;} public Instant getResolutionPausedAt(){return resolutionPausedAt;} public Instant getCompletedAt(){return completedAt;}
  public String getSlaState(){return slaState;} public String getSlaRuleSnapshot(){return slaRuleSnapshot;} public String getTypeSnapshot(){return typeSnapshot;} public String getMetrics(){return metrics;} public long getVersionNumber(){return versionNumber;}
  public Instant getResolutionCycleStartedAt(){return resolutionCycleStartedAt==null?getCreatedAt():resolutionCycleStartedAt;}
  public String getResolutionPauseIntervals(){return resolutionPauseIntervals;}
  public boolean isApprovalRequired(){return snapshotFlag("approvalRequired");}
  public boolean isEvidenceRequired(){return snapshotFlag("evidenceRequired");}
  private boolean snapshotFlag(String key){try{return new com.fasterxml.jackson.databind.ObjectMapper().readTree(typeSnapshot).path(key).asBoolean();}catch(java.io.IOException ex){throw new IllegalStateException("Configuração do tipo aplicada ao ticket inválida");}}
  public void finishResolutionPause(Instant now){if(resolutionPausedAt==null)return;try{var mapper=new com.fasterxml.jackson.databind.ObjectMapper();var periods=(com.fasterxml.jackson.databind.node.ArrayNode)mapper.readTree(resolutionPauseIntervals);var period=periods.addObject();period.put("start",resolutionPausedAt.toString());period.put("end",now.toString());resolutionPauseIntervals=mapper.writeValueAsString(periods);resolutionPausedAt=null;}catch(java.io.IOException ex){throw new IllegalStateException("Registro de pausas inválido");}}
  public void recordActivity(){updatedAt=Instant.now();}
  public void reassign(AppUser user){require(TicketStatus.TRIAGE,TicketStatus.EXECUTION,TicketStatus.WAITING_FOR_CLIENT,TicketStatus.VALIDATION,TicketStatus.REOPENED);if(user==null||!user.isActive()||user.getRole()!=br.pucminas.sige.shared.domain.Role.TRAFFIC_MANAGER)throw new IllegalArgumentException("Responsável inválido");assignee=user;recordActivity();}
  public void setResolutionDueAt(Instant dueAt){resolutionDueAt=dueAt;}
  public void pauseValidation(){resolutionPausedAt=Instant.now();slaState="PAUSED";}
  public void startTriage(){ require(TicketStatus.OPEN, TicketStatus.REOPENED); status=TicketStatus.TRIAGE; }
  public void classify(Priority priority, AppUser assignee, String slaSnapshot, Instant responseDue, Instant resolutionDue) {
    require(TicketStatus.TRIAGE, TicketStatus.REOPENED); this.priority=priority; this.assignee=assignee; this.slaRuleSnapshot=slaSnapshot; this.responseDueAt=responseDue; this.resolutionDueAt=resolutionDue; this.slaState="ON_TIME";
  }
  public void sendToExecution(){ require(TicketStatus.TRIAGE, TicketStatus.REOPENED); if (priority==null || assignee==null || slaRuleSnapshot==null) throw new IllegalStateException("Triagem incompleta"); status=TicketStatus.EXECUTION; executionStarted=true; }
  public void waitForClient(WaitOrigin origin, String reason) { require(TicketStatus.TRIAGE, TicketStatus.EXECUTION); waitOrigin=origin; waitReason=reason; complementReceived=false; resolutionPausedAt=Instant.now(); status=TicketStatus.WAITING_FOR_CLIENT; slaState="PAUSED"; }
  public void receiveComplement(){ require(TicketStatus.WAITING_FOR_CLIENT); complementReceived=true; }
  public void resume(){ require(TicketStatus.WAITING_FOR_CLIENT); if (!complementReceived) throw new IllegalStateException("Complemento ainda não foi recebido"); if(waitOrigin==WaitOrigin.EXECUTION&&(priority==null||assignee==null||slaRuleSnapshot==null))throw new IllegalStateException("Triagem incompleta"); finishResolutionPause(Instant.now()); status = waitOrigin == WaitOrigin.TRIAGE ? TicketStatus.TRIAGE : TicketStatus.EXECUTION; waitOrigin=null; waitReason=null; complementReceived=false; resolutionPausedAt=null; slaState="ON_TIME"; }
  public void recordExecution(boolean approvalRequired) { require(TicketStatus.EXECUTION, TicketStatus.REOPENED); if(status==TicketStatus.REOPENED&&slaRuleSnapshot==null)throw new IllegalStateException("Reabertura exige confirmação da triagem"); executionStarted=true; status=approvalRequired ? TicketStatus.VALIDATION : TicketStatus.DONE; if (!approvalRequired) { slaState="COMPLETED"; completedAt=Instant.now(); } }
  public void approve(){ require(TicketStatus.VALIDATION); status=TicketStatus.DONE; slaState="COMPLETED"; completedAt=Instant.now(); }
  public void requestCorrection(){ require(TicketStatus.VALIDATION); finishResolutionPause(Instant.now()); status=TicketStatus.REOPENED; slaState="ON_TIME"; }
  public void cancel(){ require(TicketStatus.OPEN, TicketStatus.TRIAGE, TicketStatus.WAITING_FOR_CLIENT); if (executionStarted || waitOrigin==WaitOrigin.EXECUTION) throw new IllegalStateException("Cancelamento não permitido após execução"); status=TicketStatus.CANCELLED; slaState="CANCELLED"; }
  public void reopenFromDone(){ require(TicketStatus.DONE); status=TicketStatus.REOPENED; resolutionCycle++; resolutionCycleStartedAt=Instant.now(); resolutionPauseIntervals="[]"; resolutionPausedAt=null; slaRuleSnapshot=null; resolutionDueAt=null; completedAt=null; slaState="PENDING_RECONFIRMATION"; }
  public void markEffectiveResponse(){ if (responseCompletedAt == null) responseCompletedAt=Instant.now(); }
  public void assignCampaign(Campaign campaign){ if(!campaign.getClient().getId().equals(client.getId())) throw new IllegalArgumentException("Campanha incompatível"); this.campaign=campaign; this.pendingCampaign=null; }
  public void updateClassificationReferences(Campaign campaign, DemandType demandType) { require(TicketStatus.TRIAGE, TicketStatus.REOPENED); assignCampaign(campaign); if(!this.demandType.getId().equals(demandType.getId()))this.typeSnapshot=typeSnapshot(demandType); this.demandType=demandType; }
  private String typeSnapshot(DemandType type) {try{var mapper=new com.fasterxml.jackson.databind.ObjectMapper();var snapshot=mapper.createObjectNode();snapshot.put("id",type.getId().toString());snapshot.put("name",type.getName());snapshot.put("version",type.getVersionNumber());snapshot.put("approvalRequired",type.isApprovalRequired());snapshot.put("evidenceRequired",type.isEvidenceRequired());snapshot.set("fieldDefinitions",mapper.readTree(type.getFieldDefinitions()));return mapper.writeValueAsString(snapshot);}catch(java.io.IOException ex){throw new IllegalArgumentException("Configuração do tipo inválida");}}
  private void require(TicketStatus... allowed) { for (TicketStatus value: allowed) if(status==value) return; throw new IllegalStateException("Transição inválida a partir de " + status); }
}
