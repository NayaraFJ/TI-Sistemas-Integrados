package br.pucminas.sige.files.application;

import br.pucminas.sige.files.domain.*;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.application.TicketAccessPolicy;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketStatus;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service @Transactional
public class TicketFileService {
  private static final long MAX_FILE_SIZE=10L*1024*1024;
  private final TicketAttachmentRepository attachments; private final TicketAccessPolicy access; private final CurrentUser current; private final Path root;
  public TicketFileService(TicketAttachmentRepository attachments,TicketAccessPolicy access,CurrentUser current,@Value("${sige.storage.path}") String path){this.attachments=attachments;this.access=access;this.current=current;this.root=Paths.get(path).toAbsolutePath().normalize();}
  public TicketAttachment upload(UUID ticketId,MultipartFile file,TicketAttachment.Kind kind,String description,String evidenceUrl){Ticket ticket=access.requireVisible(ticketId,current.require());if(ticket.getStatus()==TicketStatus.CANCELLED)throw new IllegalStateException("Ticket cancelado é somente consulta");if(file.isEmpty())throw new IllegalArgumentException("Arquivo vazio");if(file.getSize()>MAX_FILE_SIZE)throw new IllegalArgumentException("Arquivo excede o limite de 10 MB");String contentType=Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");if(!contentType.matches("(image/.*|application/pdf|text/csv|application/vnd\\..*)"))throw new IllegalArgumentException("Tipo de arquivo não permitido");String name=Paths.get(Optional.ofNullable(file.getOriginalFilename()).orElse("arquivo")).getFileName().toString();String key=ticketId+"/"+UUID.randomUUID();try{Path target=root.resolve(key).normalize();if(!target.startsWith(root))throw new IllegalStateException("Caminho de arquivo inválido");Files.createDirectories(target.getParent());try(InputStream input=file.getInputStream()){Files.copy(input,target,StandardCopyOption.REPLACE_EXISTING);}}catch(IOException exception){throw new IllegalStateException("Não foi possível armazenar o arquivo");}return attachments.save(new TicketAttachment(ticket,current.require(),name,key,contentType,file.getSize(),kind,description,evidenceUrl));}
  @Transactional(readOnly=true) public Resource download(UUID ticketId,UUID id){Ticket ticket=access.requireVisible(ticketId,current.require());TicketAttachment attachment=attachments.findById(id).filter(item->item.getTicket().getId().equals(ticket.getId())).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Arquivo não encontrado"));try{Path target=root.resolve(attachment.getStorageKey()).normalize();if(!target.startsWith(root))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Arquivo não encontrado");Resource file=new UrlResource(target.toUri());if(!file.exists()||!file.isReadable())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Arquivo não encontrado");return file;}catch(java.net.MalformedURLException exception){throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Arquivo não encontrado");}}
  @Transactional(readOnly=true) public List<TicketAttachment> list(UUID ticketId){access.requireVisible(ticketId,current.require());return attachments.findByTicketIdOrderByCreatedAtAsc(ticketId);}
}
