package br.pucminas.sige.files.api;

import br.pucminas.sige.files.application.TicketFileService;
import br.pucminas.sige.files.domain.TicketAttachment;
import java.time.Instant;
import java.util.*;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api/v1/tickets/{ticketId}/attachments")
public class TicketFileController {
  private final TicketFileService files;
  public TicketFileController(TicketFileService files){this.files=files;}
  record Item(String id,String name,String contentType,long sizeBytes,String kind,String description,String evidenceUrl,Instant createdAt){}
  @GetMapping public List<Item> list(@PathVariable UUID ticketId){return files.list(ticketId).stream().map(this::item).toList();}
  @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE) @ResponseStatus(HttpStatus.CREATED) public Item upload(@PathVariable UUID ticketId,@RequestPart MultipartFile file,@RequestParam(defaultValue="ATTACHMENT") TicketAttachment.Kind kind,@RequestParam(required=false) String description,@RequestParam(required=false) String evidenceUrl){return item(files.upload(ticketId,file,kind,description,evidenceUrl));}
  @GetMapping("/{id}/download") public ResponseEntity<Resource> download(@PathVariable UUID ticketId,@PathVariable UUID id){Resource file=files.download(ticketId,id);return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION,ContentDisposition.attachment().filename(files.originalName(ticketId,id),java.nio.charset.StandardCharsets.UTF_8).build().toString()).body(file);}
  private Item item(TicketAttachment attachment){return new Item(attachment.getId().toString(),attachment.getOriginalName(),attachment.getContentType(),attachment.getSizeBytes(),attachment.getKind().name(),attachment.getEvidenceDescription(),attachment.getEvidenceUrl(),attachment.getCreatedAt());}
}
