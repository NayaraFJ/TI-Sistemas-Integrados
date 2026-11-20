package br.pucminas.sige.auth.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.pucminas.sige.shared.security.CurrentUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;

class LoginFailureTest {
  private final AuthenticationManager manager = mock(AuthenticationManager.class);
  private final AuthController controller = new AuthController(manager, mock(CurrentUser.class));
  private final AuthController.LoginRequest request = new AuthController.LoginRequest("user@example.com", "password");

  @Test void invalidCredentialsReturnUnauthorized() {
    when(manager.authenticate(any())).thenThrow(new BadCredentialsException("internal detail"));
    var error = assertThrows(ResponseStatusException.class, () -> controller.login(request, null, null));
    assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    assertEquals("Credenciais inválidas.", error.getReason());
  }

  @Test void internalAuthenticationFailureIsNotReportedAsInvalidCredentials() {
    when(manager.authenticate(any())).thenThrow(new AuthenticationServiceException("internal failure"));
    assertThrows(AuthenticationServiceException.class, () -> controller.login(request, null, null));
  }
}
