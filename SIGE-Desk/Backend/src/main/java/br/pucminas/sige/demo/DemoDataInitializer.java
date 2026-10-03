package br.pucminas.sige.demo;

import br.pucminas.sige.campaigns.domain.*;
import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.files.domain.*;
import br.pucminas.sige.notifications.domain.*;
import br.pucminas.sige.shared.domain.*;
import br.pucminas.sige.sla.application.SlaService;
import br.pucminas.sige.sla.domain.*;
import br.pucminas.sige.tickets.application.TicketNumberGenerator;
import br.pucminas.sige.tickets.domain.*;
import br.pucminas.sige.users.domain.*;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("demo")
@Order(0)
public class DemoDataInitializer implements ApplicationRunner {
  static final String SEED = "prototype-validation-v1";
  private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);
  private static final String DEADLINES = "{\"URGENT\":{\"responseHours\":2,\"resolutionHours\":8},\"HIGH\":{\"responseHours\":4,\"resolutionHours\":16},\"MEDIUM\":{\"responseHours\":8,\"resolutionHours\":24},\"LOW\":{\"responseHours\":16,\"resolutionHours\":40}}";
  private static final String[] SUBJECTS = {
      "Aumentar orçamento do vestibular", "Conferir relatório semanal de leads", "Pausar anúncio com oferta encerrada",
      "Informar orçamento autorizado", "Validar criativo da pós-graduação", "Entregar relatório de desempenho",
      "Corrigir segmentação por região", "Cancelar campanha de evento adiado",
      "Cadastrar campanha de Black Friday", "Classificar solicitação de remarketing", "Investigar queda de conversões",
      "Confirmar URL da página de destino", "Aprovar novo anúncio de energia solar", "Concluir ajuste de orçamento aprovado",
      "Reabrir revisão de público após conclusão", "Cancelar alteração duplicada",
      "Solicitar análise de ROAS", "Revisar prioridade do lançamento", "Corrigir campanha com prazo vencido",
      "Conferir complemento recebido do cliente", "Validar anúncio antes da publicação", "Entregar análise mensal de CPA",
      "Reabrir análise com divergência no período", "Cancelar demanda sem campanha vinculada"
  };
  private final AppUserRepository users;
  private final ClientRepository clients;
  private final CampaignRepository campaigns;
  private final DemandTypeRepository demandTypes;
  private final SlaRuleRepository slaRules;
  private final TicketRepository tickets;
  private final TicketHistoryRepository history;
  private final TicketCommentRepository comments;
  private final TicketAttachmentRepository attachments;
  private final NotificationRepository notifications;
  private final TicketNumberGenerator numbers;
  private final SlaService sla;
  private final PasswordEncoder passwords;
  private final JdbcTemplate jdbc;
  private final EntityManager entities;
  private final String demoPassword;
  private final Path storage;

  public DemoDataInitializer(AppUserRepository users, ClientRepository clients, CampaignRepository campaigns,
      DemandTypeRepository demandTypes, SlaRuleRepository slaRules, TicketRepository tickets,
      TicketHistoryRepository history, TicketCommentRepository comments, TicketAttachmentRepository attachments,
      NotificationRepository notifications, TicketNumberGenerator numbers, SlaService sla,
      PasswordEncoder passwords, JdbcTemplate jdbc, EntityManager entities,
      @Value("${sige.demo.password}") String demoPassword, @Value("${sige.storage.path}") String storage) {
    this.users=users; this.clients=clients; this.campaigns=campaigns; this.demandTypes=demandTypes;
    this.slaRules=slaRules; this.tickets=tickets; this.history=history; this.comments=comments;
    this.attachments=attachments; this.notifications=notifications; this.numbers=numbers; this.sla=sla;
    this.passwords=passwords; this.jdbc=jdbc; this.entities=entities; this.demoPassword=demoPassword;
    this.storage=Paths.get(storage).toAbsolutePath().normalize();
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    // Serializa a carga com a mesma trava utilizada na numeração de tickets.
    jdbc.queryForObject("SELECT next_value FROM ticket_sequences WHERE name = 'SIGE' FOR UPDATE", Long.class);
    if (alreadyLoaded()) {
      log.info("Carga de validação demo já aplicada; cadastros e alterações preservados");
      return;
    }
    if (demoPassword == null || demoPassword.isBlank()) throw new IllegalStateException("Defina SIGE_DEMO_PASSWORD");
    String hash = passwords.encode(demoPassword);
    Client aurora = client("Aurora Educação", "Marina Costa", "marketing@aurora.demo");
    Client horizonte = client("Horizonte Solar", "Rafael Lima", "midia@horizonte.demo");
    Client viva = client("Viva Saúde", "Camila Alves", "marketing@viva.demo");
    Client archived = client("Arquivo Studio (demo inativo)", "Paulo Dias", "arquivo@studio.demo");
    if (archived.isActive()) { archived.setActive(false); clients.save(archived); }
    List<Client> organizations = List.of(aurora, horizonte, viva);
    List<AppUser> requesters = List.of(
        user("Marina Costa", "cliente@aurora.demo", Role.CLIENT, aurora, hash),
        user("Rafael Lima", "rafael@horizonte.demo", Role.CLIENT, horizonte, hash),
        user("Camila Alves", "cliente@viva.demo", Role.CLIENT, viva, hash));
    AppUser service = user("Beatriz Souza", "atendimento@sige.demo", Role.SERVICE, null, hash);
    AppUser secondService = user("Diego Santos", "atendimento2@sige.demo", Role.SERVICE, null, hash);
    List<AppUser> managers = List.of(user("Lucas Martins", "gestor@sige.demo", Role.TRAFFIC_MANAGER, null, hash),
        user("Ana Ribeiro", "gestor2@sige.demo", Role.TRAFFIC_MANAGER, null, hash));
    AppUser admin = user("Administrador SIGE Desk", "admin@sige.demo", Role.ADMIN, null, hash);
    if (!users.existsByEmailIgnoreCase("inativo@sige.demo")) {
      AppUser inactive = new AppUser("Usuário inativo (demo)", "inativo@sige.demo", hash, Role.SERVICE, null);
      inactive.update(inactive.getName(), inactive.getEmail(), Role.SERVICE, null, false);
      users.save(inactive);
    }
    List<Campaign> portfolio = List.of(
        campaign(aurora,"Vestibular 2027","Google Ads"), campaign(aurora,"Pós-graduação Setembro","Meta Ads"),
        campaign(horizonte,"Solar para Empresas","LinkedIn Ads"), campaign(horizonte,"Energia Residencial","Google Ads"),
        campaign(viva,"Consultas Preventivas","Meta Ads"), campaign(viva,"Check-up Executivo","Google Ads"));
    Campaign old = campaign(archived,"Campanha arquivada (demo)","Meta Ads");
    if (old.isActive()) { old.setActive(false); campaigns.save(old); }
    DemandType budget=type("Ajuste de orçamento",true,"justificativa");
    DemandType report=type("Relatório de desempenho",false,"periodo");
    DemandType ad=type("Alteração de anúncio",true,"acao");
    DemandType creative=type("Aprovação de criativo",true,"acao");
    DemandType audience=type("Ajuste de público e segmentação",true,"justificativa");
    DemandType analysis=type("Análise de métricas",false,"periodo");
    rule("Regra padrão",SlaRule.Scope.DEFAULT,null,null);
    rule("SLA Aurora (demo)",SlaRule.Scope.CLIENT,aurora,null);
    rule("SLA relatórios (demo)",SlaRule.Scope.DEMAND_TYPE,null,report);
    rule("SLA Horizonte e orçamento (demo)",SlaRule.Scope.CLIENT_AND_DEMAND_TYPE,horizonte,budget);

    for (int i=0; i<SUBJECTS.length; i++) {
      TicketStatus target=TicketStatus.values()[i%8];
      int organization=i%3;
      Client client=organizations.get(organization);
      Campaign campaign=portfolio.get(organization*2+(i/3)%2);
      DemandType type = switch (target) {
        case VALIDATION -> creative;
        case DONE -> i==13 ? budget : report;
        case REOPENED -> i==6 ? audience : analysis;
        default -> List.of(budget,report,ad,creative,audience,analysis).get(i%6);
      };
      AppUser requester=requesters.get(organization);
      AppUser author=i%2==0 ? requester : service;
      boolean pending=i==8 || target==TicketStatus.CANCELLED;
      Ticket ticket=new Ticket(numbers.next(),client,pending?null:campaign,pending?"Campanha informada pelo cliente (demo)":null,
          type,campaign.getChannel(),SUBJECTS[i],"[DEMO] Cenário fictício para validar o protótipo. " + SUBJECTS[i]
          + ". Conferir responsável, permissões, histórico e evidências antes de realizar a próxima ação.",
          Priority.values()[(i+i/8)%4],LocalDate.now().plusDays(i%5+1),requester,author,metrics(i));
      ticket=tickets.saveAndFlush(ticket);
      Instant created=Instant.now().minus(Duration.ofDays(i<8?0:i<16?2:12)).minus(Duration.ofHours(i%3));
      jdbc.update("UPDATE tickets SET created_at = ? WHERE id = ?",Timestamp.from(created),ticket.getId().toString());
      entities.refresh(ticket);
      event(ticket,author,"TICKET_CREATED",null,TicketStatus.OPEN,"Solicitação fictícia registrada");
      comments.save(new TicketComment(ticket,requester,"Dados de contexto enviados. Período e valores são fictícios para validação.",false));
      attachment(ticket,requester,TicketAttachment.Kind.ATTACHMENT);
      buildScenario(ticket,target,service,managers.get(i%2),i);
      tickets.saveAndFlush(ticket);
      if ((target==TicketStatus.EXECUTION || target==TicketStatus.VALIDATION)
          && ticket.getResolutionDueAt().isBefore(Instant.now())) {
        jdbc.update("UPDATE tickets SET sla_state = 'OVERDUE' WHERE id = ?",ticket.getId().toString());
        entities.refresh(ticket);
      }
      notify(ticket,requester,i%2==0);
      notify(ticket,service,false);
      notify(ticket,secondService,i%3==0);
      notify(ticket,admin,false);
      if (ticket.getAssignee()!=null) notify(ticket,ticket.getAssignee(),i%2==0);
    }
    tickets.flush();
    jdbc.update("INSERT INTO demo_seed_runs (seed_key, applied_at) VALUES (?, CURRENT_TIMESTAMP(6))",SEED);
    log.info("Carga demo aplicada: 24 tickets, 3 organizações ativas, cenários dos 8 estados e arquivos de validação");
  }

  boolean alreadyLoaded() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM demo_seed_runs WHERE seed_key = ?",Long.class,SEED)>0;
  }

  void buildScenario(Ticket ticket, TicketStatus target, AppUser service, AppUser manager, int scenario) {
    if (target==TicketStatus.OPEN) return;
    ticket.startTriage();
    event(ticket,service,"TRIAGE_STARTED",TicketStatus.OPEN,TicketStatus.TRIAGE,"Atendimento iniciou a triagem");
    if (target==TicketStatus.CANCELLED) {
      ticket.cancel();
      event(ticket,service,"CANCELLED",TicketStatus.TRIAGE,TicketStatus.CANCELLED,"Solicitante informou cancelamento do evento ou duplicidade");
      return;
    }
    if (target==TicketStatus.TRIAGE || (target==TicketStatus.WAITING_FOR_CLIENT&&scenario==3)) {
      if (target==TicketStatus.WAITING_FOR_CLIENT) waitForClient(ticket,service,WaitOrigin.TRIAGE);
      return;
    }
    SlaService.Calculation deadlines=sla.calculate(ticket,ticket.getUrgency());
    ticket.classify(ticket.getUrgency(),manager,deadlines.snapshot(),deadlines.responseDueAt(),deadlines.resolutionDueAt());
    comments.save(new TicketComment(ticket,service,"Demanda classificada; responsável e prazo informados ao solicitante.",true));
    ticket.markEffectiveResponse();
    event(ticket,service,"TRIAGE_CLASSIFIED",TicketStatus.TRIAGE,TicketStatus.TRIAGE,"Prioridade, SLA e responsável confirmados");
    ticket.sendToExecution();
    event(ticket,service,"SENT_TO_EXECUTION",TicketStatus.TRIAGE,TicketStatus.EXECUTION,"Triagem concluída");
    if (target==TicketStatus.EXECUTION) return;
    if (target==TicketStatus.WAITING_FOR_CLIENT) {
      waitForClient(ticket,manager,WaitOrigin.EXECUTION);
      if (scenario==19) {
        ticket.receiveComplement();
        comments.save(new TicketComment(ticket,ticket.getRequester(),"URL final e orçamento confirmados. Aguardando conferência do Atendimento.",false));
        event(ticket,ticket.getRequester(),"COMPLEMENT_RECEIVED",TicketStatus.WAITING_FOR_CLIENT,TicketStatus.WAITING_FOR_CLIENT,"Complemento enviado; retomada pendente");
      }
      return;
    }
    attachment(ticket,manager,TicketAttachment.Kind.EVIDENCE);
    comments.save(new TicketComment(ticket,manager,"Execução demonstrativa registrada. Confira o arquivo de evidência; nenhuma plataforma de anúncios foi alterada.",false));
    boolean approval=ticket.getDemandType().isApprovalRequired();
    ticket.recordExecution(approval);
    event(ticket,manager,"EXECUTION_RECORDED",TicketStatus.EXECUTION,ticket.getStatus(),"Material e ação executada registrados para conferência");
    if (target==TicketStatus.VALIDATION) return;
    if (target==TicketStatus.REOPENED&&scenario==6) {
      ticket.requestCorrection();
      event(ticket,ticket.getRequester(),"CORRECTION_REQUESTED",TicketStatus.VALIDATION,TicketStatus.REOPENED,"Cliente solicitou ajuste da região; mesmo ciclo de resolução");
      return;
    }
    if (approval) {
      ticket.approve();
      event(ticket,ticket.getRequester(),"APPROVED",TicketStatus.VALIDATION,TicketStatus.DONE,"Cliente aprovou a entrega demonstrativa");
    }
    if (target==TicketStatus.REOPENED) {
      ticket.reopenFromDone();
      event(ticket,ticket.getRequester(),"REOPENED",TicketStatus.DONE,TicketStatus.REOPENED,"Divergência identificada após conclusão; novo ciclo de resolução");
    }
  }

  private void waitForClient(Ticket ticket, AppUser actor, WaitOrigin origin) {
    TicketStatus previous=ticket.getStatus();
    ticket.waitForClient(origin,"Confirmar URL final e orçamento autorizado para prosseguir");
    comments.save(new TicketComment(ticket,actor,ticket.getWaitReason(),false));
    event(ticket,actor,"COMPLEMENT_REQUESTED",previous,TicketStatus.WAITING_FOR_CLIENT,ticket.getWaitReason());
  }

  private void event(Ticket ticket, AppUser actor, String action, TicketStatus old, TicketStatus current, String reason) {
    history.save(new TicketHistory(ticket,actor,action,"status",old==null?null:"\""+old.name()+"\"","\""+current.name()+"\"",reason));
  }

  private void notify(Ticket ticket, AppUser recipient, boolean read) {
    Notification notification=new Notification(recipient,ticket,"DEMO_"+ticket.getStatus().name(),
        ticket.getNumber()+" — "+ticket.getSubject()+" (cenário demonstrativo)");
    if (read) notification.markRead();
    notifications.save(notification);
  }

  private void attachment(Ticket ticket, AppUser author, TicketAttachment.Kind kind) {
    String key="demo/"+ticket.getId()+"/"+kind.name().toLowerCase()+".csv";
    Path file=storage.resolve(key).normalize();
    if (!file.startsWith(storage)) throw new IllegalStateException("Caminho demo inválido");
    String contents="ticket,tipo,periodo,impressoes,ctr_percentual,cpc_brl,conversoes,cpa_brl,roas\n"
        +ticket.getNumber()+","+kind.name()+",ultimos 7 dias,12000,2.4,1.80,48,9.00,3.2\n";
    try {
      Files.createDirectories(file.getParent());
      Files.writeString(file,contents,StandardCharsets.UTF_8);
    } catch (IOException exception) { throw new IllegalStateException("Não foi possível criar o arquivo demonstrativo",exception); }
    attachments.save(new TicketAttachment(ticket,author,kind==TicketAttachment.Kind.EVIDENCE?"evidencia-demonstrativa.csv":"contexto-campanha.csv",
        key,"text/csv",contents.getBytes(StandardCharsets.UTF_8).length,kind,
        kind==TicketAttachment.Kind.EVIDENCE?"Registro fictício de execução para validação do protótipo":null,null));
  }

  private String metrics(int i) {
    return "{\"period\":\"Últimos 7 dias (fictício)\",\"impressions\":"+(i==0?0:12000+i*500)
        +",\"ctr\":2.4,\"cpc\":1.8,\"conversions\":48,\"cpa\":9.0,\"roas\":3.2,\"fields\":{\"justificativa\":\"Ajustar distribuição de verba\",\"periodo\":\"Últimos 7 dias\",\"acao\":\"Revisar material da campanha\"}}";
  }

  private Client client(String name, String contact, String email) {
    return clients.findAll().stream().filter(item->item.getEmail().equalsIgnoreCase(email)).findFirst()
        .orElseGet(()->clients.save(new Client(name,contact,email,"(31) 98888-1020")));
  }

  private AppUser user(String name, String email, Role role, Client client, String hash) {
    Optional<AppUser> existing=users.findByEmailIgnoreCase(email);
    if (existing.isPresent()) {
      AppUser user=existing.get();
      if (user.getRole()!=role || (client!=null&&(user.getClient()==null||!client.getId().equals(user.getClient().getId())))) {
        throw new IllegalStateException("Conta existente incompatível com a carga demo: "+email);
      }
      return user;
    }
    return users.save(new AppUser(name,email,hash,role,client));
  }

  private Campaign campaign(Client client, String name, String channel) {
    return campaigns.findAll().stream().filter(item->item.getClient().getId().equals(client.getId())&&item.getName().equals(name)).findFirst()
        .orElseGet(()->campaigns.save(new Campaign(client,name,channel,"Geração de leads (demonstração)")));
  }

  private DemandType type(String name, boolean approval, String field) {
    return demandTypes.findAll().stream().filter(item->item.getName().equals(name)).findFirst()
        .orElseGet(()->demandTypes.save(new DemandType(name,approval,true,"[{\"name\":\""+field+"\",\"required\":true}]")));
  }

  private void rule(String name, SlaRule.Scope scope, Client client, DemandType type) {
    if (slaRules.findAll().stream().noneMatch(item->item.getName().equals(name))) {
      slaRules.save(new SlaRule(name,scope,client,type,DEADLINES));
    }
  }
}
