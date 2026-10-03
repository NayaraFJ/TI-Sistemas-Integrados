package br.pucminas.sige.shared.outbox;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="outbox_events")
public class OutboxEvent {
  @Id private UUID id;
  @Column(name="aggregate_type",nullable=false) private String aggregateType;
  @Column(name="aggregate_id",nullable=false) private UUID aggregateId;
  @Column(name="event_type",nullable=false) private String eventType;
  @Column(nullable=false,columnDefinition="json") private String payload;
  @Column(name="occurred_at",nullable=false) private Instant occurredAt;
  @Column(name="processed_at") private Instant processedAt;
  protected OutboxEvent(){}
  public OutboxEvent(String aggregateType,UUID aggregateId,String eventType,String payload){id=UUID.randomUUID();this.aggregateType=aggregateType;this.aggregateId=aggregateId;this.eventType=eventType;this.payload=payload;occurredAt=Instant.now();}
  public UUID getId(){return id;} public String getAggregateType(){return aggregateType;} public UUID getAggregateId(){return aggregateId;} public String getEventType(){return eventType;} public String getPayload(){return payload;} public Instant getOccurredAt(){return occurredAt;} public Instant getProcessedAt(){return processedAt;} public void markProcessed(){processedAt=Instant.now();}
}
