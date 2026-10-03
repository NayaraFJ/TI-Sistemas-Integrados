package br.pucminas.sige.demo;

import br.pucminas.sige.campaigns.domain.*;
import br.pucminas.sige.clients.domain.*;
import br.pucminas.sige.demandtypes.domain.*;
import br.pucminas.sige.shared.domain.*;
import br.pucminas.sige.sla.domain.*;
import br.pucminas.sige.tickets.domain.*;
import br.pucminas.sige.users.domain.*;
import java.time.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

@Component @Profile("demo")
public class DemoDataInitializer implements ApplicationRunner {
  private final AppUserRepository users; private final ClientRepository clients; private final CampaignRepository campaigns; private final DemandTypeRepository demandTypes; private final SlaRuleRepository slaRules; private final TicketRepository tickets; private final PasswordEncoder passwords; private final String demoPassword; private final JdbcTemplate jdbc;
  public DemoDataInitializer(AppUserRepository users,ClientRepository clients,CampaignRepository campaigns,DemandTypeRepository demandTypes,SlaRuleRepository slaRules,TicketRepository tickets,PasswordEncoder passwords,JdbcTemplate jdbc,@Value("${sige.demo.password}") String demoPassword){this.users=users;this.clients=clients;this.campaigns=campaigns;this.demandTypes=demandTypes;this.slaRules=slaRules;this.tickets=tickets;this.passwords=passwords;this.jdbc=jdbc;this.demoPassword=demoPassword;}
  @Override @Transactional public void run(ApplicationArguments args) {
    if(users.count()>0)return;
    Client aurora=clients.save(new Client("Aurora Educação","Marina Costa","marketing@aurora.demo","(31) 98888-1020")); Client horizonte=clients.save(new Client("Horizonte Solar","Rafael Lima","midia@horizonte.demo","(31) 97777-2080"));
    String password=passwords.encode(demoPassword); AppUser marina=users.save(new AppUser("Marina Costa","cliente@aurora.demo",password,Role.CLIENT,aurora)); AppUser rafael=users.save(new AppUser("Rafael Lima","rafael@horizonte.demo",password,Role.CLIENT,horizonte)); AppUser atendimento=users.save(new AppUser("Beatriz Souza","atendimento@sige.demo",password,Role.SERVICE,null)); AppUser gestor=users.save(new AppUser("Lucas Martins","gestor@sige.demo",password,Role.TRAFFIC_MANAGER,null)); users.save(new AppUser("Nayara Rodrigues","admin@sige.demo",password,Role.ADMIN,null));
    Campaign vestibular=campaigns.save(new Campaign(aurora,"Vestibular 2027","Google Ads","Geração de leads")); Campaign pos=campaigns.save(new Campaign(aurora,"Pós-graduação Setembro","Meta Ads","Conversão")); Campaign solar=campaigns.save(new Campaign(horizonte,"Solar para Empresas","LinkedIn Ads","Leads B2B"));
    DemandType budget=demandTypes.save(new DemandType("Ajuste de orçamento",true,true,"[{\"name\":\"justificativa\",\"required\":true}]")); DemandType report=demandTypes.save(new DemandType("Relatório de desempenho",false,true,"[{\"name\":\"periodo\",\"required\":true}]")); DemandType ad=demandTypes.save(new DemandType("Alteração de anúncio",true,true,"[{\"name\":\"acao\",\"required\":true}]"));
    slaRules.save(new SlaRule("Regra padrão",SlaRule.Scope.DEFAULT,null,null,"{\"URGENT\":{\"responseHours\":2,\"resolutionHours\":8},\"HIGH\":{\"responseHours\":4,\"resolutionHours\":16},\"MEDIUM\":{\"responseHours\":8,\"resolutionHours\":24},\"LOW\":{\"responseHours\":16,\"resolutionHours\":40}}"));
    Ticket open=create("SIGE-1000",aurora,vestibular,budget,"Aumentar orçamento no fim de semana",Priority.HIGH,marina,marina); tickets.save(open);
    Ticket triage=create("SIGE-1001",horizonte,solar,report,"Relatório semanal de captação",Priority.MEDIUM,rafael,atendimento); triage.startTriage(); tickets.save(triage);
    Ticket execution=create("SIGE-1002",aurora,pos,ad,"Pausar anúncio com oferta encerrada",Priority.URGENT,marina,marina); prepareExecution(execution,gestor); tickets.save(execution);
    Ticket waiting=create("SIGE-1003",horizonte,solar,ad,"Confirmar URL da página de destino",Priority.MEDIUM,rafael,rafael); prepareExecution(waiting,gestor); waiting.waitForClient(WaitOrigin.EXECUTION,"Enviar URL final"); tickets.save(waiting);
    Ticket validation=create("SIGE-1004",aurora,pos,budget,"Aprovar criativo da campanha de pós",Priority.HIGH,marina,atendimento); prepareExecution(validation,gestor); validation.recordExecution(true); tickets.save(validation);
    Ticket done=create("SIGE-1005",horizonte,solar,report,"Análise de queda no CTR",Priority.LOW,rafael,rafael); prepareExecution(done,gestor); done.recordExecution(false); tickets.save(done);
    Ticket reopened=create("SIGE-1006",aurora,vestibular,ad,"Revisar segmentação da campanha",Priority.HIGH,marina,marina); prepareExecution(reopened,gestor); reopened.recordExecution(true); reopened.requestCorrection(); tickets.save(reopened);
    Ticket cancelled=create("SIGE-1007",horizonte,null,ad,"Campanha de evento cancelada",Priority.MEDIUM,rafael,rafael); cancelled.startTriage(); cancelled.cancel(); tickets.save(cancelled);
    jdbc.update("UPDATE ticket_sequences SET next_value = 1008 WHERE name = 'SIGE'");
  }
  private Ticket create(String number,Client client,Campaign campaign,DemandType type,String subject,Priority urgency,AppUser requester,AppUser author){return new Ticket(number,client,campaign,campaign==null?"Feira Solar Minas":null,type,campaign==null?"Meta Ads":campaign.getChannel(),subject,"Dados fictícios criados pelo perfil de desenvolvimento.",urgency,LocalDate.now().plusDays(2),requester,author,"{}");}
  private void prepareExecution(Ticket ticket,AppUser assignee){ticket.startTriage();ticket.classify(ticket.getUrgency(),assignee,"{\"name\":\"Regra padrão\"}",Instant.now().plus(Duration.ofHours(4)),Instant.now().plus(Duration.ofHours(16)));ticket.sendToExecution();}
}
