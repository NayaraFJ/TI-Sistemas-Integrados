# SIGE Desk — Frontend

Interface React do SIGE Desk. Antes de usar a aplicação, inicie o backend conforme o [README do backend](../Backend/README.md).

## Pré-requisitos

- Node.js 22 ou superior.
- pnpm instalado e disponível no terminal (`pnpm --version`).

## Instalação e execução

Abra o terminal na pasta `SIGE-Desk/Frontend`.

Na primeira execução, instale as dependências:

```powershell
pnpm install
```

Para iniciar o frontend:

```powershell
pnpm dev
```

Acesse [http://localhost:5173](http://localhost:5173). Nas próximas execuções, basta usar `pnpm dev`; repita `pnpm install` quando as dependências ou o arquivo `pnpm-lock.yaml` forem atualizados.

A porta é fixa (`strictPort: true`): se `5173` estiver ocupada, o Vite informa o conflito e encerra, em vez de iniciar silenciosamente em `5174`. Encerre a instância anterior do frontend e execute `pnpm dev` novamente. A origem local padrão permitida pela API é `http://localhost:5173`; acessar outra porta pode resultar em HTTP 403 (`Invalid CORS request`) mesmo com credenciais corretas.

## Kanban

O Kanban utiliza [`@caldwell619/react-kanban`](https://github.com/christopher-caldwell/react-kanban), com licença MIT, e `@hello-pangea/dnd`. Ambos são instalados por `pnpm install`. O quadro apresenta os tickets retornados pelo backend, respeitando os filtros e a página atual. Clique em um cartão para abrir o ticket; alterações de status são feitas pelas ações do detalhe, com as validações do backend. A movimentação livre entre colunas está desativada porque algumas transições exigem triagem, justificativa, evidência ou aprovação.

## Conexão com o backend

O arquivo [vite.config.ts](vite.config.ts) configura a porta `5173` e encaminha as chamadas `/api` e `/actuator` para `http://localhost:8080`. Mantenha o backend em execução em outro terminal. Se mudar a porta ou o endereço do backend, defina `SIGE_API_TARGET` antes de iniciar o Vite (por exemplo, `$env:SIGE_API_TARGET = 'http://localhost:8081'`) e reinicie `pnpm dev`.

## Primeiro acesso

No ambiente local padrão, use:

| Campo | Valor |
| --- | --- |
| E-mail do administrador | `admin@sige.demo` |
| Senha | `123` |

Essa conta é cadastrada pelo backend quando a tabela de usuários está vazia. Se uma senha diferente foi configurada na primeira inicialização, use essa senha; reiniciar a aplicação não redefine senhas existentes.

## Build

Para gerar os arquivos de produção em `dist/`:

```powershell
pnpm build
```

O proxy descrito acima pertence ao servidor de desenvolvimento do Vite. Na publicação, configure o servidor de hospedagem para encaminhar `/api` ao backend.

## Visualização das listagens

As abas de clientes, campanhas, tipos de demanda, SLA e usuários usam uma lista compacta com busca, edição e controle de ativação. Os nomes das organizações e os resumos das regras substituem IDs e JSON na listagem. Os formulários de tipos e SLA possuem controles para campos obrigatórios, dias, feriados e prazos.

Em Tickets, alterne entre Kanban e tabela. Os filtros principais ficam visíveis; **Mais filtros** mostra campanha, tipo, prioridade oficial, responsável e período. Recolher os campos preserva os filtros aplicados. A paginação permite 10, 25 ou 50 itens, e a exportação CSV considera todos os resultados dos filtros, independentemente da página exibida.

Notificações permite consultar todas ou apenas não lidas, marcar uma notificação e marcar todas como lidas. Os indicadores e gráficos do painel e dos relatórios vêm da API. Os dados de demonstração do backend são persistidos no MySQL.

## Tutorial, tags e gráficos

**Ver tutorial** inicia um guia da página atual: destaca os controles na própria tela, rola até a área explicada e apresenta progresso, Anterior, Próximo e Concluir. As setas do teclado navegam pelas etapas e Esc fecha o guia. O conteúdo considera o perfil e os controles presentes; no celular, o acesso fica dentro de Menu. O guia não envia formulários nem executa ações de negócio.

As tags seguem o tema compartilhado: altura de 24 px, texto de 12 px com peso 500, cantos de 6 px e fundo suave sem contorno. Status e SLA usam espaçamento de 8 px e podem quebrar linha sem encostar. Tags de cadastro, notificações, contadores do Kanban e anexos usam a mesma base.

Os gráficos usam a versão Community de MUI X Charts já instalada. Barras horizontais com eixo de quantidade substituem as linhas de progresso do relatório; a rosca mostra quantidades e percentuais e permite destacar um status pelo gráfico ou pela legenda, inclusive com teclado. As cores e a ordem dos status são compartilhadas com as tags. As animações respeitam `prefers-reduced-motion`; o estado vazio informa a ausência de tickets.

Validação e capturas locais: [registro de interface](../docs/ui/2026-11-20/README.md). Testes do frontend: `node --experimental-strip-types --test tests/*.test.ts`.

## Campos, concorrência e contrato

Tipos de demanda permitem texto, número, data, seleção e anexo, com limites e obrigatoriedade condicional. A abertura envia arquivos e ticket juntos; uma falha mantém o formulário e não cria um ticket parcial. No detalhe, os rótulos dos campos seguem o snapshot da demanda.

Atendimento/Admin podem reatribuir tickets ativos com motivo. Formulários guardam a versão aberta: se o ticket mudar, a API rejeita a alteração e a interface pede fechar/reabrir a ação para revisar os dados atuais. O filtro **Somente SLA vencido** funciona na lista, no Kanban e nos relatórios. Cadastros e notificações paginam no servidor.

Para atualizar contrato e tipos após mudar DTOs do backend:

```powershell
pnpm api:sync http://localhost:8080/v3/api-docs
pnpm build
```

Sem servidor, `pnpm api:sync` gera tipos a partir do contrato versionado. Detalhes em [docs/openapi.md](../docs/openapi.md).
