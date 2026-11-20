package br.pucminas.sige.tickets.application;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ComplementHistoryTest {
  @Test void multibyteCommentAtTextByteLimitLeavesRoomForHistoryPrefix() {
    String comment = "😀".repeat(16383) + "abc";
    assertEquals(65535, comment.getBytes(StandardCharsets.UTF_8).length);
    String history = TicketService.complementHistory(comment);
    assertTrue(history.startsWith("Comentário: 😀"));
    assertTrue(history.endsWith("…"));
    assertTrue(history.getBytes(StandardCharsets.UTF_8).length < 65535);
    assertEquals(100, history.substring("Comentário: ".length()).codePointCount(0, history.substring("Comentário: ".length()).length()));
  }

  @Test void shortCommentIsPreserved() {
    assertEquals("Comentário: Dados recebidos", TicketService.complementHistory("Dados recebidos"));
  }
}
