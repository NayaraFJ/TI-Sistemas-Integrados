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
