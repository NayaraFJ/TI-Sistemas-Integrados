package br.pucminas.sige.tickets.application;

import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.tickets.domain.Ticket;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.users.domain.AppUser;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TicketAccessPolicy {
  private final TicketRepository tickets;
  public TicketAccessPolicy(TicketRepository tickets) { this.tickets=tickets; }
  public List<Ticket> visibleTickets(AppUser user) {
    return switch(user.getRole()) {
      case CLIENT -> tickets.findByClientIdOrderByUpdatedAtDesc(user.getClient().getId());
      case TRAFFIC_MANAGER -> tickets.findByAssigneeIdOrderByUpdatedAtDesc(user.getId());
      case SERVICE, ADMIN -> tickets.findAllByOrderByUpdatedAtDesc();
    };
  }
  public Ticket requireVisible(UUID id, AppUser user) {
    Ticket ticket=tickets.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ticket não encontrado"));
    if (!canAccess(ticket,user)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ticket não encontrado");
    return ticket;
  }
  public boolean canAccess(Ticket ticket, AppUser user) {
    return switch(user.getRole()) {
      case CLIENT -> user.getClient()!=null && user.getClient().getId().equals(ticket.getClient().getId());
      case TRAFFIC_MANAGER -> ticket.getAssignee()!=null && ticket.getAssignee().getId().equals(user.getId());
      case SERVICE, ADMIN -> true;
    };
  }
}
