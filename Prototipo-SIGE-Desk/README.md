# Protótipo SIGE Desk

Aplicação estática navegável que representa as 15 telas definidas em [Prototipo especificação.md](../Trabalho/Documentacao%20do%20Projeto/Prototipo/Prototipo%20especificação.md). Os dados são fictícios e ficam no `localStorage` do navegador.

## Executar

No diretório do protótipo:

```powershell
npm start
```

Abra `http://localhost:4173`. O servidor usa apenas o Node.js, sem dependências externas.

## Contas de demonstração

| Perfil | E-mail | Senha |
| --- | --- | --- |
| Cliente | `cliente@aurora.demo` | `demo123` |
| Atendimento | `atendimento@sige.demo` | `demo123` |
| Gestor de tráfego | `gestor@sige.demo` | `demo123` |
| Administrador | `admin@sige.demo` | `demo123` |

Na autenticação, os botões de acesso rápido preenchem e enviam essas credenciais. Use **Sair** e entre com outra conta para demonstrar a transferência de trabalho entre perfis.

## Tutorial guiado

No primeiro acesso de cada conta, o protótipo inicia um roteiro próprio para o perfil autenticado. O tutorial destaca os controles relevantes, navega entre as telas necessárias e permite avançar, voltar, pular ou concluir sem alterar o estado dos tickets.

Depois de concluído ou pulado, o roteiro não abre automaticamente para aquela conta. A opção **Ver tutorial**, no rodapé do menu lateral, permite iniciá-lo novamente a qualquer momento. Essa preferência fica no `localStorage` do navegador e também é apagada por **Restaurar base**.

## Dados e reinicialização

Alterações realizadas durante a demonstração ficam salvas no navegador. Use **Restaurar base**, na tela de login, para retornar ao conjunto inicial.

## Verificação

```powershell
npm test
```

O teste valida o inventário das 15 telas, os quatro roteiros do tutorial, os perfis de demonstração, a coerência dos relacionamentos mockados e as funções centrais de permissão e transição.
