package br.pucminas.sige.reference.api;

import br.pucminas.sige.campaigns.domain.CampaignRepository;
import br.pucminas.sige.clients.domain.ClientRepository;
import br.pucminas.sige.demandtypes.domain.DemandTypeRepository;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@org.springframework.transaction.annotation.Transactional(readOnly=true)
@RestController @RequestMapping("/api/v1/reference")
public class ReferenceController {
  private final ClientRepository clients; private final CampaignRepository campaigns; private final DemandTypeRepository demandTypes; private final AppUserRepository users; private final CurrentUser current;private final br.pucminas.sige.tickets.application.TicketAccessPolicy access;
  public ReferenceController(ClientRepository clients,CampaignRepository campaigns,DemandTypeRepository demandTypes,AppUserRepository users,CurrentUser current,br.pucminas.sige.tickets.application.TicketAccessPolicy access){this.clients=clients;this.campaigns=campaigns;this.demandTypes=demandTypes;this.users=users;this.current=current;this.access=access;}
  @io.swagger.v3.oas.annotations.media.Schema(name="ReferenceOption") record Option(String id,String label){} @io.swagger.v3.oas.annotations.media.Schema(name="ReferenceCampaignItem") record CampaignItem(String id,String clientId,String name,String channel,String objective){} @io.swagger.v3.oas.annotations.media.Schema(name="ReferenceDemandTypeItem") record DemandTypeItem(String id,String name,boolean approvalRequired,boolean evidenceRequired,String fieldDefinitions){} @io.swagger.v3.oas.annotations.media.Schema(name="ReferenceUserItem") record UserItem(String id,String name,@io.swagger.v3.oas.annotations.media.Schema(allowableValues={"CLIENT","SERVICE","TRAFFIC_MANAGER","ADMIN"}) String role,@io.swagger.v3.oas.annotations.media.Schema(types={"string","null"}) String clientId){} @io.swagger.v3.oas.annotations.media.Schema(name="ReferenceResponse") record Response(List<Option> clients,List<CampaignItem> campaigns,List<DemandTypeItem> demandTypes,List<UserItem> trafficManagers,List<UserItem> clientUsers){}
  @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager em;
  @GetMapping public Response all(){
    var actor=current.require();boolean manager=actor.getRole()==Role.TRAFFIC_MANAGER,client=actor.getRole()==Role.CLIENT;
    var organizations=client?List.of(actor.getClient()):manager?em.createQuery("select distinct t.client from Ticket t where t.assignee.id=:id and t.client.active=true",br.pucminas.sige.clients.domain.Client.class).setParameter("id",actor.getId()).getResultList():em.createQuery("select c from Client c where c.active=true order by c.name",br.pucminas.sige.clients.domain.Client.class).getResultList();
    var clientOptions=organizations.stream().filter(br.pucminas.sige.clients.domain.Client::isActive).map(c->new Option(c.getId().toString(),c.getName())).toList();
    var campaignList=client?campaigns.findByClientIdAndActiveTrueOrderByName(actor.getClient().getId()):manager?em.createQuery("select distinct t.campaign from Ticket t where t.assignee.id=:id and t.campaign.active=true",br.pucminas.sige.campaigns.domain.Campaign.class).setParameter("id",actor.getId()).getResultList():em.createQuery("select c from Campaign c where c.active=true order by c.name",br.pucminas.sige.campaigns.domain.Campaign.class).getResultList();
    var campaignItems=campaignList.stream().map(c->new CampaignItem(c.getId().toString(),c.getClient().getId().toString(),c.getName(),c.getChannel(),c.getObjective())).toList();
    var types=em.createQuery("select t from DemandType t where t.active=true order by t.name",br.pucminas.sige.demandtypes.domain.DemandType.class).getResultList().stream().map(t->new DemandTypeItem(t.getId().toString(),t.getName(),t.isApprovalRequired(),t.isEvidenceRequired(),t.getFieldDefinitions())).toList();
    var trafficUsers=manager?List.of(actor):client?em.createQuery("select distinct t.assignee from Ticket t where t.client.id=:id and t.assignee.active=true and t.assignee.role='TRAFFIC_MANAGER'",br.pucminas.sige.users.domain.AppUser.class).setParameter("id",actor.getClient().getId()).getResultList():users.findByRoleInAndActiveTrue(List.of(Role.TRAFFIC_MANAGER));
    var traffic=trafficUsers.stream().map(u->new UserItem(u.getId().toString(),u.getName(),u.getRole().name(),null)).toList();
    var clientUsers=client?List.of(new UserItem(actor.getId().toString(),actor.getName(),actor.getRole().name(),actor.getClient().getId().toString())):manager?List.<UserItem>of():em.createQuery("select u from AppUser u where u.active=true and u.role='CLIENT' and u.client.active=true order by u.name",br.pucminas.sige.users.domain.AppUser.class).getResultList().stream().map(u->new UserItem(u.getId().toString(),u.getName(),u.getRole().name(),u.getClient().getId().toString())).toList();
    return new Response(clientOptions,campaignItems,types,traffic,clientUsers);
  }
}
