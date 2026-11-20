package br.pucminas.sige.sla.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.pucminas.sige.clients.domain.ClientRepository;
import br.pucminas.sige.demandtypes.domain.DemandTypeRepository;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import br.pucminas.sige.sla.domain.SlaRule;
import br.pucminas.sige.sla.domain.SlaRuleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class LastDefaultRuleDeactivationTest {
  @Test void rejectsDeactivationOfLastDefaultWithoutChangingRule() {
    var deadlines="{\"LOW\":{\"responseHours\":1,\"resolutionHours\":2},\"MEDIUM\":{\"responseHours\":1,\"resolutionHours\":2},\"HIGH\":{\"responseHours\":1,\"resolutionHours\":2},\"URGENT\":{\"responseHours\":1,\"resolutionHours\":2}}";
    var rule=new SlaRule("Padrão QA", SlaRule.Scope.DEFAULT, null, null, deadlines);
    var rules=mock(SlaRuleRepository.class);
    var current=mock(CurrentUser.class);
    when(rules.findById(rule.getId())).thenReturn(Optional.of(rule));
    when(rules.findByActiveTrue()).thenReturn(List.of(rule));
    var controller=new SlaRuleController(rules,mock(ClientRepository.class),mock(DemandTypeRepository.class),current);
    var request=new SlaRuleController.Request(rule.getName(),rule.getScope(),null,null,rule.getTimezone(),rule.getBusinessDays(),rule.getBusinessStart(),rule.getBusinessEnd(),rule.getHolidays(),rule.isPauseInValidation(),deadlines,false);
    var error=assertThrows(IllegalStateException.class,()->controller.update(rule.getId(),request));
    assertEquals("A última regra padrão não pode ser desativada",error.getMessage());
    assertTrue(rule.isActive());
    assertEquals(SlaRule.Scope.DEFAULT,rule.getScope());
    assertEquals(1,rule.getVersionNumber());
    verify(current).requireRole(Role.ADMIN);
    verify(rules,never()).save(any());
  }
}
