package br.pucminas.sige.campaigns.api;

import br.pucminas.sige.campaigns.domain.*;
import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/campaigns") @Transactional
public class CampaignController {
  private final CampaignRepository campaigns; private final ClientRepository clients; private final CurrentUser current;
  public CampaignController(CampaignRepository campaigns,ClientRepository clients,CurrentUser current){this.campaigns=campaigns;this.clients=clients;this.current=current;}
  record Request(@NotBlank String clientId,@NotBlank @Size(max=160) String name,@NotBlank @Size(max=80) String channel,@NotBlank @Size(max=160) String objective){} record Item(String id,String clientId,String clientName,String name,String channel,String objective,boolean active){}
  @GetMapping public List<Item> list(){current.requireRole(Role.SERVICE,Role.ADMIN);return campaigns.findAll().stream().map(this::item).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody Request request){current.requireRole(Role.SERVICE,Role.ADMIN);return item(campaigns.save(new Campaign(client(request.clientId()),request.name(),request.channel(),request.objective())));}
  @PutMapping("/{id}") public Item update(@PathVariable UUID id,@Valid @RequestBody Request request){current.requireRole(Role.SERVICE,Role.ADMIN);var campaign=required(id);campaign.update(client(request.clientId()),request.name(),request.channel(),request.objective());return item(campaign);}
  @PostMapping("/{id}/active") public Item setActive(@PathVariable UUID id,@RequestParam boolean value){current.requireRole(Role.SERVICE,Role.ADMIN);var campaign=required(id);campaign.setActive(value);return item(campaign);}
  private Client client(String id){return clients.findById(UUID.fromString(id)).filter(Client::isActive).orElseThrow(()->new IllegalArgumentException("Cliente indisponível"));} private Campaign required(UUID id){return campaigns.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Campanha não encontrada"));} private Item item(Campaign campaign){return new Item(campaign.getId().toString(),campaign.getClient().getId().toString(),campaign.getClient().getName(),campaign.getName(),campaign.getChannel(),campaign.getObjective(),campaign.isActive());}
}
