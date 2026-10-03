package br.pucminas.sige.reference.api;

import br.pucminas.sige.campaigns.domain.CampaignRepository;
import br.pucminas.sige.clients.domain.ClientRepository;
import br.pucminas.sige.demandtypes.domain.DemandTypeRepository;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/reference")
public class ReferenceController {
  private final ClientRepository clients; private final CampaignRepository campaigns; private final DemandTypeRepository demandTypes; private final AppUserRepository users; private final CurrentUser current;
  public ReferenceController(ClientRepository clients,CampaignRepository campaigns,DemandTypeRepository demandTypes,AppUserRepository users,CurrentUser current){this.clients=clients;this.campaigns=campaigns;this.demandTypes=demandTypes;this.users=users;this.current=current;}
  record Option(String id,String label){} record CampaignItem(String id,String clientId,String name,String channel,String objective){} record DemandTypeItem(String id,String name,boolean approvalRequired,boolean evidenceRequired,String fieldDefinitions){} record UserItem(String id,String name,String role,String clientId){} record Response(List<Option> clients,List<CampaignItem> campaigns,List<DemandTypeItem> demandTypes,List<UserItem> trafficManagers,List<UserItem> clientUsers){}
  @GetMapping public Response all(){var actor=current.require();var clientOptions=(actor.getRole()==Role.CLIENT?List.of(actor.getClient()):clients.findAll()).stream().filter(br.pucminas.sige.clients.domain.Client::isActive).map(client->new Option(client.getId().toString(),client.getName())).toList();var campaignItems=(actor.getRole()==Role.CLIENT?campaigns.findByClientIdAndActiveTrueOrderByName(actor.getClient().getId()):campaigns.findAll().stream().filter(br.pucminas.sige.campaigns.domain.Campaign::isActive).toList()).stream().map(campaign->new CampaignItem(campaign.getId().toString(),campaign.getClient().getId().toString(),campaign.getName(),campaign.getChannel(),campaign.getObjective())).toList();var types=demandTypes.findAll().stream().filter(br.pucminas.sige.demandtypes.domain.DemandType::isActive).map(type->new DemandTypeItem(type.getId().toString(),type.getName(),type.isApprovalRequired(),type.isEvidenceRequired(),type.getFieldDefinitions())).toList();var traffic=users.findByRoleInAndActiveTrue(List.of(Role.TRAFFIC_MANAGER)).stream().map(user->new UserItem(user.getId().toString(),user.getName(),user.getRole().name(),null)).toList();var clientUsers=actor.getRole()==Role.CLIENT?List.of(new UserItem(actor.getId().toString(),actor.getName(),actor.getRole().name(),actor.getClient().getId().toString())):users.findByRoleAndActiveTrue(Role.CLIENT).stream().map(user->new UserItem(user.getId().toString(),user.getName(),user.getRole().name(),user.getClient().getId().toString())).toList();return new Response(clientOptions,campaignItems,types,traffic,clientUsers);}
}
