package br.pucminas.sige.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.campaigns.domain.*;
import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.files.domain.*;
import br.pucminas.sige.notifications.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.sla.application.SlaService;
import br.pucminas.sige.sla.domain.*;
import br.pucminas.sige.tickets.application.TicketNumberGenerator;
import br.pucminas.sige.tickets.domain.*;
import br.pucminas.sige.users.domain.*;
import jakarta.persistence.EntityManager;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class DemoDataInitializerTest {
  @TempDir Path storage;
  private final AppUserRepository users=mock(AppUserRepository.class);
  private final ClientRepository clients=mock(ClientRepository.class);
  private final CampaignRepository campaigns=mock(CampaignRepository.class);
  private final DemandTypeRepository types=mock(DemandTypeRepository.class);
  private final SlaRuleRepository rules=mock(SlaRuleRepository.class);
  private final TicketRepository tickets=mock(TicketRepository.class);
  private final TicketHistoryRepository history=mock(TicketHistoryRepository.class);
  private final TicketCommentRepository comments=mock(TicketCommentRepository.class);
  private final TicketAttachmentRepository attachments=mock(TicketAttachmentRepository.class);
  private final NotificationRepository notifications=mock(NotificationRepository.class);
  private final TicketNumberGenerator numbers=mock(TicketNumberGenerator.class);
  private final SlaService sla=mock(SlaService.class);
  private final JdbcTemplate jdbc=mock(JdbcTemplate.class);
  private final EntityManager entities=mock(EntityManager.class);
  private final BCryptPasswordEncoder passwords=new BCryptPasswordEncoder();

  private DemoDataInitializer initializer() {
    return new DemoDataInitializer(users,clients,campaigns,types,rules,tickets,history,comments,attachments,
        notifications,numbers,sla,passwords,jdbc,entities,"123",storage.toString());
  }

  @Test
  void addsAllScenariosEvenWhenAnAdminExistsAndPreservesItsPassword() throws Exception {
    String original=passwords.encode("original-admin-password");
    AppUser admin=new AppUser("Administrador","admin@sige.demo",original,Role.ADMIN,null);
    when(users.findByEmailIgnoreCase("admin@sige.demo")).thenReturn(Optional.of(admin));
    when(users.save(any())).thenAnswer(call->call.getArgument(0));
    when(clients.save(any())).thenAnswer(call->call.getArgument(0));
    when(campaigns.save(any())).thenAnswer(call->call.getArgument(0));
    when(types.save(any())).thenAnswer(call->call.getArgument(0));
    when(tickets.saveAndFlush(any())).thenAnswer(call->call.getArgument(0));
    AtomicInteger sequence=new AtomicInteger(1000);
    when(numbers.next()).thenAnswer(call->"SIGE-"+sequence.getAndIncrement());
    when(sla.calculate(any(),any())).thenReturn(new SlaService.Calculation("{}",Instant.now().plusSeconds(3600),Instant.now().plusSeconds(7200)));
    when(jdbc.queryForObject("SELECT COUNT(*) FROM demo_seed_runs WHERE seed_key = ?",Long.class,DemoDataInitializer.SEED)).thenReturn(0L);

    initializer().run(new DefaultApplicationArguments(new String[0]));

    ArgumentCaptor<Ticket> saved=ArgumentCaptor.forClass(Ticket.class);
    verify(tickets,times(48)).saveAndFlush(saved.capture());
    List<Ticket> fixtures=saved.getAllValues().stream().distinct().toList();
    assertEquals(24,fixtures.size());
    for (TicketStatus status:TicketStatus.values()) assertEquals(3,fixtures.stream().filter(t->t.getStatus()==status).count());
    assertEquals(original,admin.getPasswordHash());
    verify(users,never()).save(same(admin));
    verify(users,never()).count();
    assertTrue(fixtures.stream().anyMatch(t->t.getWaitOrigin()==WaitOrigin.TRIAGE));
    assertTrue(fixtures.stream().anyMatch(t->t.getWaitOrigin()==WaitOrigin.EXECUTION&&t.isComplementReceived()));
    assertTrue(fixtures.stream().anyMatch(t->t.getStatus()==TicketStatus.REOPENED&&t.getResolutionCycle()==1));
    assertTrue(fixtures.stream().anyMatch(t->t.getStatus()==TicketStatus.REOPENED&&t.getResolutionCycle()==2));
    assertTrue(fixtures.stream().filter(t->t.getStatus()==TicketStatus.VALIDATION).allMatch(t->t.getDemandType().isApprovalRequired()));
    ArgumentCaptor<TicketAttachment> files=ArgumentCaptor.forClass(TicketAttachment.class);
    verify(attachments,times(33)).save(files.capture());
    for (TicketAttachment file:files.getAllValues()) {
      Path path=storage.resolve(file.getStorageKey());
      assertTrue(Files.exists(path));
      assertEquals(file.getSizeBytes(),Files.size(path));
    }
    verify(jdbc).update("INSERT INTO demo_seed_runs (seed_key, applied_at) VALUES (?, CURRENT_TIMESTAMP(6))",DemoDataInitializer.SEED);
  }

  @Test
  void skipsACompletedSeedWithoutRecreatingTicketsOrChangingAccounts() {
    when(jdbc.queryForObject("SELECT COUNT(*) FROM demo_seed_runs WHERE seed_key = ?",Long.class,DemoDataInitializer.SEED)).thenReturn(1L);
    initializer().run(new DefaultApplicationArguments(new String[0]));
    verifyNoInteractions(users,clients,campaigns,types,rules,tickets,history,comments,attachments,notifications,numbers,sla);
  }
}
