package br.pucminas.sige.tickets.application;

import br.pucminas.sige.tickets.api.TicketDtos;
import br.pucminas.sige.files.application.TicketFileService;
import br.pucminas.sige.files.domain.TicketAttachment;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class TicketCreationService {
  private final TicketService tickets;private final TicketFileService files;
  public TicketCreationService(TicketService tickets,TicketFileService files){this.tickets=tickets;this.files=files;}
  @Transactional public TicketDtos.TicketItem create(TicketDtos.CreateTicketRequest request,MultipartHttpServletRequest multipart){
    var values=new HashMap<String,String>();if(request.metrics()!=null&&request.metrics().fields()!=null)values.putAll(request.metrics().fields());Set<String> supplied=new HashSet<>();
    multipart.getMultiFileMap().forEach((part,items)->{if(part.startsWith("field.")){if(items.size()!=1||items.getFirst().isEmpty())throw new IllegalArgumentException("Envie um arquivo por campo");String name=part.substring(6);supplied.add(name);values.put(name,Objects.toString(items.getFirst().getOriginalFilename(),"arquivo"));}else if(!part.equals("files")&&!part.equals("request"))throw new IllegalArgumentException("Campo de arquivo desconhecido");});
    var m=request.metrics();var metrics=m==null?new TicketDtos.TicketMetrics(null,null,null,null,null,null,null,values):new TicketDtos.TicketMetrics(m.period(),m.impressions(),m.ctr(),m.cpc(),m.conversions(),m.cpa(),m.roas(),values);
    var body=new TicketDtos.CreateTicketRequest(request.clientId(),request.campaignId(),request.pendingCampaign(),request.demandTypeId(),request.channel(),request.subject(),request.description(),request.urgency(),request.desiredDate(),request.requesterId(),metrics);
    var ticket=tickets.create(body,supplied);
    multipart.getMultiFileMap().forEach((part,items)->{if(!part.equals("request"))items.forEach(file->files.upload(UUID.fromString(ticket.id()),file,TicketAttachment.Kind.ATTACHMENT,part.startsWith("field.")?"Campo: "+part.substring(6):null,null));});
    return tickets.detail(UUID.fromString(ticket.id())).ticket();
  }
}
