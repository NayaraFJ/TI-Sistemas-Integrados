package br.pucminas.sige.users.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import br.pucminas.sige.clients.domain.ClientRepository;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.tickets.domain.TicketRepository;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class LastAdminDeactivationTest {
  @Test void blocksLastActiveAdminWithoutMutatingIt() {
    var users=mock(AppUserRepository.class);
    var tickets=mock(TicketRepository.class);
    var current=mock(CurrentUser.class);
    var admin=new AppUser("Admin QA", "admin@example.test", "hash", Role.ADMIN, null);
    when(users.findById(admin.getId())).thenReturn(Optional.of(admin));
    when(users.findByRoleAndActiveTrue(Role.ADMIN)).thenReturn(List.of(admin));
    var controller=new UserController(users,mock(ClientRepository.class),tickets,mock(PasswordEncoder.class),current);
    var request=new UserController.UpdateRequest(admin.getName(),admin.getEmail(),Role.ADMIN,null,false,null);
    var error=assertThrows(IllegalStateException.class,()->controller.update(admin.getId(),request));
    assertEquals("O último administrador ativo não pode perder o acesso",error.getMessage());
    assertTrue(admin.isActive());
    assertEquals(Role.ADMIN,admin.getRole());
    verify(current).requireRole(Role.ADMIN);
    verify(users,never()).save(any());
  }
  @Test void allowsDeactivationWhenAnotherActiveAdminExists() {
    var users=mock(AppUserRepository.class);
    var admin=new AppUser("Admin QA", "admin@example.test", "hash", Role.ADMIN, null);
    var other=new AppUser("Outro QA", "outro@example.test", "hash", Role.ADMIN, null);
    when(users.findById(admin.getId())).thenReturn(Optional.of(admin));
    when(users.findByRoleAndActiveTrue(Role.ADMIN)).thenReturn(List.of(admin,other));
    var controller=new UserController(users,mock(ClientRepository.class),mock(TicketRepository.class),mock(PasswordEncoder.class),mock(CurrentUser.class));
    var result=controller.update(admin.getId(),new UserController.UpdateRequest(admin.getName(),admin.getEmail(),Role.ADMIN,null,false,null));
    assertFalse(result.active());
    assertFalse(admin.isActive());
    assertTrue(other.isActive());
  }
}
