package br.pucminas.sige.shared.api;
import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.campaigns.domain.*;
import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.sla.domain.*;
import br.pucminas.sige.users.domain.*;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.shared.domain.Role;
import jakarta.persistence.EntityManager;
import java.util.*;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1") @Transactional(readOnly=true)
public class CatalogPageController {
  private final EntityManager em;private final CurrentUser current;
  public CatalogPageController(EntityManager em,CurrentUser current){this.em=em;this.current=current;}
  @io.swagger.v3.oas.annotations.media.Schema(name="CatalogPagePageResponse") public record PageResponse(List<Map<String,Object>> items,long total,int page,int size){}
  @GetMapping("/{resource}/page") public PageResponse page(@PathVariable String resource,@RequestParam(defaultValue="") String search,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="25") int size){
    if(page<0||size<1||size>100||(long)page*size>Integer.MAX_VALUE)throw new IllegalArgumentException("Paginação inválida");
    if(resource.equals("clients")||resource.equals("campaigns"))current.requireRole(Role.SERVICE,Role.ADMIN);else current.requireRole(Role.ADMIN);
    String entity=switch(resource){case "clients"->"Client";case "campaigns"->"Campaign";case "demand-types"->"DemandType";case "sla-rules"->"SlaRule";case "users"->"AppUser";default->throw new IllegalArgumentException("Cadastro inválido");};
    String clause=" where lower(e.name) like :search escape '\\'";if(resource.equals("clients")||resource.equals("users"))clause+=" or lower(e.email) like :search escape '\\'";if(resource.equals("campaigns"))clause+=" or lower(e.channel) like :search escape '\\' or lower(e.objective) like :search escape '\\' or lower(e.client.name) like :search escape '\\'";
    String value="%"+search.toLowerCase(Locale.ROOT).replace("\\","\\\\").replace("%","\\%").replace("_","\\_")+"%";
    long total=em.createQuery("select count(e) from "+entity+" e"+clause,Long.class).setParameter("search",value).getSingleResult();
    var rows=em.createQuery("select e from "+entity+" e"+clause+" order by e.name,e.id",Object.class).setParameter("search",value).setFirstResult(page*size).setMaxResults(size).getResultList();
    return new PageResponse(rows.stream().map(this::view).toList(),total,page,size);
  }
  private Map<String,Object> view(Object entity){String fields=entity instanceof Client?"id name contactName email phone active":entity instanceof Campaign?"id name channel objective active":entity instanceof DemandType?"id name active approvalRequired evidenceRequired fieldDefinitions versionNumber":entity instanceof SlaRule?"id name scope timezone businessDays businessStart businessEnd holidays pauseInValidation deadlines active versionNumber":"id name email role active";
    var bean=new BeanWrapperImpl(entity);Map<String,Object> values=new LinkedHashMap<>();for(String field:fields.split(" "))values.put(field,bean.getPropertyValue(field));
    Client client=entity instanceof Campaign c?c.getClient():entity instanceof AppUser u?u.getClient():entity instanceof SlaRule r?r.getClient():null;
    if(entity instanceof Campaign||entity instanceof AppUser||entity instanceof SlaRule){values.put("clientId",client==null?null:client.getId());values.put("clientName",client==null?null:client.getName());}
    if(entity instanceof SlaRule r)values.put("demandTypeId",r.getDemandType()==null?null:r.getDemandType().getId());return values;
  }
}
