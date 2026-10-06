package br.pucminas.sige.shared.security;
import br.pucminas.sige.users.domain.AppUser;
import jakarta.persistence.Query;
public final class QueryScope {
  private QueryScope(){}
  public static String ticket(AppUser user,String alias){return switch(user.getRole()){case CLIENT->alias+".client.id=:scopeId";case TRAFFIC_MANAGER->alias+".assignee.id=:scopeId";case SERVICE,ADMIN->"1=1";};}
  public static <T extends Query> T bind(T query,AppUser user){switch(user.getRole()){case CLIENT->query.setParameter("scopeId",user.getClient().getId());case TRAFFIC_MANAGER->query.setParameter("scopeId",user.getId());default->{}}return query;}
}
