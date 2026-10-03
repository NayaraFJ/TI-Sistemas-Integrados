package br.pucminas.sige.tickets.domain;

import static org.junit.jupiter.api.Assertions.*;
import br.pucminas.sige.campaigns.domain.Campaign;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.users.domain.AppUser;
import java.time.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketTransitionTest {
  private AppUser client; private AppUser manager; private Ticket ticket;
  @BeforeEach void setUp() { Client organization=new Client("Aurora","Marina","aurora@example.test",null); client=new AppUser("Marina","cliente@example.test","hash",Role.CLIENT,organization); manager=new AppUser("Lucas","gestor@example.test","hash",Role.TRAFFIC_MANAGER,null); DemandType type=new DemandType("Ajuste",true,true,"[]"); ticket=new Ticket("SIGE-1000",organization,null,"Campanha pendente",type,"Google Ads","Assunto","Descrição",Priority.HIGH,LocalDate.now(),client,client,"{}"); }
  @Test void completesWithApprovalWhenTypeRequiresIt() { ticket.startTriage(); ticket.classify(Priority.HIGH,manager,"{}",Instant.now(),Instant.now()); ticket.sendToExecution(); ticket.recordExecution(true); assertEquals(TicketStatus.VALIDATION,ticket.getStatus()); ticket.approve(); assertEquals(TicketStatus.DONE,ticket.getStatus()); assertNotNull(ticket.getCompletedAt()); }
  @Test void blocksCancellationAfterExecutionHasStarted() { ticket.startTriage(); ticket.classify(Priority.HIGH,manager,"{}",Instant.now(),Instant.now()); ticket.sendToExecution(); ticket.waitForClient(WaitOrigin.EXECUTION,"Informação"); assertThrows(IllegalStateException.class,ticket::cancel); }
  @Test void requiresComplementBeforeResume() { ticket.startTriage(); ticket.waitForClient(WaitOrigin.TRIAGE,"Dados"); assertThrows(IllegalStateException.class,ticket::resume); ticket.receiveComplement(); ticket.resume(); assertEquals(TicketStatus.TRIAGE,ticket.getStatus()); }
  @Test void triageCanConfirmCampaignAndReplaceDemandTypeSnapshot() { DemandType confirmedType=new DemandType("Relatório",false,false,"[]"); Campaign campaign=new Campaign(client.getClient(),"Captação","Google Ads","Leads"); ticket.startTriage(); ticket.updateClassificationReferences(campaign,confirmedType); assertEquals(campaign,ticket.getCampaign()); assertEquals(confirmedType,ticket.getDemandType()); assertNull(ticket.getPendingCampaign()); assertTrue(ticket.getTypeSnapshot().contains(confirmedType.getId().toString())); }
}
