package br.pucminas.sige.reports.api;

import br.pucminas.sige.shared.domain.*;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.api.TicketDtos;
import br.pucminas.sige.tickets.application.TicketService;
import br.pucminas.sige.tickets.domain.TicketStatus;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/reports")
public class ReportController {
  private final TicketService tickets; private final CurrentUser current;
  public ReportController(TicketService tickets,CurrentUser current){this.tickets=tickets;this.current=current;}
  @GetMapping("/tickets") public TicketDtos.TicketReport report(@RequestParam(required=false) String search,@RequestParam(required=false) TicketStatus status,@RequestParam(required=false) UUID clientId,@RequestParam(required=false) UUID campaignId,@RequestParam(required=false) UUID demandTypeId,@RequestParam(required=false) Priority priority,@RequestParam(required=false) UUID assigneeId,@RequestParam(required=false) LocalDate createdFrom,@RequestParam(required=false) LocalDate createdTo){current.requireRole(Role.SERVICE,Role.ADMIN);return tickets.report(search,status,clientId,campaignId,demandTypeId,priority,assigneeId,createdFrom,createdTo);}
  @GetMapping(value="/tickets/export",produces="text/csv") public ResponseEntity<byte[]> export(@RequestParam(required=false) String search,@RequestParam(required=false) TicketStatus status,@RequestParam(required=false) UUID clientId,@RequestParam(required=false) UUID campaignId,@RequestParam(required=false) UUID demandTypeId,@RequestParam(required=false) Priority priority,@RequestParam(required=false) UUID assigneeId,@RequestParam(required=false) LocalDate createdFrom,@RequestParam(required=false) LocalDate createdTo){var result=report(search,status,clientId,campaignId,demandTypeId,priority,assigneeId,createdFrom,createdTo);StringBuilder csv=new StringBuilder("Número;Assunto;Cliente;Campanha;Tipo;Status;Prioridade;Responsável;Atualização\n");for(var item:result.items())csv.append(row(item.number())).append(';').append(row(item.subject())).append(';').append(row(item.clientName())).append(';').append(row(item.campaignName())).append(';').append(row(item.typeName())).append(';').append(row(item.status())).append(';').append(row(item.priority())).append(';').append(row(item.assigneeName())).append(';').append(row(item.updatedAt().toString())).append('\n');return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=sige-desk-tickets.csv").contentType(MediaType.parseMediaType("text/csv;charset=UTF-8")).body(("\uFEFF"+csv).getBytes(StandardCharsets.UTF_8));}
  private String row(String value){return "\""+(value==null?"":value.replace("\"","\"\""))+"\"";}
}
