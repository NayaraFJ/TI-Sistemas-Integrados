package br.pucminas.sige.clients.domain;

import br.pucminas.sige.shared.domain.AuditableEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client extends AuditableEntity {
  @Id private UUID id;
  @Column(nullable = false) private String name;
  @Column(name = "contact_name", nullable = false) private String contactName;
  @Column(nullable = false) private String email;
  private String phone;
  @Column(nullable = false) private boolean active = true;
  protected Client() {}
  public Client(String name, String contactName, String email, String phone) {
    id = UUID.randomUUID(); this.name = name; this.contactName = contactName; this.email = email.toLowerCase(); this.phone = phone;
  }
  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getContactName() { return contactName; }
  public String getEmail() { return email; }
  public String getPhone() { return phone; }
  public boolean isActive() { return active; }
  public void update(String name, String contactName, String email, String phone) { this.name=name; this.contactName=contactName; this.email=email.toLowerCase(); this.phone=phone; }
  public void setActive(boolean active) { this.active = active; }
}
