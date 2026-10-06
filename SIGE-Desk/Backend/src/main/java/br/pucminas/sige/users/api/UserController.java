package br.pucminas.sige.users.api;

import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.domain.*;
import br.pucminas.sige.users.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/users") @Transactional
public class UserController {
  private final AppUserRepository users; private final ClientRepository clients; private final TicketRepository tickets; private final PasswordEncoder passwords; private final CurrentUser current;
  public UserController(AppUserRepository users,ClientRepository clients,TicketRepository tickets,PasswordEncoder passwords,CurrentUser current){this.users=users;this.clients=clients;this.tickets=tickets;this.passwords=passwords;this.current=current;}
  @io.swagger.v3.oas.annotations.media.Schema(name="UserCreateRequest") record CreateRequest(@NotBlank @Size(max=160) String name,@NotBlank @Email String email,@NotBlank @Size(min=12,max=128) String password,@NotNull Role role,String clientId){} @io.swagger.v3.oas.annotations.media.Schema(name="UserUpdateRequest") record UpdateRequest(@NotBlank @Size(max=160) String name,@NotBlank @Email String email,@NotNull Role role,String clientId,boolean active,@Size(min=12,max=128) String password){} @io.swagger.v3.oas.annotations.media.Schema(name="UserItem") record Item(String id,String name,String email,String role,String clientId,boolean active){}
  @GetMapping public List<Item> list(){current.requireRole(Role.ADMIN);return users.findAll().stream().map(this::item).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody CreateRequest request){current.requireRole(Role.ADMIN);if(users.existsByEmailIgnoreCase(request.email()))throw new IllegalArgumentException("E-mail já está em uso");return item(users.save(new AppUser(request.name(),request.email(),passwords.encode(request.password()),request.role(),clientFor(request.role(),request.clientId()))));}
  @PutMapping("/{id}") public Item update(@PathVariable UUID id,@Valid @RequestBody UpdateRequest request){current.requireRole(Role.ADMIN);var user=required(id);if(!user.getEmail().equalsIgnoreCase(request.email())&&users.existsByEmailIgnoreCase(request.email()))throw new IllegalArgumentException("E-mail já está em uso");if((!request.active()||request.role()!=Role.TRAFFIC_MANAGER)&&tickets.countByAssigneeIdAndStatusNotIn(id,List.of(TicketStatus.DONE,TicketStatus.CANCELLED))>0)throw new IllegalStateException("Não é possível inativar responsável com tickets ativos");if(user.getRole()==Role.ADMIN&&user.isActive()&&(!request.active()||request.role()!=Role.ADMIN)&&users.findByRoleAndActiveTrue(Role.ADMIN).stream().noneMatch(other->!other.getId().equals(id)))throw new IllegalStateException("O último administrador ativo não pode perder o acesso");if(user.getRole()==Role.CLIENT&&user.getClient()!=null&&(!request.active()||request.role()!=Role.CLIENT||!user.getClient().getId().toString().equals(request.clientId()))){var organization=user.getClient().getId();boolean waiting=tickets.findByClientIdOrderByUpdatedAtDesc(organization).stream().anyMatch(ticket->ticket.getStatus()==TicketStatus.WAITING_FOR_CLIENT||ticket.getStatus()==TicketStatus.VALIDATION);boolean another=users.findByRoleAndActiveTrue(Role.CLIENT).stream().anyMatch(other->!other.getId().equals(id)&&other.getClient()!=null&&other.getClient().getId().equals(organization));if(waiting&&!another)throw new IllegalStateException("Mantenha outro Cliente ativo para responder ou validar os tickets da organização");}user.update(request.name(),request.email(),request.role(),clientFor(request.role(),request.clientId()),request.active());if(request.password()!=null&&!request.password().isBlank())user.changePassword(passwords.encode(request.password()));return item(user);}
  private Client clientFor(Role role,String id){if(role!=Role.CLIENT)return null;if(id==null||id.isBlank())throw new IllegalArgumentException("Usuário Cliente exige organização vinculada");return clients.findById(UUID.fromString(id)).filter(Client::isActive).orElseThrow(()->new IllegalArgumentException("Cliente indisponível"));} private AppUser required(UUID id){return users.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado"));} private Item item(AppUser user){return new Item(user.getId().toString(),user.getName(),user.getEmail(),user.getRole().name(),user.getClient()==null?null:user.getClient().getId().toString(),user.isActive());}
}
