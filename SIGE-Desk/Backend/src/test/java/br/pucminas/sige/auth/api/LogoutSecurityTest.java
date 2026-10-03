package br.pucminas.sige.auth.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.shared.security.DatabaseUserDetailsService;
import br.pucminas.sige.shared.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class LogoutSecurityTest {
  @Autowired MockMvc mvc;
  @MockitoBean DatabaseUserDetailsService users;
  @MockitoBean CurrentUser current;

  @Test
  void invalidatesTheSessionAndReturnsNoContentWithoutARedirect() throws Exception {
    MockHttpSession session=authenticatedSession();
    mvc.perform(post("/api/v1/auth/logout").session(session).with(csrf()))
        .andExpect(status().isNoContent())
        .andExpect(header().doesNotExist("Location"))
        .andExpect(cookie().maxAge("JSESSIONID",0));
    assertTrue(session.isInvalid());
  }

  @Test
  void requiresCsrfProtectionBeforeEndingTheSession() throws Exception {
    MockHttpSession session=authenticatedSession();
    mvc.perform(post("/api/v1/auth/logout").session(session)).andExpect(status().isForbidden());
    org.junit.jupiter.api.Assertions.assertFalse(session.isInvalid());
  }

  private MockHttpSession authenticatedSession() {
    MockHttpSession session=new MockHttpSession();
    var context=SecurityContextHolder.createEmptyContext();
    context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated("admin@sige.demo",null,
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,context);
    return session;
  }
}
