# Publicação acadêmica na AWS

O pacote em `deploy/aws/` prepara uma única EC2 para executar o frontend React, a API Spring Boot e o MySQL. Todos os recursos regionais ficam em `us-east-2`, usando o perfil local `sige-desk`.

Esta configuração foi preparada para uma demonstração de até dois meses, com uso intermitente. Preparar o pacote e validar o template não publica a aplicação. A publicação exige criar a infraestrutura, enviar o pacote e verificar o sistema em execução.

## Plano e estimativa

A consulta de 17/11/2026 retornou plano `FREE`, estado `ACTIVE` e US$ 100 em créditos disponíveis. Isso é um retrato do projeto naquele momento; o saldo deve ser consultado novamente antes da criação dos recursos.

Os preços consultados na API oficial para Ohio foram US$ 0,0208/h pela `t3.small` Linux, US$ 0,005/h por um IPv4 público e US$ 0,08/GiB-mês pelo disco gp3. O cálculo de referência usa 60 dias e dois meses de armazenamento:

| Uso da máquina | Processamento | IP fixo | Disco de 20 GiB | Total base em créditos |
| --- | ---: | ---: | ---: | ---: |
| Ligada continuamente | US$ 29,95 | US$ 7,20 | US$ 3,20 | US$ 40,35 |
| Ligada 4 horas por dia | US$ 4,99 | US$ 7,20 | US$ 3,20 | US$ 15,39 |

O total não inclui eventual tráfego excedente, operações e armazenamento do pequeno pacote S3, snapshots, impostos ou outros recursos. O template usa créditos de CPU em modo `standard`, evitando cobranças por CPU excedente do modo `unlimited`. A implantação não exige mudar para o plano pago.

O IP fixo permanece reservado quando a máquina está parada e continua sendo cobrado. Ele mantém o mesmo endereço após parar e ligar; o certificado e o domínio não precisam ser refeitos a cada uso.

## Recursos e acesso

- EC2 `t3.small`: 2 vCPU, 2 GiB de RAM, Amazon Linux 2023, monitoramento básico e IMDSv2 obrigatório.
- Disco gp3 criptografado de 20 GiB, com volumes Docker para MySQL, anexos e certificados.
- Elastic IP e hostname `sige-desk.<IP>.sslip.io`, resolvido pelo serviço externo sslip.io. O Caddy obtém e renova o certificado HTTPS automaticamente enquanto o domínio resolver para a instância e as portas de validação estiverem acessíveis.
- Grupo de segurança com entrada TCP somente em 80 e 443. MySQL e API não publicam suas portas na máquina.
- Administração pelo Systems Manager Session Manager, sem chave SSH. A role da máquina permite gerenciamento SSM e leitura somente dos objetos `releases/*` do bucket da aplicação.
- Bucket S3 privado e criptografado para os pacotes, com expiração dos artefatos após sete dias. Ele não guarda o banco, os anexos ou as senhas.

O frontend e `/api` compartilham a mesma origem HTTPS. O Caddy preserva o prefixo da API e atende rotas React diretamente, incluindo recarga de `/tickets`. O perfil `production` exige configuração explícita de conexão e senhas e habilita cookies de sessão `Secure`.

## Gerar o pacote no Windows

Os pré-requisitos são Node.js 22 ou superior, Java 21 ou superior, PowerShell e `tar`. Na raiz `SIGE-Desk`, execute:

```powershell
.\deploy\aws\package.ps1
```

O script instala as dependências com pnpm 10.24.0 e o lockfile versionado, compila o front e executa `mvnw verify` no backend. Depois cria um `.tar.gz` em `deploy/aws/artifacts/`, com hash SHA-256. O pacote contém o JAR, o front compilado e os arquivos necessários ao Docker; não inclui `.env`, uploads, banco ou credenciais AWS.

`-UseExistingBuild` empacota os builds existentes sem recompilar ou executar testes. Use essa opção somente depois de verificar que os builds correspondem aos fontes atuais.

## Criar a infraestrutura e publicar

Antes de criar a stack, confira o plano e os créditos e revise `deploy/aws/cloudformation.yaml` e `deploy/aws/deployment-plan.local.json`. O plano local contém os IDs de rede descobertos neste projeto e é ignorado pelo Git. O template cria recursos de hospedagem com custo de uso; valide a estimativa antes de executá-lo.

1. Crie uma stack CloudFormation em `us-east-2` com o template, os parâmetros `VpcId` e `SubnetId` do plano local e a capacidade `CAPABILITY_IAM`.
2. Aguarde `CREATE_COMPLETE`. Os outputs fornecem `InstanceId`, `ArtifactBucket`, `ApplicationUrl` e o endereço do Session Manager. A conclusão da stack não comprova a conclusão do bootstrap: confirme que a máquina está registrada no SSM e que `docker compose version` funciona.
3. Envie o arquivo `.tar.gz` para `releases/<nome-do-arquivo>` no bucket privado. Use upload assinado; mantenha o bucket privado.
4. Pelo Session Manager, baixe esse objeto na máquina com as permissões da instance role, confira seu SHA-256 e extraia-o em `/opt/sige-desk`. Não envie credenciais AWS locais para a máquina.
5. Inicialize a configuração e os serviços:

```bash
sudo -i
cd /opt/sige-desk
bash deploy/aws/init-env.sh "$(cat deployment-domain)"
bash deploy/aws/up.sh
```

`init-env.sh` gera senhas aleatórias e grava `deploy/aws/.env` com permissão `600`; se o arquivo existir, preserva a configuração. O arquivo real é ignorado pelo Git e fica fora do pacote de publicação. `up.sh` rejeita os valores de exemplo antes de iniciar os serviços.

O perfil inicial é `production,demo`, com os dados fictícios de validação. Como a carga demo também cria o administrador, o gerador usa a mesma senha inicial em `SIGE_ADMIN_PASSWORD` e `SIGE_DEMO_PASSWORD`. O login administrativo é `admin@sige.demo`. Consulte a senha no `.env` por uma sessão SSM autorizada, sem copiá-la para logs públicos. Retirar o perfil `demo` depois não apaga os registros já persistidos.

Para atualizar a versão, gere outro pacote, confirme seu hash, extraia-o no mesmo diretório e execute `up.sh`. Preserve `.env` e os volumes. O Flyway aplica as novas migrations; voltar somente o JAR não desfaz alterações de banco.

## Parar e ligar para economizar

No console EC2, selecione a instância `sige-desk-academico` em `us-east-2` e use **Estado da instância > Parar instância**. A cobrança de processamento para; disco e Elastic IP continuam sendo cobrados. **Encerrar instância** remove a máquina e seu disco nesta configuração e não é a operação para pausar o trabalho.

Para usar novamente, selecione **Iniciar instância**, aguarde os status checks e teste a URL da aplicação. O Docker inicia com o sistema e os contêineres usam `restart: unless-stopped`. Esse retorno automático pressupõe que os contêineres estavam ativos antes da parada da EC2; um contêiner parado manualmente por `docker stop` precisa ser iniciado novamente.

Com a máquina parada, front, API, downloads e tarefas de notificação ficam indisponíveis. Os dados nos volumes EBS/Docker permanecem. O relógio dos prazos continua avançando; o job de vencimentos retoma quando a API volta. Mantenha a máquina ligada durante o período em que o professor precisar avaliar o sistema.

## Verificar a publicação

Confira HTTPS válido, redirecionamento HTTP para HTTPS, `/healthz` retornando `UP`, carregamento e recarga de rotas React, login, sessão em navegação, logout, consulta de tickets e upload/download de anexos. Confirme que 22, 3306 e 8080 não estão liberadas no grupo de segurança.

Antes de considerar a publicação concluída, pare e ligue a EC2 e repita os checks de saúde, login e download para comprovar persistência e retorno dos serviços. A criação do template e os testes Java não substituem essa verificação.

## Encerramento após a avaliação

Exporte o banco, os anexos e qualquer evidência que queira guardar antes de excluir os recursos. A exclusão da stack termina a EC2 e remove seu disco, libera o Elastic IP e remove role, profile e grupo de segurança. O bucket precisa estar vazio para ser excluído; os artefatos podem ser removidos depois de confirmar que não serão mais usados. Parar a EC2, por si só, não elimina os custos fixos.

## Referências

- [Estados e cobrança da EC2](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-lifecycle.html).
- [Custos de recursos parados](https://docs.aws.amazon.com/cost-management/latest/userguide/coh-stop-idle-resources.html).
- [Padrões oficiais Caddy para SPA e proxy](https://caddyserver.com/docs/caddyfile/patterns).
- [Instalação do Docker Compose no Linux](https://docs.docker.com/compose/install/linux/).
- [AWS Free Tier](https://aws.amazon.com/free/).
