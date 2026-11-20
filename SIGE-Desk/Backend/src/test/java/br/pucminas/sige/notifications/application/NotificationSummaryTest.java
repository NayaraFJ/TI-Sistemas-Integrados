package br.pucminas.sige.notifications.application;

import static org.junit.jupiter.api.Assertions.*;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.WaitOrigin;
import br.pucminas.sige.users.domain.AppUser;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class NotificationSummaryTest {
  private final Instant due = Instant.parse("2026-11-20T12:00:00Z");
  private Ticket ticket() {
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser requester = new AppUser("Marina", "marina@example.test", "hash", Role.CLIENT, client);
    AppUser manager = new AppUser("Lucas", "lucas@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    Ticket t = new Ticket("SIGE-7001", client, null, "Campanha", new DemandType("Ajuste", false, false, "[]"),
      "Google Ads", "Campanha de orçamento", "Descrição", Priority.LOW, LocalDate.now(), requester, requester, "{}");
    t.startTriage(); t.classify(Priority.HIGH, manager, "{}", due, due); t.sendToExecution();
    return t;
  }

  @Test void assignmentIncludesSubjectOfficialPriorityAndDeadline() {
    String summary = NotificationSummary.render(ticket(), "ASSIGNED", "Atribuído");
    for (String expected : new String[]{"SIGE-7001", "Campanha de orçamento", "HIGH", due.toString(), "Lucas"}) assertTrue(summary.contains(expected), expected);
  }

  @Test void waitingAndResumeExplainInformationOriginPauseAndDeadline() {
    Ticket t = ticket(); t.waitForClient(WaitOrigin.EXECUTION, "Informe o novo limite");
    String waiting = NotificationSummary.render(t, "WAITING_FOR_CLIENT", "");
    for (String expected : new String[]{"Informe o novo limite", "EXECUTION", "pausa", "ativa"}) assertTrue(waiting.contains(expected), expected);
    String received = NotificationSummary.render(t, "COMPLEMENT_RECEIVED", "Limite de R$ 100 recebido");
    assertTrue(received.contains("Limite de R$ 100")); assertTrue(received.contains("EXECUTION")); assertTrue(received.contains(due.toString()));
    t.receiveComplement(); t.resume();
    String resumed = NotificationSummary.render(t, "TICKET_RESUMED", "Informação recebida: Comentário: Limite de R$ 100; confirmação: Limite conferido");
    assertTrue(resumed.contains("Informação recebida: Comentário: Limite de R$ 100; confirmação: Limite conferido")); assertTrue(resumed.contains("EXECUTION")); assertTrue(resumed.contains(due.toString()));
  }

  @Test void commentsAndValidationRetainTheirActionableContext() {
    String comment = NotificationSummary.render(ticket(), "COMMENT_ADDED", "Autor: Lucas; comentário: orçamento atualizado");
    assertTrue(comment.contains("Lucas")); assertTrue(comment.contains("orçamento atualizado"));
    String validation = NotificationSummary.render(ticket(), "VALIDATION_REQUESTED", "Ação: orçamento ajustado; material: https://example.test/evidencia; Cliente: aprovar ou solicitar correção.");
    for (String expected : new String[]{"orçamento ajustado", "https://example.test/evidencia", "aprovar", "correção"}) assertTrue(validation.contains(expected), expected);
  }

  @Test void terminalEventsExplainReasonCycleNextStepAndCancellationActor() {
    Ticket t = ticket(); t.recordExecution(false);
    String done = NotificationSummary.render(t, "DONE", "Justificativa: entrega aprovada");
    for (String expected : new String[]{"Concluída", "ciclo: 1", "entrega aprovada", "próximo passo"}) assertTrue(done.contains(expected), expected);
    t.reopenFromDone();
    String reopened = NotificationSummary.render(t, "REOPENED", "Justificativa: ajustar limite");
    for (String expected : new String[]{"Reaberta", "ciclo: 2", "ajustar limite", "próximo passo"}) assertTrue(reopened.contains(expected), expected);
    String cancelled = NotificationSummary.render(t, "CANCELLED", "Motivo: duplicidade; autor: Beatriz");
    for (String expected : new String[]{"duplicidade", "Beatriz", "confirmado"}) assertTrue(cancelled.contains(expected), expected);
  }

  @Test void correctionAndNewCycleHaveDifferentNextSteps() {
    Ticket t = ticket();
    t.recordExecution(true);
    t.requestCorrection();
    String correction = NotificationSummary.render(t, "REOPENED", "Justificativa: correção solicitada");
    assertTrue(correction.contains("responsável executa a correção"));
    assertFalse(correction.contains("confirma triagem"));
    t.recordExecution(true); t.approve(); t.reopenFromDone();
    String reopening = NotificationSummary.render(t, "REOPENED", "Justificativa: novo ciclo");
    assertTrue(reopening.contains("confirma triagem"));
  }

  @Test void resumedSummaryIncludesReceivedAttachmentAndConfirmation() {
    Ticket t = ticket(); t.waitForClient(WaitOrigin.EXECUTION, "Envie o arquivo");
    t.receiveComplement(); t.resume();
    String summary = NotificationSummary.render(t, "TICKET_RESUMED", "Informação recebida: Anexo: comprovante.pdf; confirmação: suficiente");
    for (String expected : new String[]{"comprovante.pdf", "suficiente", "EXECUTION", due.toString()}) assertTrue(summary.contains(expected), expected);
  }
  @Test void overdueProvidesPriorityTypeDeadlineAndDelay() {
    String summary = NotificationSummary.render(ticket(), "SLA_OVERDUE", "Prazo de resolução vencido em " + due + "; atraso: 60 segundos; ciclo: 1.");
    for (String expected : new String[]{"HIGH", "resolução", due.toString(), "atraso: 60 segundos"}) assertTrue(summary.contains(expected), expected);
  }

  @Test void longUnicodeContentIsExplicitlyAbbreviatedWithinDatabaseLimit() {
    String summary = NotificationSummary.render(ticket(), "COMMENT_ADDED", "😀".repeat(600));
    assertTrue(summary.codePointCount(0, summary.length()) <= 500);
    assertTrue(summary.endsWith("…"));
    assertTrue(summary.contains("😀"));
    assertFalse(summary.contains("\n"));
  }
}
