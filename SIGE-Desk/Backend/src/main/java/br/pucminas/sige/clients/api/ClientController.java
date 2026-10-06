package br.pucminas.sige.clients.api;

import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/clients") @Transactional
public class ClientController {
  private final ClientRepository clients; private final TicketRepository tickets; private final CurrentUser current;
  public ClientController(ClientRepository clients,TicketRepository tickets,CurrentUser current){this.clients=clients;this.tickets=tickets;this.current=current;}
  @io.swagger.v3.oas.annotations.media.Schema(name="ClientRequest") record Request(@NotBlank @Size(max=160) String name,@NotBlank @Size(max=160) String contactName,@NotBlank @Email String email,@Size(max=40) String phone){} @io.swagger.v3.oas.annotations.media.Schema(name="ClientItem") record Item(String id,String name,String contactName,String email,String phone,boolean active){}
  @GetMapping public List<Item> list(){current.requireRole(Role.SERVICE,Role.ADMIN);return clients.findAll().stream().map(this::item).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);if(clients.existsByEmailIgnoreCase(request.email()))throw new IllegalArgumentException("E-mail já está em uso");return item(clients.save(new Client(request.name(),request.contactName(),request.email(),request.phone())));}
  @PutMapping("/{id}") public Item update(@PathVariable UUID id,@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);var client=required(id);if(!client.getEmail().equalsIgnoreCase(request.email())&&clients.existsByEmailIgnoreCase(request.email()))throw new IllegalArgumentException("E-mail já está em uso");client.update(request.name(),request.contactName(),request.email(),request.phone());return item(client);}
  @PostMapping("/{id}/active") public Item setActive(@PathVariable UUID id,@RequestParam boolean value){current.requireRole(Role.ADMIN);var client=required(id);if(!value&&tickets.countByClientIdAndStatusNotIn(id,List.of(TicketStatus.DONE,TicketStatus.CANCELLED))>0)throw new IllegalStateException("Não é possível inativar cliente com tickets ativos");client.setActive(value);return item(client);}
  private Client required(UUID id){return clients.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado"));} private Item item(Client client){return new Item(client.getId().toString(),client.getName(),client.getContactName(),client.getEmail(),client.getPhone(),client.isActive());}
}
