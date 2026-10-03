package br.pucminas.sige.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity {
  @Column(nullable = false, updatable = false)
  protected Instant createdAt;
  @Column(nullable = false)
  protected Instant updatedAt;

  @PrePersist
  void onCreate() { createdAt = updatedAt = Instant.now(); }
  @PreUpdate
  void onUpdate() { updatedAt = Instant.now(); }

  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
