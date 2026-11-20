package br.pucminas.sige.tickets.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.campaigns.domain.Campaign;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.demandtypes.domain.DemandType;
import br.pucminas.sige.shared.domain.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TicketOriginalFieldLabelsTest {
  @Test void reclassificationKeepsOriginalLabelsAndUsesNewRules() throws Exception {
    var original=new DemandType("Original",false,false,"[{\"name\":\"date\",\"label\":\"Data original\"},{\"name\":\"brief\",\"label\":\"Arquivo\"}]");
    var next=new DemandType("Novo",true,true,"[{\"name\":\"date\",\"label\":\"Data nova\"}]");
    var third=new DemandType("Terceiro",false,true,"[]");
    var client=mock(Client.class); var campaign=mock(Campaign.class); var id=UUID.randomUUID();
    when(client.getId()).thenReturn(id); when(campaign.getClient()).thenReturn(client);
    var ticket=new Ticket("QA",client,campaign,null,original,"Web","Assunto","Descrição",Priority.LOW,null,null,null,"{\"fields\":{\"date\":\"2026-11-19\",\"brief\":\"arquivo\"}}");
    ticket.startTriage(); ticket.updateClassificationReferences(campaign,next);
    assertEquals("Data original",ticket.getOriginalFieldLabels().get("date"));
    assertEquals("Arquivo",ticket.getOriginalFieldLabels().get("brief"));
    assertTrue(ticket.isApprovalRequired()); assertTrue(ticket.isEvidenceRequired());
    var snapshot=new ObjectMapper().readTree(ticket.getTypeSnapshot());
    assertEquals("Data nova",snapshot.path("fieldDefinitions").get(0).path("label").asText());
    assertEquals("2026-11-19",new ObjectMapper().readTree(ticket.getMetrics()).path("fields").path("date").asText());
    ticket.updateClassificationReferences(campaign,third);
    assertEquals("Data original",ticket.getOriginalFieldLabels().get("date"));
    assertFalse(ticket.isApprovalRequired()); assertTrue(ticket.isEvidenceRequired());
  }
}
