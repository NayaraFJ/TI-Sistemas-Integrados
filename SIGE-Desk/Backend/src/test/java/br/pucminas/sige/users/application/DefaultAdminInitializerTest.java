package br.pucminas.sige.users.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.users.domain.AppUser;
import br.pucminas.sige.users.domain.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class DefaultAdminInitializerTest {
  private final AppUserRepository users = mock(AppUserRepository.class);
  private final BCryptPasswordEncoder passwords = new BCryptPasswordEncoder();
  private final DefaultApplicationArguments args = new DefaultApplicationArguments(new String[0]);

  @Test
  void createsAnActiveAdminWithAnEncodedPasswordInAnEmptyDatabase() {
    new DefaultAdminInitializer(users, passwords, "123").run(args);

    ArgumentCaptor<AppUser> saved = ArgumentCaptor.forClass(AppUser.class);
    verify(users).save(saved.capture());
    AppUser admin = saved.getValue();
    assertEquals("admin@sige.demo", admin.getEmail());
    assertEquals(Role.ADMIN, admin.getRole());
    assertTrue(admin.isActive());
    assertNull(admin.getClient());
    assertNotEquals("123", admin.getPasswordHash());
    assertTrue(passwords.matches("123", admin.getPasswordHash()));
  }

  @Test
  void preservesExistingUsersAndPasswordsOnRestart() {
    when(users.count()).thenReturn(1L);
    new DefaultAdminInitializer(users, passwords, "a-different-password").run(args);
    verify(users, never()).save(any());
  }

  @Test
  void rejectsAnEmptyInitialPasswordWithoutSavingAUser() {
    assertThrows(IllegalStateException.class,
        () -> new DefaultAdminInitializer(users, passwords, " ").run(args));
    verify(users, never()).save(any());
  }
}
