package br.pucminas.sige.users.domain;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
  Optional<AppUser> findByEmailIgnoreCase(String email);
  boolean existsByEmailIgnoreCase(String email);
  List<AppUser> findByRoleInAndActiveTrue(List<br.pucminas.sige.shared.domain.Role> roles);
  List<AppUser> findByRoleAndActiveTrue(br.pucminas.sige.shared.domain.Role role);
}
