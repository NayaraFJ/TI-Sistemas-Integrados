package br.pucminas.sige.sla.domain;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.AuditableEntity;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "sla_rules")
public class SlaRule extends AuditableEntity {
  public enum Scope { DEFAULT, CLIENT, DEMAND_TYPE, CLIENT_AND_DEMAND_TYPE }
  @Id private UUID id;
  @Column(nullable=false) private String name;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Scope scope;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="client_id") private Client client;
  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="demand_type_id") private DemandType demandType;
  @Column(nullable=false) private String timezone;
  @Column(name="business_days", columnDefinition="json", nullable=false) private String businessDays;
  @Column(name="business_start", nullable=false) private LocalTime businessStart;
  @Column(name="business_end", nullable=false) private LocalTime businessEnd;
  @Column(columnDefinition="json", nullable=false) private String holidays;
  @Column(name="pause_in_validation", nullable=false) private boolean pauseInValidation;
  @Column(columnDefinition="json", nullable=false) private String deadlines;
  @Column(nullable=false) private boolean active = true;
  @Column(name="version_number", nullable=false) private int versionNumber = 1;
  protected SlaRule() {}
  public SlaRule(String name, Scope scope, Client client, DemandType demandType, String deadlines) {
    id=UUID.randomUUID(); this.name=name; this.scope=scope; this.client=client; this.demandType=demandType; timezone="America/Sao_Paulo"; businessDays="[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\"]"; businessStart=LocalTime.of(8,0); businessEnd=LocalTime.of(18,0); holidays="[]"; this.deadlines=deadlines;
  }
  public UUID getId() { return id; }
  public String getName() { return name; }
  public Scope getScope() { return scope; }
  public Client getClient() { return client; }
  public DemandType getDemandType() { return demandType; }
  public String getTimezone() { return timezone; }
  public String getBusinessDays() { return businessDays; }
  public LocalTime getBusinessStart() { return businessStart; }
  public LocalTime getBusinessEnd() { return businessEnd; }
  public String getHolidays() { return holidays; }
  public boolean isPauseInValidation() { return pauseInValidation; }
  public String getDeadlines() { return deadlines; }
  public boolean isActive() { return active; }
  public int getVersionNumber() { return versionNumber; }
  public void update(String name, Scope scope, Client client, DemandType demandType, String timezone, String businessDays, LocalTime businessStart, LocalTime businessEnd, String holidays, boolean pauseInValidation, String deadlines, boolean active) { this.name=name; this.scope=scope; this.client=client; this.demandType=demandType; this.timezone=timezone; this.businessDays=businessDays; this.businessStart=businessStart; this.businessEnd=businessEnd; this.holidays=holidays; this.pauseInValidation=pauseInValidation; this.deadlines=deadlines; this.active=active; this.versionNumber++; }
}
