package br.pucminas.sige.files.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment,UUID>{List<TicketAttachment> findByTicketIdOrderByCreatedAtAsc(UUID ticketId);}
