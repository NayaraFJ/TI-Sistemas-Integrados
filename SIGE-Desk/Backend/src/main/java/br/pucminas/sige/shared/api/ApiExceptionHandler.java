package br.pucminas.sige.shared.api;

import jakarta.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
  record ApiError(String code, String message, Instant timestamp, Map<String, String> fields) {}
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
    Map<String,String> fields = ex.getBindingResult().getFieldErrors().stream().collect(java.util.stream.Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b)->a));
    return ResponseEntity.badRequest().body(new ApiError("VALIDATION_ERROR", "Há campos inválidos.", Instant.now(), fields));
  }
  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ApiError> denied(AccessDeniedException ex) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError("FORBIDDEN", "Ação não permitida.", Instant.now(), Map.of())); }
  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<ApiError> invalid(IllegalArgumentException ex) { return ResponseEntity.badRequest().body(new ApiError("BUSINESS_RULE", ex.getMessage(), Instant.now(), Map.of())); }
  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<ApiError> conflict(IllegalStateException ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("INVALID_TRANSITION", ex.getMessage(), Instant.now(), Map.of())); }
  @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
  ResponseEntity<ApiError> optimistic(Exception ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("VERSION_CONFLICT", "O ticket foi alterado por outra pessoa.", Instant.now(), Map.of())); }
  @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
  ResponseEntity<ApiError> integrity(org.springframework.dao.DataIntegrityViolationException ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("DATA_CONFLICT", "O cadastro conflita com dados existentes. Confira os campos informados.", Instant.now(), Map.of())); }
  @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
  ResponseEntity<ApiError> uploadLimit(Exception ex) { return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ApiError("UPLOAD_TOO_LARGE", "O envio excede o limite permitido de arquivos.", Instant.now(), Map.of())); }
  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<ApiError> status(ResponseStatusException ex) { return ResponseEntity.status(ex.getStatusCode()).body(new ApiError("REQUEST_ERROR", ex.getReason(), Instant.now(), Map.of())); }
}
