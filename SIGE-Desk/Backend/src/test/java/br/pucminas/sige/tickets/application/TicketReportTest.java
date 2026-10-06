package br.pucminas.sige.tickets.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import br.pucminas.sige.campaigns.domain.CampaignRepository;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.clients.domain.ClientRepository;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.demandtypes.domain.DemandTypeRepository;
import br.pucminas.sige.files.domain.TicketAttachmentRepository;
import br.pucminas.sige.notifications.application.NotificationService;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.sla.application.SlaService;
import br.pucminas.sige.tickets.api.TicketDtos;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketCommentRepository;
import br.pucminas.sige.tickets.domain.TicketHistoryRepository;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

class TicketReportTest {
  @Test
  void excludesCancelledTicketsFromSlaAndCalculatesAverageCompletionTime() {
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser serviceUser = new AppUser("Beatriz", "service@example.test", "hash", Role.SERVICE, null);
    AppUser requester = new AppUser("Marina", "client@example.test", "hash", Role.CLIENT, client);
    AppUser manager = new AppUser("Lucas", "manager@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    Instant created = Instant.parse("2020-09-01T12:00:00Z");
    Ticket completed = classified("SIGE-4001", client, requester, manager, created, Instant.parse("2020-09-03T12:00:00Z"));
    ReflectionTestUtils.setField(completed, "responseCompletedAt", created.plusSeconds(30*60));
    completed.sendToExecution(); completed.recordExecution(false); ReflectionTestUtils.setField(completed, "completedAt", created.plusSeconds(120 * 60));
    Ticket overdue = classified("SIGE-4002", client, requester, manager, created, Instant.parse("2020-09-02T12:00:00Z"));
    Ticket cancelled = classified("SIGE-4003", client, requester, manager, created, Instant.parse("2020-09-02T12:00:00Z"));
    cancelled.cancel();

    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    when(current.require()).thenReturn(serviceUser);
    when(access.visibleTickets(serviceUser)).thenReturn(List.of(completed, overdue, cancelled));

    TicketService service = service(access, current);
    TicketDtos.TicketReport report = service.report(null, null, null, null, null, null, null, null, null);

    assertEquals(3, report.total());
    assertEquals(2, report.summary().classifiedCount());
    assertEquals(1, report.summary().slaCompliantCount());
    assertEquals(1, report.summary().overdueCount());
    assertEquals(120L, report.summary().averageResolutionMinutes());
  }

  @Test
  void returnsOnlyTheRequestedPageWhileKeepingTheFilteredTotal() {
    Client client = new Client("Aurora", "Marina", "aurora@example.test", null);
    AppUser serviceUser = new AppUser("Beatriz", "service@example.test", "hash", Role.SERVICE, null);
    AppUser requester = new AppUser("Marina", "client@example.test", "hash", Role.CLIENT, client);
    AppUser manager = new AppUser("Lucas", "manager@example.test", "hash", Role.TRAFFIC_MANAGER, null);
    TicketAccessPolicy access = Mockito.mock(TicketAccessPolicy.class);
    CurrentUser current = Mockito.mock(CurrentUser.class);
    when(current.require()).thenReturn(serviceUser);
    when(access.visibleTickets(serviceUser)).thenReturn(List.of(
        classified("SIGE-5001", client, requester, manager, Instant.now(), Instant.now().plusSeconds(3600)),
        classified("SIGE-5002", client, requester, manager, Instant.now(), Instant.now().plusSeconds(3600)),
        classified("SIGE-5003", client, requester, manager, Instant.now(), Instant.now().plusSeconds(3600))));

    TicketService service = service(access, current);
    TicketDtos.TicketList page = service.list(null, null, null, null, null, null, null, null, null, 1, 2);
    TicketDtos.TicketList empty = service.list(null, null, null, null, null, null, null, null, null, 2, 2);

    assertEquals(3, page.total());
    assertEquals(1, page.page());
    assertEquals(2, page.size());
    assertEquals(List.of("SIGE-5003"), page.items().stream().map(TicketDtos.TicketItem::number).toList());
    assertEquals(List.of(), empty.items());
  }

  private Ticket classified(String number, Client client, AppUser requester, AppUser manager, Instant created, Instant resolutionDue) {
    DemandType type = new DemandType("Ajuste", false, false, "[]");
    Ticket ticket = new Ticket(number, client, null, "Campanha", type, "Google Ads", "Assunto", "Descrição", Priority.MEDIUM, LocalDate.now(), requester, requester, "{}");
    ReflectionTestUtils.setField(ticket, "createdAt", created);
    ReflectionTestUtils.setField(ticket, "updatedAt", created);
    ticket.startTriage();
    ticket.classify(Priority.MEDIUM, manager, "{}", created.plusSeconds(60 * 60), resolutionDue);
    return ticket;
  }

  private TicketService service(TicketAccessPolicy access, CurrentUser current) {
    TicketRepository repository=Mockito.mock(TicketRepository.class);
    when(repository.findAll(Mockito.<org.springframework.data.jpa.domain.Specification<Ticket>>any(),Mockito.any(org.springframework.data.domain.Sort.class))).thenAnswer(call->access.visibleTickets(current.require()));
    when(repository.findAll(Mockito.<org.springframework.data.jpa.domain.Specification<Ticket>>any(),Mockito.any(org.springframework.data.domain.Pageable.class))).thenAnswer(call->{var page=(org.springframework.data.domain.Pageable)call.getArgument(1);var all=access.visibleTickets(current.require());int start=(int)Math.min(page.getOffset(),all.size());return new org.springframework.data.domain.PageImpl<>(all.subList(start,Math.min(start+page.getPageSize(),all.size())),page,all.size());});
    return new TicketService(repository, Mockito.mock(ClientRepository.class), Mockito.mock(CampaignRepository.class), Mockito.mock(DemandTypeRepository.class), Mockito.mock(AppUserRepository.class), Mockito.mock(TicketHistoryRepository.class), Mockito.mock(TicketCommentRepository.class), Mockito.mock(TicketAttachmentRepository.class), Mockito.mock(TicketNumberGenerator.class), Mockito.mock(SlaService.class), Mockito.mock(NotificationService.class), current, access, new ObjectMapper());
  }
}
