package br.pucminas.sige.demandtypes.api;

import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.shared.domain.Role;
import br.pucminas.sige.shared.security.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1/demand-types") @Transactional
public class DemandTypeController {
  private final DemandTypeRepository types; private final CurrentUser current;
  public DemandTypeController(DemandTypeRepository types,CurrentUser current){this.types=types;this.current=current;}
  @io.swagger.v3.oas.annotations.media.Schema(name="DemandTypeRequest") record Request(@NotBlank @Size(max=160) String name,boolean approvalRequired,boolean evidenceRequired,@NotBlank String fieldDefinitions){} @io.swagger.v3.oas.annotations.media.Schema(name="DemandTypeItem") record Item(String id,String name,boolean active,boolean approvalRequired,boolean evidenceRequired,String fieldDefinitions,int versionNumber){}
  @GetMapping public List<Item> list(){current.requireRole(Role.ADMIN);return types.findAll().stream().map(this::item).toList();}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Item create(@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);validateJson(request.fieldDefinitions());return item(types.save(new DemandType(request.name(),request.approvalRequired(),request.evidenceRequired(),request.fieldDefinitions())));}
  @PutMapping("/{id}") public Item update(@PathVariable UUID id,@Valid @RequestBody Request request){current.requireRole(Role.ADMIN);validateJson(request.fieldDefinitions());var type=required(id);type.update(request.name(),request.approvalRequired(),request.evidenceRequired(),request.fieldDefinitions());return item(type);}
  @PostMapping("/{id}/active") public Item setActive(@PathVariable UUID id,@RequestParam boolean value){current.requireRole(Role.ADMIN);var type=required(id);type.setActive(value);return item(type);}
  private DemandType required(UUID id){return types.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tipo de demanda não encontrado"));} private void validateJson(String source){br.pucminas.sige.demandtypes.application.DynamicFieldValidator.definitions(source);} private Item item(DemandType type){return new Item(type.getId().toString(),type.getName(),type.isActive(),type.isApprovalRequired(),type.isEvidenceRequired(),type.getFieldDefinitions(),type.getVersionNumber());}
}
