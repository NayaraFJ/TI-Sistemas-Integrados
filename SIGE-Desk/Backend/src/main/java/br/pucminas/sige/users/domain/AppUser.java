package br.pucminas.sige.users.domain;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.shared.domain.AuditableEntity;
import br.pucminas.sige.shared.domain.Role;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class AppUser extends AuditableEntity {
  @Id private UUID id;
  @Column(nullable = false) private String name;
  @Column(nullable = false) private String email;
  @Column(name = "password_hash", nullable = false) private String passwordHash;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
  @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "client_id") private Client client;
  @Column(nullable = false) private boolean active = true;

  protected AppUser() {}
  public AppUser(String name, String email, String passwordHash, Role role, Client client) {
    this.id = UUID.randomUUID(); this.name = name; this.email = email.toLowerCase();
    this.passwordHash = passwordHash; this.role = role; this.client = client;
  }
  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getPasswordHash() { return passwordHash; }
  public Role getRole() { return role; }
  public Client getClient() { return client; }
  public boolean isActive() { return active; }
  public void update(String name, String email, Role role, Client client, boolean active) { this.name = name; this.email = email.toLowerCase(); this.role = role; this.client = client; this.active = active; }
  public void changePassword(String passwordHash) { this.passwordHash = passwordHash; }
}
