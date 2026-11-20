package br.pucminas.sige.tickets.api;

import br.pucminas.sige.tickets.application.TicketService;
import br.pucminas.sige.tickets.domain.TicketStatus;
import jakarta.validation.Valid;
import java.util.UUID;
import java.time.LocalDate;
import br.pucminas.sige.shared.domain.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@org.springframework.transaction.annotation.Transactional
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
  private final TicketService service; private final br.pucminas.sige.tickets.application.TicketCreationService creation;
  public TicketController(TicketService service,br.pucminas.sige.tickets.application.TicketCreationService creation) { this.service=service;this.creation=creation; }
  // Access is checked before request-body validation; the version check stays inside the action transaction.
  @ModelAttribute
  public void checkTicketVisibility(@PathVariable(value="id",required=false) UUID id) {
    if (id != null) service.verifyVisibility(id);
  }
  @PostMapping("/{id}/reassign") public TicketDtos.TicketItem reassign(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReassignRequest request){service.verifyVersion(id,expected);return service.reassign(id,request);}
  @GetMapping public TicketDtos.TicketList list(@RequestParam(required=false) String search, @RequestParam(required=false) TicketStatus status,@RequestParam(required=false) UUID clientId,@RequestParam(required=false) UUID campaignId,@RequestParam(required=false) UUID demandTypeId,@RequestParam(required=false) Priority priority,@RequestParam(required=false) UUID assigneeId,@RequestParam(required=false) LocalDate createdFrom,@RequestParam(required=false) LocalDate createdTo,@RequestParam(required=false) Boolean overdue,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="25") int size) { return service.list(search,status,clientId,campaignId,demandTypeId,priority,assigneeId,createdFrom,createdTo,page,size,overdue); }
  @PostMapping(consumes="application/json") @ResponseStatus(HttpStatus.CREATED) public TicketDtos.TicketItem create(@Valid @RequestBody TicketDtos.CreateTicketRequest request) { return service.create(request); }
  @PostMapping(consumes="multipart/form-data") @ResponseStatus(HttpStatus.CREATED) public TicketDtos.TicketItem createMultipart(@Valid @RequestPart("request") TicketDtos.CreateTicketRequest request,org.springframework.web.multipart.MultipartHttpServletRequest multipart){return creation.create(request,multipart);}
  @GetMapping("/{id}") public TicketDtos.TicketDetail detail(@PathVariable UUID id) { return service.detail(id); }
  @PostMapping("/{id}/triage/start") public TicketDtos.TicketItem startTriage(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.StartTriageRequest request){service.verifyVersion(id,expected);return service.startTriage(id,request);}
  @PostMapping("/{id}/triage") public TicketDtos.TicketItem triage(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.TriageRequest request){service.verifyVersion(id,expected);return service.triage(id,request);}
  @PostMapping("/{id}/wait") public TicketDtos.TicketItem waitForComplement(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.waitForComplement(id,request);}
  @PostMapping("/{id}/complement") public TicketDtos.TicketItem complement(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.CommentRequest request){service.verifyVersion(id,expected);return service.receiveComplement(id,request);}
  @PostMapping("/{id}/resume") public TicketDtos.TicketItem resume(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.resume(id,request);}
  @PostMapping("/{id}/execution") public TicketDtos.TicketItem execute(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ExecutionRequest request){service.verifyVersion(id,expected);return service.execute(id,request);}
  @PostMapping("/{id}/approve") public TicketDtos.TicketItem approve(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.approve(id,request);}
  @PostMapping("/{id}/correction") public TicketDtos.TicketItem correct(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.correct(id,request);}
  @PostMapping("/{id}/cancel") public TicketDtos.TicketItem cancel(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.cancel(id,request);}
  @PostMapping("/{id}/reopen") public TicketDtos.TicketItem reopen(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.ReasonRequest request){service.verifyVersion(id,expected);return service.reopen(id,request);}
  @PostMapping("/{id}/comments") @ResponseStatus(HttpStatus.CREATED) public TicketDtos.TicketCommentItem comment(@PathVariable UUID id,@RequestHeader(value="If-Match",required=false) String expected,@Valid @RequestBody TicketDtos.CommentRequest request){service.verifyVersion(id,expected);return service.comment(id,request);}
}
