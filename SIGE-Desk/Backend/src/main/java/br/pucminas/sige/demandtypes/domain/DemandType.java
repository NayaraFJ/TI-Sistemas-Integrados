package br.pucminas.sige.demandtypes.domain;

import br.pucminas.sige.shared.domain.AuditableEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "demand_types")
public class DemandType extends AuditableEntity {
  @Id private UUID id;
  @Column(nullable = false) private String name;
  @Column(nullable = false) private boolean active = true;
  @Column(name = "approval_required", nullable = false) private boolean approvalRequired;
  @Column(name = "evidence_required", nullable = false) private boolean evidenceRequired;
  @Column(name = "field_definitions", columnDefinition = "json", nullable = false) private String fieldDefinitions;
  @Column(name = "version_number", nullable = false) private int versionNumber = 1;
  protected DemandType() {}
  public DemandType(String name, boolean approvalRequired, boolean evidenceRequired, String fieldDefinitions) { id=UUID.randomUUID(); this.name=name; this.approvalRequired=approvalRequired; this.evidenceRequired=evidenceRequired; this.fieldDefinitions=fieldDefinitions; }
  public UUID getId() { return id; }
  public String getName() { return name; }
  public boolean isActive() { return active; }
  public boolean isApprovalRequired() { return approvalRequired; }
  public boolean isEvidenceRequired() { return evidenceRequired; }
  public String getFieldDefinitions() { return fieldDefinitions; }
  public int getVersionNumber() { return versionNumber; }
  public void update(String name, boolean approvalRequired, boolean evidenceRequired, String fieldDefinitions) { this.name=name; this.approvalRequired=approvalRequired; this.evidenceRequired=evidenceRequired; this.fieldDefinitions=fieldDefinitions; this.versionNumber++; }
  public void setActive(boolean active) { this.active=active; }
}
