# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 03 — Referencial / marco teórico

**Tema:** Gestão de Demandas de Marketing Digital  
**Título:** *Help Desk para Gestão de Demandas de Tráfego Pago*

## 1. Revisão bibliográfica

A revisão bibliográfica organiza o conhecimento publicado que permite delimitar um problema de pesquisa, compreender conceitos e interpretar resultados. Sua base metodológica é a pesquisa bibliográfica, desenvolvida a partir de material já elaborado, sobretudo livros e artigos científicos. Esse procedimento pode constituir a principal forma de investigação ou complementar pesquisas que também utilizem outros meios de obtenção de dados (Gil, 1991).

O processo envolve estabelecer objetivos, elaborar um plano, identificar e localizar fontes, selecionar e ler os materiais, registrar as informações pertinentes e redigir uma síntese vinculada ao problema. A leitura exploratória verifica a utilidade da obra; a seletiva delimita os trechos relevantes; a analítica organiza suas ideias; e a interpretativa relaciona essas contribuições à investigação. Essas atividades exigem exame crítico e podem ser retomadas conforme o entendimento do tema evolui (Gil, 1991).

A qualidade da revisão depende da pertinência e da confiabilidade das fontes, da comparação de argumentos e do registro de autoria, edição e localização das informações. Fontes secundárias podem reproduzir erros, e a autoridade de uma publicação não dispensa a análise de suas evidências. No presente trabalho, esses fundamentos orientam a articulação entre estudos sobre atendimento e marketing digital e as bases conceituais necessárias à especificação e à avaliação do sistema (Gil, 1991; Booth, Colomb e Williams, 2008).

## 2. Requisitos do sistema

Os requisitos de um sistema descrevem os serviços que ele deve oferecer, os comportamentos esperados e as restrições de funcionamento, expressando necessidades dos interessados. A engenharia de requisitos abrange descobrir, analisar, documentar e verificar essas necessidades. Requisitos de usuário apresentam serviços e restrições em nível acessível ao público envolvido, enquanto requisitos de sistema detalham o que deve ser implementado (Sommerville, 2011).

Os requisitos funcionais especificam o que o sistema deve fazer, incluindo respostas a entradas e comportamentos em situações determinadas. Os não funcionais estabelecem propriedades e restrições, como desempenho, disponibilidade, segurança e usabilidade, além de exigências organizacionais e externas. A distinção não é absoluta: uma exigência de proteção pode originar funções de autenticação e autorização. No Help Desk proposto, registrar e acompanhar uma solicitação exemplificam funções, enquanto a proteção dos dados, a compatibilidade entre navegadores e a acessibilidade exemplificam propriedades e restrições da solução (Sommerville, 2011).

Requisitos claros permitem reduzir interpretações divergentes e relacionar a especificação aos procedimentos de verificação. Sempre que viável, propriedades como desempenho e facilidade de uso devem ser expressas por condições observáveis, em vez de metas vagas. A ISO/IEC/IEEE 29148:2018 complementa essa orientação ao tratar características como ausência de ambiguidade e verificabilidade e apoiar o registro e a rastreabilidade dos requisitos ao longo do desenvolvimento (Sommerville, 2011; ISO/IEC/IEEE, 2018).

## 3. Backlog

No Scrum, o *Product Backlog* é uma lista ordenada e emergente do que é necessário para melhorar o produto e constitui a fonte de trabalho do Scrum Team. Seu conteúdo evolui com o conhecimento adquirido, e sua ordenação permite orientar as decisões de desenvolvimento em relação à Meta do Produto. O Product Owner responde pelo gerenciamento eficaz dessa lista, incluindo a comunicação dos itens e a garantia de transparência e compreensão (Schwaber e Sutherland, 2020).

O refinamento é uma atividade contínua de desdobramento e detalhamento dos itens, tornando-os menores e mais precisos. O *Sprint Backlog*, por sua vez, reúne a Meta da Sprint, os itens selecionados e o plano de ação para entregar o incremento, sendo atualizado pelos Developers durante o trabalho. Assim, a organização das necessidades do produto e o planejamento de uma Sprint possuem alcances distintos (Schwaber e Sutherland, 2020).

Audy descreve o backlog como instrumento vivo, construído a partir de demandas levantadas com os envolvidos e revisto conforme os pressupostos do produto são examinados. O registro pode utilizar planilhas, ferramentas especializadas ou post-its, e sua elaboração considera prioridade, valor e complexidade. Para o SIGE Desk, esse conceito fundamenta a organização das demandas de desenvolvimento e a seleção do escopo inicial; o uso do backlog, isoladamente, não caracteriza a adoção integral do Scrum (Audy, 2015; Schwaber e Sutherland, 2020).

## 4. Produto mínimo viável

O produto mínimo viável, ou *Minimum Viable Product* (MVP), corresponde à versão mais simples de um produto que pode ser disponibilizada para validar um pequeno conjunto de hipóteses sobre o negócio. Seu propósito é produzir aprendizagem sobre o direcionamento da solução antes de ampliar o investimento. O recorte de funcionalidades deve, portanto, ser suficiente para realizar essa verificação (Caroli, 2015).

A evolução ocorre por incrementos orientados pelos resultados das validações. Hipóteses amplas podem ser decompostas em hipóteses menores, permitindo obter retorno antecipado e corrigir o curso do produto. Uma resposta negativa também é relevante, pois pode mostrar que o investimento em uma solução mais elaborada não seria justificado (Caroli, 2015).

Na proposta do SIGE Desk, a delimitação do MVP concentra-se no fluxo de registro e acompanhamento de demandas, com responsáveis, prazos, comunicação e histórico. A avaliação desse recorte deverá examinar se os usuários compreendem e conseguem percorrer os fluxos previstos. A prototipação poderá apoiar essa aprendizagem, enquanto a verificação das regras implementadas e da operação da aplicação dependerá dos testes correspondentes. Essa aplicação do conceito orienta o planejamento do projeto e não constitui resultado de validação já obtido.

## 5. Testes funcionais e de usabilidade

### 5.1 Testes funcionais

Os testes funcionais procuram identificar discrepâncias entre o comportamento do software e sua especificação externa, considerada do ponto de vista do usuário. Na abordagem de caixa-preta, os casos são derivados das entradas, saídas e condições especificadas, sem utilizar a estrutura interna do programa como base para sua seleção. Cada caso precisa explicitar o resultado esperado e considerar também situações inválidas ou inesperadas (Myers, Sandler e Badgett, 2012).

O particionamento de equivalência organiza as entradas em classes válidas e inválidas para selecionar representantes de comportamentos esperados. A análise de valor limite complementa essa técnica, explorando as fronteiras e os valores imediatamente próximos, tanto no domínio de entrada quanto no de saída. Em um formulário, isso pode orientar testes de campos obrigatórios e limites definidos na especificação, comparando a resposta obtida com a regra prevista (Myers, Sandler e Badgett, 2012; Pressman e Maxim, 2021).

### 5.2 Testes de usabilidade

Os testes de usabilidade examinam a interação de usuários com a interface durante a realização de tarefas. Teixeira destaca a compreensão e a manipulação do produto, enquanto Myers, Sandler e Badgett discutem clareza das mensagens, consistência, retorno das ações, navegação, aprendizagem e satisfação. A observação dessas dimensões complementa a verificação funcional, pois uma operação pode produzir a saída correta e ainda apresentar dificuldades de uso (Teixeira, 2014; Myers, Sandler e Badgett, 2012).

Ao aplicar a ISO 9241-11 à avaliação de uma aplicação de assinatura digital, Radhitya et al. articulam eficácia, eficiência e satisfação em um contexto de uso. A eficácia diz respeito à realização dos objetivos, a eficiência relaciona essa realização aos recursos empregados, como o tempo, e a satisfação considera a percepção dos usuários. A combinação de desempenho nas tarefas e percepção permite distinguir a conclusão de uma atividade das dificuldades encontradas para concluí-la (Radhitya et al., 2024).

A avaliação requer tarefas realistas, participantes compatíveis com o público e instruções consistentes, com registro de conclusão, erros, ajuda e tempo. Entrevistas ou questionários podem complementar a observação; o UEQ-S, por exemplo, oferece uma medida resumida das qualidades pragmática e hedônica da experiência. Essas respostas não substituem os registros de execução das tarefas. O número de participantes e o alcance das conclusões devem considerar o contexto e a complexidade do sistema (Myers, Sandler e Badgett, 2012; Teixeira, 2014; Schrepp, Hinderks e Thomaschewski, 2017).

## 6. Gestão de demandas e sistemas de informação

Amanullah, Zeki e Abubakar (2017) descrevem o sistema de Service Desk como apoio ao controle do fluxo de trabalho mediante coleta, rastreamento e processamento de solicitações de suporte. O estudo apresenta uma aplicação web voltada a estudantes e funcionários de uma instituição de ensino. Fenner et al. (2015) caracterizam o Service Desk como canal de comunicação entre o provedor interno de serviços de TI e seus usuários, pelo qual incidentes e problemas são analisados, encaminhados e resolvidos.

Firmansyah e Subriadi (2022) apresentam o Service Desk como ponto único de contato para receber incidentes e solicitações de serviço. Na revisão de quarenta artigos, os autores identificam benefícios relacionados à documentação, às métricas de processo e à eficiência da comunicação interna, bem como desafios de investimento, resistência organizacional e adaptação dos modelos de gestão às características da organização. Os resultados dizem respeito à gestão de serviços de TI e não constituem evidência de desempenho de um Help Desk em agências de marketing.

O guia ITIL 4 de Service Desk orienta a captura e o registro das interações, a triagem das consultas e a comunicação entre o provedor e os usuários (PEOPLECERT, 2023). O guia de Service Request Management define modelos de solicitação como abordagens previamente estabelecidas e repetíveis para atender determinados tipos de pedido, incluindo procedimentos de iniciação, aprovação e atendimento (AXELOS, 2020). Fenner et al. (2015) também destacam registro dos chamados, tempo de atendimento, espera e acompanhamento dos acordos de nível de serviço (SLA) entre os elementos relevantes à gestão da capacidade de atendimento.

## 7. Marketing digital e tráfego pago

O marketing digital compreende a promoção de produtos e serviços por canais e meios digitais, conforme a discussão conceitual apresentada por Kharisma et al. (2024). Gujar, Paliwal e Panyam (2024) descrevem atividades de geração de potenciais clientes, comunicação por redes sociais, mecanismos de busca e e-mail, gestão de campanhas e análise de resultados. Esses autores situam tais atividades em um processo que articula aquisição de clientes, relacionamento e acompanhamento do desempenho.

O tráfego associado à publicidade paga envolve anúncios cuja veiculação depende de investimento em mídia digital (Agus et al., 2019; Nain et al., 2025). Agus et al. (2019) analisam esse contexto por meio da compra de espaços publicitários e das relações entre anunciantes, agências, publishers digitais e plataformas de troca de anúncios. Nain et al. (2025) descrevem, no caso da busca paga, a relação entre anúncios, consultas dos usuários, palavras-chave e páginas de destino, além do uso de estratégias de lance por clique ou conversão. Assim, a publicidade paga é tratada nessas fontes como uma operação que reúne investimento, veiculação e direcionamento do usuário ao conteúdo anunciado.

Yahia e ElBolok (2024) propõem um modelo de programação não linear para distribuir orçamento entre plataformas, com o objetivo de maximizar impressões líquidas esperadas sob restrições de verba e preferências do anunciante. A aplicação do modelo é ilustrada por dois estudos de caso hipotéticos. Nain et al. (2025), por sua vez, discutem a influência de palavras-chave, segmentação geográfica, relevância dos anúncios e estratégias de lance sobre indicadores de campanhas de busca. Essas pesquisas abordam variáveis e decisões da operação publicitária; não investigam o registro de demandas em sistemas de Help Desk.

## 8. Gestão de projetos em agências de marketing digital

Steponaitis e Andrijauskienė (2023) investigam competências de gestão de projetos digitais em uma agência de marketing da Lituânia. O estudo utiliza revisão de literatura e entrevistas semiestruturadas com quatro gestores de projeto de uma organização de vinte pessoas. Entre as competências destacadas estão conhecimento contextual, planejamento, comunicação e adaptação a um ambiente de mudanças rápidas.

A análise de Steponaitis e Andrijauskienė (2023) também contempla gestão de tempo, monitoramento e controle, gestão de riscos, análise de dados e empatia. Os autores propõem ações de desenvolvimento de competências, como compartilhamento de conhecimento, mentoria e formação profissional. A pesquisa examina competências e percepções dos participantes; não testa a adoção de um Help Desk nem permite generalizar o desempenho de ferramentas de atendimento para outras agências.

Agus et al. (2019) situam as agências de mídia na cadeia de publicidade digital paga da Indonésia, representando interesses dos anunciantes na compra de espaços publicitários. O estudo descreve fluxos de informação, recursos financeiros e serviços, além de assimetrias de poder entre agências, publishers e plataformas de troca de anúncios. A transparência de preços e as mudanças na intermediação constituem resultados dessa análise exploratória, cujo recorte não inclui a publicidade em redes sociais.

## 9. Automação de marketing, gestão do relacionamento com o cliente (CRM) e atendimento ao cliente

Gujar, Paliwal e Panyam (2024) descrevem a automação de marketing de ponta a ponta como integração de atividades que abrangem geração de potenciais clientes, relacionamento, gestão de campanhas e análise de resultados. Os autores discutem o uso de inteligência artificial (IA) e software como serviço (SaaS) para organizar fluxos de trabalho, personalizar interações e apoiar decisões baseadas em dados. A contribuição é conceitual e discute benefícios potenciais dessas tecnologias.

Younas et al. (2025) apresentam a plataforma web DIGI-AI, que reúne criação e gestão de campanhas, recomendações por IA, análise de dados, personalização, automação de e-mail e painéis de acompanhamento. Os autores descrevem a integração desses recursos e apresentam testes de funcionalidades. A plataforma tem foco na automação das atividades de marketing; seus resultados de verificação funcional não constituem uma avaliação de um fluxo de atendimento baseado em tickets e SLA.

Kharisma et al. (2024) apresentam o CRM como gestão da comunicação e do relacionamento entre empresa e cliente. No marketplace de comércio eletrônico pesquisado, os autores relacionam CRM, confiança na marca e intenção de recompra, destacando atualização contínua de informações, retorno rápido e atendimento após a venda. O estudo é restrito ao público de um marketplace e não demonstra efeitos de um sistema de tickets sobre o atendimento em agências de marketing.

Emon et al. (2026) investigam a adoção de CRM com IA no setor de marketing digital de Bangladesh. Com 149 respostas válidas de gestores de marketing, profissionais de marketing digital e especialistas em CRM, identificam associações entre utilidade percebida, prontidão organizacional, compatibilidade tecnológica, pressão competitiva e adoção. A amostragem por conveniência e o desenho transversal delimitam os resultados ao contexto pesquisado. A pesquisa trata de adoção tecnológica e relacionamento com clientes, não da implementação de um Help Desk.

## 10. Métricas e controle de campanhas

Nain et al. (2025) analisam posição dos anúncios, taxa de cliques (CTR), custo por clique (CPC), conversão e retorno sobre o investimento (ROI) em campanhas selecionadas de busca por voz. Yahia e ElBolok (2024) utilizam impressões líquidas esperadas como objetivo do modelo de alocação de orçamento, enquanto Younas et al. (2025) apresentam acompanhamento de impressões, CTR e conversões em painéis de análise de campanhas. As fontes, portanto, examinam indicadores distintos, conforme seus objetivos de estudo.

A documentação oficial do Google Ads define impressões, CTR e CPC médio entre as métricas de acompanhamento de anúncios (Google, [s.d.]b). Também define CPA médio como custo médio por ação de conversão e ROAS como relação entre o valor das conversões e o gasto publicitário (Google, [s.d.]a, [s.d.]c). A tabela a seguir organiza essas definições técnicas; quando o valor das conversões corresponde a vendas, o numerador do ROAS representa a receita atribuída.

| Métrica | Definição técnica | Fonte |
| --- | --- | --- |
| Impressões | Número de exibições de um anúncio. | Google, [s.d.]b |
| Taxa de cliques (CTR) | Proporção de cliques em relação às impressões. | Google, [s.d.]b |
| CPC médio | Gasto com cliques dividido pelo número de cliques. | Google, [s.d.]b |
| Conversões | Resultados registrados nas ações de conversão associadas às metas da campanha. | Google, [s.d.]b |
| CPA médio | Custo total das conversões dividido pelo número de conversões; a documentação utiliza custo por ação. | Google, [s.d.]a |
| ROAS | Valor das conversões dividido pelo gasto com anúncios, expresso como razão ou percentual. | Google, [s.d.]c |

Abbonizio et al. (2023) investigam a distinção entre tráfego genuíno e fraudulento em campanhas digitais, incluindo acessos gerados por bots. O artigo compara modelos de aprendizado de máquina em dados de tráfego anotados e destaca a importância de distinguir conversões reais de tráfego indevido na mensuração da efetividade das campanhas. A discussão se refere à qualidade dos dados publicitários, e não ao tempo de resposta ou à resolução de solicitações de atendimento.

## 11. Rastreabilidade, transparência e proteção de dados

O guia ITIL 4 de Service Desk recomenda capturar e registrar as comunicações e manter dados de histórico de atendimento e conhecimento (PEOPLECERT, 2023). O guia de Service Request Management inclui registros das ações de atendimento, relatórios de revisão e retorno dos usuários entre as informações utilizadas para acompanhar e melhorar o tratamento das solicitações (AXELOS, 2020). Esses registros documentam as interações e ações realizadas ao longo do atendimento.

A comunicação do andamento das solicitações integra o processo descrito pelo guia de Service Desk (PEOPLECERT, 2023). No campo da publicidade direcionada, Daoud et al. (2023) discutem preocupações dos consumidores com coleta e uso de dados e destacam transparência e confiança como dimensões de práticas responsáveis. O estudo aborda percepções sobre publicidade e privacidade, sem avaliar sistemas de atendimento de agências.

A Lei Geral de Proteção de Dados Pessoais (LGPD) estabelece o princípio da necessidade, que limita o tratamento aos dados pertinentes, proporcionais e necessários à finalidade, e prevê medidas de segurança para proteger dados pessoais (BRASIL, 2018). O guia da Autoridade Nacional de Proteção de Dados (ANPD) orienta o controle de acesso mediante autenticação, autorização e auditoria (AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS, 2021). A minimização de dados e a restrição de acesso, portanto, possuem fundamentação normativa, além da discussão de privacidade na literatura de marketing.

## 12. Síntese do marco teórico

A revisão bibliográfica fornece a base de seleção e análise do conhecimento; os requisitos descrevem serviços e restrições; o backlog organiza o trabalho para a evolução do produto; e o MVP delimita um recorte destinado à aprendizagem. Os testes funcionais verificam comportamentos especificados, enquanto os de usabilidade examinam a realização de tarefas e a percepção dos usuários. Esses fundamentos conectam a investigação do problema ao planejamento, à construção e à avaliação da solução (Gil, 1991; Sommerville, 2011; Schwaber e Sutherland, 2020; Caroli, 2015; Myers, Sandler e Badgett, 2012; Teixeira, 2014).

A gestão de demandas e sistemas de informação é fundamentada nos estudos de Service Desk e nos guias de registro, comunicação e atendimento de solicitações (Amanullah, Zeki e Abubakar, 2017; Fenner et al., 2015; Firmansyah e Subriadi, 2022; PEOPLECERT, 2023; AXELOS, 2020). Marketing digital e tráfego pago são contextualizados pela cadeia de publicidade, pela distribuição de orçamento e pelas estratégias de busca paga (Agus et al., 2019; Yahia e ElBolok, 2024; Nain et al., 2025). As competências de gestão de projetos em agências são discutidas por Steponaitis e Andrijauskienė (2023).

A automação de marketing, o CRM e o atendimento são tratados por estudos de integração de atividades, relacionamento e adoção tecnológica (Gujar, Paliwal e Panyam, 2024; Younas et al., 2025; Kharisma et al., 2024; Emon et al., 2026). Métricas e controle de campanhas são abordados pela análise de indicadores e da qualidade do tráfego (Nain et al., 2025; Abbonizio et al., 2023). Rastreabilidade, transparência e proteção de dados têm apoio nos guias de atendimento, na discussão ética da publicidade direcionada e nas fontes normativas de proteção de dados (PEOPLECERT, 2023; AXELOS, 2020; Daoud et al., 2023; BRASIL, 2018; AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS, 2021). Esses seis campos de aplicação complementam os fundamentos de revisão bibliográfica, requisitos, backlog, MVP e testes apresentados no início do referencial, preservando os contextos e limites das evidências.

A especificação BPMN 2.0 complementa esses fundamentos ao estabelecer uma notação para representar processos de negócio e suas interações, compreensível para participantes de negócio e técnicos (OMG, 2011). A representação do atendimento conecta as necessidades especificadas aos fluxos que serão desenvolvidos e avaliados.

## Referências

- AUDY, Jorge. *Scrum 360: um guia completo e prático de agilidade*. Casa do Código, 2015.
- BOOTH, Wayne C.; COLOMB, Gregory G.; WILLIAMS, Joseph M. *The Craft of Research*. 3. ed. Chicago: The University of Chicago Press, 2008.
- CAROLI, Paulo. *Direto ao ponto: criando produtos de forma enxuta*. Casa do Código, 2015.
- GIL, Antonio Carlos. *Como elaborar projetos de pesquisa*. 3. ed. São Paulo: Atlas, 1991.
- MYERS, Glenford J.; SANDLER, Corey; BADGETT, Tom. *The Art of Software Testing*. 3. ed. Hoboken: John Wiley & Sons, 2012.
- PRESSMAN, Roger S.; MAXIM, Bruce R. *Engenharia de software: uma abordagem profissional*. 9. ed. Porto Alegre: AMGH, 2021.
- SCHWABER, Ken; SUTHERLAND, Jeff. *O Guia do Scrum: o guia definitivo para o Scrum: as regras do jogo*. Nov. 2020.
- SOMMERVILLE, Ian. *Engenharia de software*. 9. ed. São Paulo: Pearson Prentice Hall, 2011.
- TEIXEIRA, Fabrício. *Introdução e boas práticas em UX Design*. Casa do Código, 2014.
- RADHITYA, Ridha et al. *Usability Analysis of Digital Signature Application Based on ISO 9241-11 Using Retrospective Think Aloud and User Experience Questionnaire*. 2024. DOI: [10.1109/ICITSI65188.2024.10929447](https://doi.org/10.1109/ICITSI65188.2024.10929447).
- SCHREPP, Martin; HINDERKS, Andreas; THOMASCHEWSKI, Jörg. *Design and Evaluation of a Short Version of the User Experience Questionnaire (UEQ-S)*. *International Journal of Interactive Multimedia and Artificial Intelligence*, v. 4, n. 6, p. 103–108, 2017. DOI: [10.9781/ijimai.2017.09.001](https://doi.org/10.9781/ijimai.2017.09.001).
- ABBONIZIO, Sara; SERNANI, Paolo; DRAGONI, Aldo Franco; RINALDESI, Paolo. *Machine Learning-Based Classification of the Traffic of Digital Marketing Campaigns*. 2023. DOI: [10.1109/MetroXRAINE58569.2023.10405717](https://doi.org/10.1109/MetroXRAINE58569.2023.10405717). Acesso em: 14 ago. 2026.
- AGUS, Anna Amalyah et al. *Digital Publisher, Advertising Media Agency and Mobile Exchange Triadic Interaction: Digital Marketing Service Supply Chain Landscape in Indonesia*. 2019. DOI: [10.1109/IC2IE47452.2019.8940881](https://doi.org/10.1109/IC2IE47452.2019.8940881). Acesso em: 14 ago. 2026.
- DAOUD, Mohammad Khalaf et al. *Examining the Ethical Implications of Data Privacy and Targeted Advertising in Digital Marketing: Consumer Perceptions*. 2023. DOI: [10.1109/SNAMS60348.2023.10375481](https://doi.org/10.1109/SNAMS60348.2023.10375481). Acesso em: 14 ago. 2026.
- AMANULLAH, M.; ZEKI, Akram M.; ABUBAKAR, Adamu. *Information-Based Interactive Services and Support System.* 2017. DOI: [10.1109/SPC.2017.8313025](https://doi.org/10.1109/SPC.2017.8313025). Acesso em: 21 ago. 2026.
- FENNER, Germano et al. *A System Dynamics Model for Managing Service Desk Capacity.* 2015. DOI: [10.1109/INM.2015.7140506](https://doi.org/10.1109/INM.2015.7140506). Acesso em: 21 ago. 2026.
- FIRMANSYAH, Ardhi Dwi; SUBRIADI, Apol Pribadi. *IT Service Desk Model Literature Review: Benefits and Challenges.* 2022. DOI: [10.1109/iSemantic55962.2022.9920439](https://doi.org/10.1109/iSemantic55962.2022.9920439). Acesso em: 21 ago. 2026.
- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Brasília, DF: Presidência da República, 2018. Disponível em: [texto consolidado](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709compilado.htm). Acesso em: 16 ago. 2026.
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* Brasília, DF, 2021. Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/documentos-e-publicacoes/guia-vf.pdf). Acesso em: 16 ago. 2026.
- PEOPLECERT. *ITIL 4 Practice Guide: Service Desk*. London, 2023. Disponível em: [guia de prática](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-desk-3706). Acesso em: 16 ago. 2026.
- AXELOS. *ITIL 4 Practice Guide: Service Request Management*. London, 2020. Disponível em: [site da ITIL](https://www.axelos.com/best-practice-solutions/itil). Acesso em: 16 ago. 2026.
- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2. ed. Geneva, 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html). Acesso em: 16 ago. 2026.
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0*. Needham, 2011. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0/). Acesso em: 16 ago. 2026.
- GUJAR, Praveen; PALIWAL, Gunjan; PANYAM, Sriram. *Revolutionizing In-House Digital Marketing with End-to-End Marketing Automation Powered by AI and SaaS*. 2024. DOI: [10.1109/COLCOM62950.2024.10720305](https://doi.org/10.1109/COLCOM62950.2024.10720305). Acesso em: 14 ago. 2026.
- KHARISMA, Fresha et al. *Digital Marketing, Brand Awareness and Customer Relationship Management on Repurchase Intention in E-Commerce Marketplace*. 2024. DOI: [10.1109/ICBIR61386.2024.10875931](https://doi.org/10.1109/ICBIR61386.2024.10875931). Acesso em: 14 ago. 2026.
- NAIN, Arvind et al. *Impact of Voice-Based Search Queries on Paid Ad Rankings in Digital Marketing Campaigns*. 2025. DOI: [10.1109/IEMECON69302.2025.11365741](https://doi.org/10.1109/IEMECON69302.2025.11365741). Acesso em: 14 ago. 2026.
- STEPONAITIS, Simonas; ANDRIJAUSKIENĖ, Meda. *Improving Digital Project Management Competencies: Case Study of a Digital Marketing Agency in Lithuania*. 2023. DOI: [10.1109/E-TEMS57541.2023.10424612](https://doi.org/10.1109/E-TEMS57541.2023.10424612). Acesso em: 14 ago. 2026.
- YAHIA, Zakaria; ELBOLOK, Mostafa *A Proposed Nonlinear Programming Optimization Model for Optimal Budget Mix of Digital Marketing Campaigns*. 2024. DOI: [10.1109/NILES63360.2024.10753162](https://doi.org/10.1109/NILES63360.2024.10753162). Acesso em: 14 ago. 2026.
- YOUNAS, Mamoona et al. *DIGI-AI: An AI-Powered Web Platform for Intelligent Digital Marketing Automation*. 2025. DOI: [10.1109/MACS68476.2025.11453341](https://doi.org/10.1109/MACS68476.2025.11453341). Acesso em: 14 ago. 2026.

- EMON, Md Mehedi Hasan et al. *Adoption of Artificial Intelligence in Customer Relationship Management: Insights from Bangladesh’s Digital Marketing Sector*. 2026. DOI: [10.1109/QPAIN69676.2026.11546086](https://doi.org/10.1109/QPAIN69676.2026.11546086). Acesso em: 3 out. 2026.
- GOOGLE. *CPA médio (definição)*. [s.d.]a. Disponível em: [Ajuda do Google Ads](https://support.google.com/google-ads/answer/6396841?hl=pt-BR). Acesso em: 3 out. 2026.
- GOOGLE. *Sobre as colunas da tabela de estatísticas*. [s.d.]b. Disponível em: [Ajuda do Google Ads](https://support.google.com/google-ads/answer/2454071?hl=pt-BR). Acesso em: 3 out. 2026.
- GOOGLE. *Sobre os lances de ROAS desejado*. [s.d.]c. Disponível em: [Ajuda do Google Ads](https://support.google.com/google-ads/answer/6268637?hl=pt-BR). Acesso em: 3 out. 2026.
