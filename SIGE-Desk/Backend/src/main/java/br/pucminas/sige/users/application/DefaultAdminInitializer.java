package br.pucminas.sige.users.application;

import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)
public class DefaultAdminInitializer implements ApplicationRunner {
  private static final Logger log = LoggerFactory.getLogger(DefaultAdminInitializer.class);
  private final AppUserRepository users;
  private final PasswordEncoder passwords;
  private final String password;

  public DefaultAdminInitializer(AppUserRepository users, PasswordEncoder passwords,
      @Value("${sige.admin.password}") String password) {
    this.users = users;
    this.passwords = passwords;
    this.password = password;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (users.count() > 0) return;
    if (password == null || password.isBlank()) {
      throw new IllegalStateException("Defina SIGE_ADMIN_PASSWORD para criar o administrador inicial");
    }
    users.save(new AppUser("Administrador SIGE Desk", "admin@sige.demo",
        passwords.encode(password), Role.ADMIN, null));
    log.info("Administrador inicial criado: admin@sige.demo");
  }
}
