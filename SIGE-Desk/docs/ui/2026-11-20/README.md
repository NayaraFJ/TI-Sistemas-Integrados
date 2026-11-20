# Revisão de interface — 20/11/2026

Escopo local: tutorial contextual, padronização das tags e apresentação dos gráficos. Estes registros complementam a manutenção da interface; não representam nova execução integral dos 17 casos do catálogo acadêmico nem avaliação de usabilidade com participantes.

## Implementação

O tutorial usa React e MUI, sem dependência adicional. Seletores `data-tour` identificam áreas da página. Somente alvos presentes e com dimensão visível entram no guia; a rolagem e as medidas são atualizadas por etapa, redimensionamento e rolagem. A janela mantém o foco, permite sair com Esc e devolve o foco ao acionador; no celular, retorna a Menu. Trocar de rota fecha o guia. As instruções ficam no i18n, e a seleção de etapas considera rota e perfil.

As tags compartilham `MuiChip` no tema: 24 px de altura, texto de 12 px/peso 500, raio de 6 px, preenchimento lateral de 8 px e ausência de contorno. Status usa sua cor sem ponto decorativo; alerta de SLA usa fundo vermelho suave. Grupos de status/SLA têm intervalo de 8 px, alinhamento central e quebra de linha.

Os gráficos comparam volume por status (barras horizontais) e participação no total (rosca). React controla os dados e o destaque temporário; MUI X Charts controla SVG, escalas, tooltip e animações. São até dois gráficos simultâneos no relatório e um no painel, com no máximo oito categorias. Permanecem os dados, filtros e contratos existentes, sem persistência adicional do destaque ou chamadas de escrita. Eixo iniciado em zero, contagens visíveis e cores/ordem compartilhadas evitam mudanças no significado dos dados. A legenda mantém contagem e percentual acessíveis sem depender do hover. A preferência de movimento reduzido desativa animações. A apresentação vazia substitui os gráficos quando o total é zero.

## Verificação realizada

- TypeScript e build de produção aprovados.
- Nove testes Node aprovados, incluindo cinco novos testes de seleção de etapas por perfil/rota e posicionamento do cartão em desktop e celular.
- Navegador local, perfil Administrador: guia do painel com seis etapas; guia de Relatórios com cinco etapas até Concluir; guia de Tickets com filtros e troca de visualização; guia de Configuração de SLA com três etapas.
- Avançar/voltar por botões e setas, fechamento por Esc, reabertura na primeira etapa e devolução de foco ao acionador conferidos.
- Viewport móvel de 390 × 844: seis etapas do painel mantiveram o cartão dentro da tela; conclusão devolveu o foco a Menu. Gráficos do relatório mantiveram os oito rótulos e largura da página de 390 px, sem transbordamento horizontal do documento.
- Base local de demonstração: oito status com três tickets cada, total 24; legenda mostrou 12,5% por status. Destaque de Em validação apresentou três tickets e 12,5% no centro. Ativação por Enter conferida.
- Busca sem resultados no relatório mostrou ausência de tickets nos dois gráficos e na tabela; Limpar restaurou os dados.
- Estilos computados de Concluída/SLA vencido e dos quatro escopos de SLA confirmaram 24 px, 12 px, peso 500 e ausência de contorno. Em coluna estreita, as tags quebraram com intervalo vertical de 8 px.
- Carregamento limpo e interação por teclado nos gráficos sem novos avisos ou erros no console. Houve mensagens transitórias de React/Emotion durante a troca de dependências pré-processadas pelo Vite em HMR; não reapareceram após recarregar a página.
- Verificação mecânica Impeccable dos componentes visuais alterados: nenhum achado.

Não foram criados ou alterados tickets nesta verificação. A preferência de movimento reduzido está configurada no componente; não foi emulada no navegador. Os demais perfis foram cobertos pela lógica de seleção de etapas, sem nova execução visual integral de cada perfil. Publicação AWS não faz parte desta revisão local.

Após a revisão local, a release `20261120-103310-967c209d` foi publicada em tarefa autorizada pelo usuário. Login e tutorial foram conferidos também no navegador na AWS; o [registro de publicação](../../publicacao-aws.md) documenta o ensaio das oito contas. Isso não representa nova coleta de usabilidade.

## Evidências

- [Tutorial no desktop](tutorial-desktop.jpg)
- [Tutorial no celular](tutorial-mobile.jpg)
- [Tutorial da fila](tutorial-tickets.jpg)
- [Tags na tabela](tags-tabela.jpg)
- [Tags de escopo do SLA](tags-sla.jpg)
- [Gráficos no desktop com destaque](graficos-desktop.jpg)
- [Gráficos no celular](graficos-mobile.jpg)
- [Tutorial na versão publicada na AWS](aws-tutorial-publicado.jpg)
