# CT-14 — preservação no cancelamento pela API

Execução em 19/11/2026, `http://localhost:8081/api/v1`, schema `sige_desk_qa_20261118`, runtime SHA-256 `8e38886b7a655b48fd514648113897ef1ff6d0dd32dd869eec1df16871d261ea`. O [roteiro](../../Backend/scripts/qa_ct14_preservacao.py) criou tipo e tickets sintéticos com marcador exclusivo, confirmou cada ticket no schema e removeu as fixtures criadas. O [JSON](ct14-preservacao-20261119.json) conserva requisições verificadas e detalhes antes/depois. Foram aprovadas 77 verificações do caso, mais a verificação de limpeza.

| Origem | Resultado | Versão antes → depois |
|---|---|---|
| Aberta | HTTP 200; CANCELLED | 0 → 2 |
| Em triagem | HTTP 200; CANCELLED | 2 → 4 |
| Aguardando cliente por triagem | HTTP 200; CANCELLED | 4 → 6 |
| Em execução | HTTP 409; EXECUTION | 5 → 5 |
| Aguardando cliente por execução | HTTP 409; WAITING_FOR_CLIENT | 7 → 7 |

Nos três cancelamentos, histórico anterior, prazos de resposta e resolução, resposta efetiva e ciclo foram preservados; novo histórico identificou motivo e autora. Nas duas negativas, estado, versão e histórico permaneceram iguais e os prazos e ciclo foram preservados. Os prazos nas três origens permitidas eram nulos, pois os tickets ainda não haviam sido classificados; nas negativas de execução eram preenchidos. Assim, o teste não demonstra cancelamento permitido com prazo não nulo.

O contrato `POST /tickets/{id}/cancel` recebe `ReasonRequest` com `reason` e `If-Match`; não existe campo de confirmação nesse contrato. Leituras sucessivas sem enviar comando foram idênticas, apenas controle de inatividade. Isso não demonstra que rejeitar ou fechar uma confirmação na interface deixa de enviar o comando. Motivo, confirmação e consulta somente leitura ainda requerem execução visual; não se infere endpoint de UI nem comprovação de confirmação pelo HTTP.
