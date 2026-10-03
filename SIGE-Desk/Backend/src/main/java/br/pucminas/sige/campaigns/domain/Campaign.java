package br.pucminas.sige.campaigns.domain;

import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.shared.domain.AuditableEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "campaigns")
public class Campaign extends AuditableEntity {
  @Id private UUID id;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "client_id", nullable = false) private Client client;
  @Column(nullable = false) private String name;
  @Column(nullable = false) private String channel;
  @Column(nullable = false) private String objective;
  @Column(nullable = false) private boolean active = true;
  protected Campaign() {}
  public Campaign(Client client, String name, String channel, String objective) { id=UUID.randomUUID(); this.client=client; this.name=name; this.channel=channel; this.objective=objective; }
  public UUID getId() { return id; }
  public Client getClient() { return client; }
  public String getName() { return name; }
  public String getChannel() { return channel; }
  public String getObjective() { return objective; }
  public boolean isActive() { return active; }
  public void update(Client client, String name, String channel, String objective) { this.client=client; this.name=name; this.channel=channel; this.objective=objective; }
  public void setActive(boolean active) { this.active=active; }
}
