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

## Conexão com o backend

O arquivo [vite.config.ts](vite.config.ts) configura a porta `5173` e encaminha as chamadas `/api` e `/actuator` para `http://localhost:8080`. Mantenha o backend em execução em outro terminal. Se mudar a porta ou o endereço do backend, ajuste os dois destinos em `server.proxy` e reinicie `pnpm dev`.

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
