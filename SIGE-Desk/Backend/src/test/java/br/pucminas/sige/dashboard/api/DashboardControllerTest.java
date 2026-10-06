package br.pucminas.sige.dashboard.api;
import br.pucminas.sige.shared.security.QueryScope;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.clients.domain.Client;
import br.pucminas.sige.shared.domain.Role;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class DashboardControllerTest {
  @Test void limitsQueriesToTheCurrentClient(){var client=new Client("Aurora","Contato","a@example.test",null);var user=new AppUser("Cliente","u@example.test","hash",Role.CLIENT,client);Query query=mock(Query.class);QueryScope.bind(query,user);assertEquals("t.client.id=:scopeId",QueryScope.ticket(user,"t"));verify(query).setParameter("scopeId",client.getId());}
  @Test void limitsQueriesToTheAssignedManager(){var user=new AppUser("Gestor","g@example.test","hash",Role.TRAFFIC_MANAGER,null);Query query=mock(Query.class);QueryScope.bind(query,user);assertEquals("t.assignee.id=:scopeId",QueryScope.ticket(user,"t"));verify(query).setParameter("scopeId",user.getId());}
}
