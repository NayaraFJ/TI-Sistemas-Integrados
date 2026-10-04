package br.pucminas.sige.shared.security;

import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
  private final AppUserRepository users;
  public CurrentUser(AppUserRepository users) { this.users = users; }
  public AppUser require() {
    var authentication=SecurityContextHolder.getContext().getAuthentication();
    if(authentication==null)throw new AccessDeniedException("Sessão inválida");
    Object principal=authentication.getPrincipal();
    if (!(principal instanceof SigeUserPrincipal user)) throw new AccessDeniedException("Sessão inválida");
    return users.findById(user.id()).filter(AppUser::isActive).orElseThrow(() -> new AccessDeniedException("Sessão inválida"));
  }
  public void requireRole(Role... roles) {
    Role actual = require().getRole(); for (Role role : roles) if (actual == role) return; throw new AccessDeniedException("Ação não permitida");
  }
  public UUID id() { return require().getId(); }
}
