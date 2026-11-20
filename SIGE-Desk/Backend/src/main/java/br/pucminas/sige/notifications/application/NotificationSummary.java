package br.pucminas.sige.notifications.application;

import br.pucminas.sige.tickets.domain.Ticket;

/** Conteúdo mínimo do Anexo D, limitado ao tamanho persistido do resumo. */
public final class NotificationSummary {
  private NotificationSummary() {}

  public static String render(Ticket ticket, String event, String context) {
    String prefix = ticket.getNumber() + " | " + text(ticket.getSubject(), 60) + "; ";
    String due = ticket.getResolutionDueAt() == null ? "pendente" : ticket.getResolutionDueAt().toString();
    String priority = ticket.getPriority() == null ? "pendente" : ticket.getPriority().name();
    String origin = ticket.getWaitOrigin() == null ? "não aplicável" : ticket.getWaitOrigin().name();
    String body = switch (event) {
      case "ASSIGNED" -> "Prioridade: " + priority + "; prazo de resolução: " + due
        + "; responsável: " + (ticket.getAssignee() == null ? "pendente" : text(ticket.getAssignee().getName(), 60)) + ".";
      case "COMMENT_ADDED" -> text(context, 260);
      case "WAITING_FOR_CLIENT" -> "Informação necessária: " + text(ticket.getWaitReason(), 140)
        + "; origem: " + origin + "; pausa da resolução: ativa.";
      case "COMPLEMENT_RECEIVED" -> "Informação recebida: " + text(context, 100)
        + "; aguarda conferência para retomar fase: " + origin + "; prazo de resolução: " + due + ".";
      case "TICKET_RESUMED" -> "Complemento confirmado: " + text(context, 230)
        + "; fase retomada: " + ticket.getStatus() + "; prazo de resolução: " + due + ".";
      case "VALIDATION_REQUESTED" -> "Em validação; " + text(context, 280);
      case "DONE" -> "Concluída; ciclo: " + ticket.getResolutionCycle() + "; " + text(context, 250)
        + "; próximo passo: consultar entrega e reabrir com justificativa se necessário.";
      case "REOPENED" -> "Reaberta; ciclo: " + ticket.getResolutionCycle() + "; " + text(context, 180)
        + (ticket.getSlaRuleSnapshot() == null ? "; próximo passo: Atendimento confirma triagem antes da execução." : "; próximo passo: responsável executa a correção.");
      case "CANCELLED" -> "Cancelada; " + text(context, 250) + "; cancelamento confirmado.";
      case "SLA_OVERDUE" -> "Prioridade: " + priority + "; " + text(context, 250);
      default -> text(context, 280);
    };
    return text(prefix + body, 500);
  }

  public static String text(String value, int limit) {
    if (value == null || value.isBlank()) return "não informado";
    String clean = value.replaceAll("[\\p{Cntrl}]+", " ").trim();
    if (clean.codePointCount(0, clean.length()) <= limit) return clean;
    return clean.substring(0, clean.offsetByCodePoints(0, limit - 1)) + "…";
  }
}
