# Verificação independente de concorrência e contratos — 19/11/2026

## Conflito simultâneo de comentários

Em base local `sige_desk_qa_20261118` e API `http://localhost:8081/api/v1`, duas sessões Cliente enviaram simultaneamente comentários diferentes para o mesmo ticket sintético, ambas com `If-Match: 0`. O processo foi informado como reiniciado após o pacote `qa-package-final5.log`; SHA-256 do JAR `target/qa-runtime.jar`: `cb773b0da529098588b234ec3068c6bd2ef573c47bd9bf0ef5d5e72ba5c91584`. A fixture exclusiva e resultados estão em [concorrencia-if-match-independente.json](concorrencia-if-match-independente.json). O roteiro está em `Backend/target/qa_concorrencia_independente.py`.

**Esperado:** um 201, um 409, uma alteração e um evento correspondente. **Obtido:** 201 e 500; versão final 1, um comentário e um evento `COMMENT_ADDED`. A preservação do estado foi observada nesta execução, mas a resposta de conflito falhou. Não se atribui causa sem examinar a exceção efetiva. O teste foi executado uma vez e não comprova todas as ações simultâneas.

## Contratos

- CT-08 exige negar acesso e alteração entre escopos e registrar a tentativa negada, sem especificar payload inválido. Como o novo binding/`@Valid` ocorre antes de `verifyVersion`, uma requisição inválida pode falhar antes da política de visibilidade; deve-se testar essa variante e explicitar o alcance pretendido da auditoria. Não foi executada nesta verificação.
- CT-03 exige enviar versão antiga/ausente **quando exigida pelo contrato HTTP** e preservar contra sobrescrita concorrente. O catálogo não especifica literalmente `If-Match` para upload de anexo; não é possível afirmar violação desse requisito apenas pela ausência do cabeçalho na rota de anexos. Upload altera o ticket e grava arquivo fora da transação de banco: concorrência e limpeza após rollback merecem verificação independente.

## Reteste no runtime atualizado

Três corridas independentes com tickets novos, em 19/11/2026, usaram o runtime SHA-256 `10f163768eb01b93dc32891fea4ed71be259bdeabce9c443cfd732d5ca754f94`, informado como reiniciado após `qa-package-final6.log`. Em cada corrida, duas sessões enviaram comentários com `If-Match: 0`: houve um 201 e um 409, versão final 1, um comentário e um evento `COMMENT_ADDED`. As duas sessões venceram ao menos uma corrida. Evidências: [corrida 1](concorrencia-if-match-reteste-1.json), [corrida 2](concorrencia-if-match-reteste-2.json), [corrida 3](concorrencia-if-match-reteste-3.json). O resultado anterior 201/500 está preservado acima. A correção passou nesse recorte de comentários; outras ações concorrentes e interface não foram verificadas por este reteste.
