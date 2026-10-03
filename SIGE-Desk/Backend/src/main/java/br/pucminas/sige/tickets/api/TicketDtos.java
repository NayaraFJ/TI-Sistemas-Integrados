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
  public record CreateTicketRequest(@NotBlank String clientId, String campaignId, @Size(max=255) String pendingCampaign, @NotBlank String demandTypeId, @NotBlank @Size(max=80) String channel, @NotBlank @Size(max=255) String subject, @NotBlank String description, @NotNull Priority urgency, LocalDate desiredDate, String requesterId, TicketMetrics metrics) {}
  public record StartTriageRequest(@NotBlank String reason) {}
  public record TriageRequest(@NotNull Priority priority, @NotBlank String assigneeId, @NotBlank String demandTypeId, @NotBlank String campaignId, @NotBlank String effectiveResponse, boolean sendToExecution) {}
  public record ReasonRequest(@NotBlank String reason) {}
  public record ExecutionRequest(@NotBlank String actionDescription, String evidenceUrl) {}
  public record CommentRequest(@NotBlank String body, boolean effectiveResponse) {}
  public record TicketItem(String id, String clientId, String campaignId, String demandTypeId, String number, String subject, String clientName, String campaignName, String typeName, String status, String priority, String urgency, String assigneeName, Instant updatedAt, List<String> actions) {}
  public record TicketHistoryItem(String id, String actorName, String action, String fieldName, String oldValue, String newValue, String reason, Instant createdAt) {}
  public record TicketCommentItem(String id, String authorName, String body, boolean effectiveResponse, Instant createdAt) {}
  public record AttachmentItem(String id, String name, String contentType, long sizeBytes, String kind, String description, String evidenceUrl, Instant createdAt) {}
  public record TicketDetail(TicketItem ticket, String description, String pendingCampaign, LocalDate desiredDate, String requesterName, String authorName, String slaState, Instant responseDueAt, Instant resolutionDueAt, int resolutionCycle, String waitOrigin, String waitReason, boolean complementReceived, String metrics, List<TicketCommentItem> comments, List<AttachmentItem> attachments, List<TicketHistoryItem> history) {}
  public record TicketList(List<TicketItem> items, long total, int page, int size) {}
  public record ReportSummary(long activeCount, long completedCount, long classifiedCount, long slaCompliantCount, long overdueCount, Long averageResolutionMinutes) {}
  public record TicketReport(List<TicketItem> items, long total, ReportSummary summary) {}
}
