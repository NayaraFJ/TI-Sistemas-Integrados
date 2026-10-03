package br.pucminas.sige.auth.api;

import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.shared.security.SigeUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final CurrentUser currentUser;
  private final HttpSessionSecurityContextRepository contexts = new HttpSessionSecurityContextRepository();
  public AuthController(AuthenticationManager authenticationManager, CurrentUser currentUser) { this.authenticationManager=authenticationManager; this.currentUser=currentUser; }
  record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
  record SessionResponse(String id, String name, String email, String role, String clientId) {}
  @PostMapping("/login")
  public SessionResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    try {
      Authentication authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password()));
      SecurityContext context = SecurityContextHolder.createEmptyContext(); context.setAuthentication(authentication); SecurityContextHolder.setContext(context); contexts.saveContext(context, servletRequest, servletResponse);
      SigeUserPrincipal user=(SigeUserPrincipal) authentication.getPrincipal();
      return new SessionResponse(user.id().toString(), user.name(), user.email(), user.role(), user.clientId()==null?null:user.clientId().toString());
    } catch (Exception exception) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas."); }
  }
  @GetMapping("/me")
  public SessionResponse me() { var user=currentUser.require(); return new SessionResponse(user.getId().toString(),user.getName(),user.getEmail(),user.getRole().name(),user.getClient()==null?null:user.getClient().getId().toString()); }
  @GetMapping("/csrf")
  public Map<String,String> csrf(jakarta.servlet.http.HttpServletRequest request) { var token=(org.springframework.security.web.csrf.CsrfToken) request.getAttribute(org.springframework.security.web.csrf.CsrfToken.class.getName()); return Map.of("token", token.getToken(), "headerName", token.getHeaderName()); }
  @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(HttpServletRequest request) { var session=request.getSession(false); if(session!=null) session.invalidate(); SecurityContextHolder.clearContext(); }
}
