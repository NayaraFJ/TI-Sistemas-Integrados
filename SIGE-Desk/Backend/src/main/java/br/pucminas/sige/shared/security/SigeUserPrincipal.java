package br.pucminas.sige.shared.security;

import br.pucminas.sige.users.domain.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record SigeUserPrincipal(UUID id, String email, String passwordHash, boolean active, String name, String role, UUID clientId) implements UserDetails {
  public static SigeUserPrincipal from(AppUser user) {
    return new SigeUserPrincipal(user.getId(), user.getEmail(), user.getPasswordHash(), user.isActive(), user.getName(), user.getRole().name(), user.getClient() == null ? null : user.getClient().getId());
  }
  @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role)); }
  @Override public String getPassword() { return passwordHash; }
  @Override public String getUsername() { return email; }
  @Override public boolean isEnabled() { return active; }
}
