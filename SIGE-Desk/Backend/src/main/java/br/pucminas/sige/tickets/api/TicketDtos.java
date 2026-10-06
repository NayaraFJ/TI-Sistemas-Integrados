package br.pucminas.sige.tickets.api;

import br.pucminas.sige.shared.domain.Priority;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;

public final class TicketDtos {
  private TicketDtos() {}
  public record TicketMetrics(@Size(max=120) String period, @PositiveOrZero Long impressions, @DecimalMin(value="0.0") BigDecimal ctr, @DecimalMin(value="0.0") BigDecimal cpc, @PositiveOrZero Long conversions, @DecimalMin(value="0.0") BigDecimal cpa, @DecimalMin(value="0.0") BigDecimal roas, Map<String, @Size(max=1000) String> fields) {}
  public record CreateTicketRequest(@NotBlank String clientId, String campaignId, @Size(max=255) String pendingCampaign, @NotBlank String demandTypeId, @NotBlank @Size(max=80) String channel, @NotBlank @Size(max=255) String subject, @NotBlank String description, @NotNull Priority urgency, LocalDate desiredDate, String requesterId, @jakarta.validation.Valid TicketMetrics metrics) {}
  public record StartTriageRequest(@NotBlank String reason) {}
  public record TriageRequest(@NotNull Priority priority, @NotBlank String assigneeId, @NotBlank String demandTypeId, @NotBlank String campaignId, @NotBlank String effectiveResponse, boolean sendToExecution) {}
  public record ReassignRequest(@NotBlank String assigneeId,@NotBlank String reason) {}
  public record ReasonRequest(@NotBlank String reason) {}
  public record ExecutionRequest(@NotBlank String actionDescription, String evidenceUrl) {}
  public record CommentRequest(@NotBlank String body, boolean effectiveResponse) {}
  public record TicketItem(String id, String clientId, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String campaignId, String demandTypeId, String number, String subject, String clientName, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String campaignName, String typeName, @io.swagger.v3.oas.annotations.media.Schema(allowableValues={"OPEN","TRIAGE","EXECUTION","WAITING_FOR_CLIENT","VALIDATION","DONE","REOPENED","CANCELLED"}) String status, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"},allowableValues={"URGENT","HIGH","MEDIUM","LOW"}) String priority, @io.swagger.v3.oas.annotations.media.Schema(allowableValues={"URGENT","HIGH","MEDIUM","LOW"}) String urgency, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String assigneeName, Instant updatedAt, List<String> actions, long version, String slaState, boolean overdue) {}
  public record TicketHistoryItem(String id, String actorName, String action, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String fieldName, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String oldValue, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String newValue, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String reason, Instant createdAt) {}
  public record TicketCommentItem(String id, String authorName, String body, boolean effectiveResponse, Instant createdAt) {}
  public record AttachmentItem(String id, String name, String contentType, long sizeBytes, @io.swagger.v3.oas.annotations.media.Schema(allowableValues={"ATTACHMENT","EVIDENCE"}) String kind, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String description, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String evidenceUrl, Instant createdAt) {}
  public record TicketDetail(TicketItem ticket, String description, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String pendingCampaign, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) LocalDate desiredDate, String requesterName, String authorName, String slaState, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) Instant responseDueAt, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) Instant resolutionDueAt, int resolutionCycle, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String waitOrigin, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String waitReason, boolean complementReceived, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String metrics, List<TicketCommentItem> comments, List<AttachmentItem> attachments, List<TicketHistoryItem> history, String channel, @io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) Instant responseCompletedAt, String responseSlaState, String resolutionSlaState, long elapsedBusinessMinutes, Map<String,String> fieldLabels) {}
  public record TicketList(List<TicketItem> items, long total, int page, int size) {}
  public record ReportSummary(long activeCount, long completedCount, long classifiedCount, long slaCompliantCount, long overdueCount, @io.swagger.v3.oas.annotations.media.Schema(types={"integer","null"}) Long averageResolutionMinutes) {}
  public record TicketReport(List<TicketItem> items, long total, ReportSummary summary) {}
}
