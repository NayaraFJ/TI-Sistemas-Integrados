package br.pucminas.sige.shared.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiExceptionHandlerTest {
  @Test
  void returnsConflictCodeForOptimisticLocking() {
    var response = new ApiExceptionHandler().optimistic(new OptimisticLockException());

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("VERSION_CONFLICT", response.getBody().code());
  }
}
