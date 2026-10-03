package br.pucminas.sige.shared.security;

import br.pucminas.sige.users.domain.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
  private final AppUserRepository users;
  public DatabaseUserDetailsService(AppUserRepository users) { this.users = users; }
  @Override public UserDetails loadUserByUsername(String email) {
    return users.findByEmailIgnoreCase(email).map(SigeUserPrincipal::from).orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
  }
}
