# Testes de Software — Trabalho Prático da Segunda Unidade

## 1. Objetivo

Projetar, implementar e avaliar uma suíte de testes automatizados com **JUnit 5** para o cálculo do custo de locação de veículos, atendendo aos seguintes requisitos:

- **100% de cobertura de arestas** do método de cálculo;
- aplicação do **critério de McCabe (caminhos básicos)**, com cálculo da complexidade ciclomática e identificação de caminhos independentes;
- aplicação de **MC/DC em duas decisões, cada uma com três condições atômicas**;
- evidências de cobertura produzidas pelo **JaCoCo**, integrado ao Maven;
- teste de mutação com **PIT (PITest)**, integrado ao Maven, buscando matar todos os mutantes gerados no escopo definido;
- análise de test smells com **tsDetect**, integrada ao Maven, sem ocorrências na suíte entregue.

O trabalho deverá ser realizado individualmente ou em dupla. A implementação fornecida foi corrigida para esta unidade usando o ChatGPT, modelo GPT-6.1 Sol. **Os alunos não devem alterar o código de produção em `src/main/java`**, nem suas regras de negócio, para facilitar a obtenção das metas. Podem criar, ampliar e refatorar os testes, modificar o `pom.xml` e acrescentar os arquivos de apoio necessários à automação.

Os testes já presentes no projeto podem ser reutilizados, mas não constituem evidência de atendimento aos critérios. Cabe aos integrantes verificar e complementar a suíte.

## 2. Funcionalidade

Em um sistema de aluguel de carros, o custo a ser pago pela locação depende da categoria do veículo, do número de diárias contratadas, da quilometragem percorrida, do seguro escolhido e de eventual atraso na devolução. O nível de fidelidade do cliente e a existência de atrasos anteriores também influenciam o valor, pois determinam a concessão de benefícios.

O método abaixo deve retornar o valor total da locação conforme as regras deste enunciado:

```java
BigDecimal calcularCustoLocacao(
    CategoriaVeiculo categoria,
    NivelCliente nivelCliente,
    int numeroDiarias,
    int quilometrosRodados,
    int horasAtraso,
    TipoSeguro seguro,
    boolean possuiAtrasoAnterior
)
```

**As regras de negócio e de validação permanecem as mesmas da primeira unidade.** Elas são reproduzidas nas seções 3 e 4 para consulta; a mudança nesta unidade está nos critérios de teste e nas evidências exigidas.

## 3. Regras de negócio

### Diárias e desconto por duração

O valor bruto das diárias corresponde ao número de diárias multiplicado pelo valor da categoria.

| Categoria | Valor da diária |
|---|---:|
| ECONOMICO | R$ 120,00 |
| INTERMEDIARIO | R$ 180,00 |
| SUV | R$ 280,00 |

O desconto por duração incide somente sobre o valor das diárias.

| Número de diárias | Desconto |
|---|---:|
| 1 a 2 | 0% |
| 3 a 6 | 5% |
| 7 a 14 | 10% |
| 15 ou mais | 15% |

### Quilometragem

Cada diária contratada inclui 100 km de franquia. Dentro da franquia total, não há cobrança adicional. Acima dela, a tarifa da faixa é aplicada sobre **toda a quilometragem excedente**, sem cálculo progressivo.

| Quilometragem excedente | Tarifa |
|---|---:|
| Até 100 km | R$ 0,80/km |
| Mais de 100 até 300 km | R$ 1,00/km |
| Mais de 300 km | R$ 1,50/km |

### Seguro

O custo do seguro corresponde ao valor por diária multiplicado pelo número de diárias contratadas, sem descontos.

| Seguro | Valor por diária |
|---|---:|
| SEM_SEGURO | R$ 0,00 |
| BASICO | R$ 25,00 |
| COMPLETO | R$ 45,00 |

### Atraso na devolução

A cobrança utiliza o valor da diária da categoria, sem descontos.

| Horas de atraso | Cobrança |
|---|---|
| 0 ou 1 | Sem cobrança |
| 2 ou 3 | 20% do valor de uma diária multiplicado pelo total de horas de atraso |
| 4 ou mais | Uma diária adicional, cobrada uma única vez |

### Benefício de fidelidade

O desconto de fidelidade incide sobre o valor das diárias **após o desconto por duração**.

- **COMUM:** não recebe desconto adicional.
- **PRATA:** recebe 5% quando a locação tem pelo menos 7 diárias e não há registro de atraso anterior.
- **OURO:** recebe 10% quando a locação tem pelo menos 5 diárias e não há registro de atraso anterior.

Se as condições do benefício não forem atendidas, não há desconto de fidelidade.

### Cálculo final

A ordem do cálculo é: valor bruto das diárias, desconto por duração, desconto de fidelidade, custo da quilometragem excedente, seguro, atraso e soma dos componentes.

```text
Total = diárias com descontos + custo da quilometragem excedente + seguro + atraso
```

Os descontos não incidem sobre quilometragem, seguro ou atraso. O resultado final deve ser arredondado para duas casas decimais com `HALF_UP`.

## 4. Entradas inválidas

O método deve lançar `NullPointerException` quando `categoria`, `nivelCliente` ou `seguro` forem `null`.

Deve lançar `IllegalArgumentException` quando:

- `numeroDiarias <= 0`;
- `quilometrosRodados < 0`;
- `horasAtraso < 0`.

Quando houver simultaneamente um parâmetro nulo e um valor numérico inválido, deve prevalecer `NullPointerException`.

## 5. Atividades obrigatórias

Crie, na raiz do projeto, o arquivo **`resposta.md`**, contendo **todas as respostas da atividade, exceto o código dos testes**, que deve permanecer em `src/test/java`. Inclua nesse arquivo identificação dos integrantes, grafo, tabelas, cálculos, casos de teste, rastreabilidade, pares MC/DC, análises, justificativas e relatório consolidado. O grafo pode ser apresentado em Mermaid dentro do próprio Markdown. Os relatórios originais e logs das ferramentas devem ser mantidos em `evidencias/` e referenciados por links relativos em `resposta.md`; esses arquivos são evidências de apoio, não substituem as respostas. Não é exigida planilha separada.


### Organização obrigatória dos testes

Organize os testes em três classes no pacote `br.com.aluguelcarros`, em `src/test/java`:

| Arquivo | Critério demonstrado | Execução isolada |
|---|---|---|
| `CoberturaArestasTest.java` | 100% de cobertura de arestas | `mvn -Pjacoco,arestas verify` |
| `CaminhosBasicosTest.java` | Caminhos independentes de McCabe | `mvn -Pjacoco,caminhos verify` |
| `McdcTest.java` | MC/DC das duas decisões escolhidas | `mvn -Pjacoco,mcdc verify` |

Cada classe deve demonstrar seu critério **isoladamente**, sem depender da execução das outras. Um mesmo cenário pode aparecer em classes diferentes quando necessário para demonstrar critérios distintos; identifique essa relação em `resposta.md`. As classes fornecidas contêm apenas um teste parametrizado básico como exemplo, não a solução dos critérios.

PIT e tsDetect devem avaliar **a suíte completa das três classes**, sem classes específicas para essas ferramentas. Testes acrescentados para matar mutantes devem permanecer na classe do critério ao qual se relacionam. Não use chamadas entre métodos de teste para compartilhar cenários. Ao refatorar duplicações, preserve a execução independente e a clareza de cada grupo.

Use apenas um perfil de grupo (`arestas`, `caminhos` ou `mcdc`) por execução. Execute PIT e tsDetect sem perfis de grupo nem filtros `-Dtest`. Os relatórios de cada grupo ficam separados; a cobertura conjunta da suíte não substitui a comprovação isolada de cada critério.

### 5.1. Cobertura de arestas

Construa o grafo de fluxo de controle do método `calcularCustoLocacao`, identificando seus nós e arestas. Represente o curto-circuito das condições compostas (`&&` e `||`) e as saídas por exceção. Declare a convenção adotada para os nós de entrada e saída.

Projete e implemente testes que percorram **100% das arestas do grafo**. A suíte deve verificar resultados e exceções por meio de asserções; apenas executar o método não é suficiente. Os resultados esperados devem ser calculados a partir das regras deste enunciado, sem reproduzir o algoritmo de produção como oráculo.

Documente, em tabelas no `resposta.md`:

- a identificação dos nós e das arestas, com correspondência ao código-fonte;
- os casos de teste, suas entradas e resultados ou exceções esperados;
- as arestas percorridas por cada caso;
- a relação entre cada caso documentado e o teste JUnit correspondente, incluindo a identificação da execução quando houver testes parametrizados.

Utilize os perfis Maven `jacoco,arestas` fornecidos para executar somente `CoberturaArestasTest`, com instrumentação, geração dos relatórios e verificação da meta pelo JaCoCo. A regra de verificação deve abranger a classe `br.com.aluguelcarros.CalculadoraCustoLocacao`, utilizar o contador **`BRANCH`** e exigir **`COVEREDRATIO = 1.00`** ou **`MISSEDCOUNT = 0`**, fazendo a execução falhar quando houver ramos não cobertos. Entregue os relatórios **HTML e XML**, com as quantidades de ramos cobertos e não cobertos do método.

Para a evidência automatizada desta atividade, será usada a cobertura de ramos do JaCoCo. Essa métrica é calculada sobre o bytecode e não representa, isoladamente, todas as arestas de um grafo de código-fonte; o tratamento de exceções também não é contado como ramo pelo JaCoCo. Por isso, o relatório da ferramenta deve ser acompanhado do grafo, da rastreabilidade e dos testes de exceção. **100% de linhas não substitui 100% de ramos.**

### 5.2. Critério de McCabe: complexidade ciclomática e caminhos independentes

Utilize o mesmo grafo de fluxo de controle da seção 5.1 para aplicar o **critério de caminhos básicos de McCabe (basis path testing)** ao método `calcularCustoLocacao`.

1. Calcule a complexidade ciclomática por **`V(G) = E - N + 2`**, em que `E` é o número de arestas e `N` é o número de nós do grafo de um único método conectado. Para esse cálculo, utilize uma entrada e uma saída únicas, conectando à saída comum tanto o retorno normal quanto as saídas por exceção.
2. Confira o resultado pela contagem de **nós de decisão binária + 1**, considerando separadamente as condições atômicas representadas no grafo para o curto-circuito. Apresente as contagens e explique qualquer divergência entre os cálculos.
3. Identifique um conjunto de **`V(G)` caminhos linearmente independentes**, da entrada à saída. Nomeie-os (`P1`, `P2`, ...) e descreva cada um pela sequência dos identificadores dos nós ou das arestas do grafo.
4. Demonstre a independência dos caminhos, por exemplo, com uma matriz caminho × aresta e verificação de independência linear. Caso utilize a construção incremental em que cada novo caminho introduz uma aresta ainda não presente nos anteriores, identifique essa aresta em cada passo. Apenas listar caminhos diferentes não demonstra independência.
5. Determine entradas concretas que executem cada caminho viável e implemente os testes JUnit correspondentes, com asserções para o valor ou a exceção esperados. Registre no `resposta.md` a relação **caminho → entradas → resultado esperado → teste JUnit**. Cenários das demais atividades podem ser reutilizados, mas os testes necessários à base devem estar em `CaminhosBasicosTest` e executar sem as outras classes.

Se um caminho escolhido for inviável, demonstre a contradição entre as condições necessárias para percorrê-lo e procure outro caminho independente viável. Quando dependências entre condições impedirem uma base executável com `V(G)` caminhos, documente os caminhos inviáveis, suas restrições e a independência do conjunto viável obtido, justificando por que não é possível ampliá-lo. Não altere o código de produção nem considere um caminho inviável como executado.

Compare o valor calculado com a complexidade informada pelo JaCoCo para o método e explique eventuais diferenças de modelagem, especialmente nas saídas por exceção. **O relatório de cobertura do JaCoCo, isoladamente, não demonstra que uma base de caminhos independentes foi executada.** A evidência desse critério deve reunir o grafo, os cálculos, a demonstração de independência e a rastreabilidade dos testes. O objetivo é cobrir uma base de caminhos, sem exigir a enumeração de todos os caminhos possíveis.

### 5.3. MC/DC em duas decisões

Escolha **duas decisões existentes no método**, cada uma contendo exatamente **três condições atômicas**. Não altere a implementação para criar ou simplificar decisões.

Para cada decisão:

1. Transcreva a expressão e identifique suas condições como A, B e C.
2. Registre os casos utilizados, com as entradas concretas, os valores lógicos das condições e o resultado da decisão.
3. Indique, para cada condição, um **par de independência**: dois casos em que apenas essa condição muda de valor lógico e o resultado da decisão também muda, mantendo os valores lógicos das outras duas condições.
4. Relacione esses casos aos testes JUnit e aos resultados esperados.
5. Explique o efeito do curto-circuito de Java: diferencie o valor lógico determinado pelas entradas de uma condição efetivamente avaliada durante a execução. Identifique condições não avaliadas e demonstre que os testes alcançam a decisão escolhida, inclusive quando ela estiver em um `else if`.

A demonstração deve atender ao **MC/DC de causa única** para as três condições de cada decisão. Organize as tabelas e os pares no `resposta.md`. É permitido reutilizar cenários de cobertura de arestas, mantendo em `McdcTest` todos os testes necessários para demonstrar o MC/DC isoladamente. **Cobertura de ramos de 100% no JaCoCo não comprova MC/DC**; sua evidência é a análise dos pares associada aos testes executados.

### 5.4. Teste de mutação com PIT

Utilize o perfil Maven `pitest` fornecido, com suporte ao JUnit 5, tendo como alvo a classe `br.com.aluguelcarros.CalculadoraCustoLocacao` e utilizando a suíte completa das três classes, sem filtros por critério. Fixe as versões utilizadas e configure explicitamente o conjunto de operadores **`DEFAULTS`**. Não reduza o conjunto de operadores nem exclua trechos do método para elevar o resultado.

Execute a análise e amplie os testes para **matar todos os mutantes gerados nesse escopo**. Entregue os relatórios **HTML e XML**, informando o total de mutantes e a quantidade por status. A suíte original deve passar antes da análise.

Para cada mutante que permanecer sem ser morto, registre individualmente no relatório:

- identificação, linha, operador e alteração realizada;
- status apresentado pelo PIT;
- tentativas de elaborar um teste capaz de distinguir o comportamento original do mutante;
- justificativa técnica para a impossibilidade de matá-lo, quando aplicável.

A alegação de **mutante equivalente** deve demonstrar por que a alteração não produz diferença observável para nenhuma entrada válida no domínio do método, incluindo o comportamento de validação previsto. Não basta afirmar que nenhum teste encontrado o matou. Mutantes sem cobertura indicam uma lacuna a corrigir; erros de execução ou de configuração não são justificativas de equivalência. Timeout não deve ser apresentado como evidência de morte por uma asserção.

A meta é **100% de mutantes com status `KILLED`**. Utilize a verificação automatizada fornecida, que combina o limiar do PIT com a conferência complementar dos status no XML. Caso existam mutantes comprovadamente impossíveis de matar, mantenha o relatório bruto e documente a exceção à meta, sem esconder mutantes, editar resultados ou apresentá-los como mortos. Qualquer flexibilização do bloqueio no Maven deve estar explícita e vinculada às justificativas individuais; sua aceitação dependerá da avaliação dessas justificativas.

### 5.5. Ausência de test smells com tsDetect

Utilize a integração do **tsDetect** fornecida no perfil Maven `tsdetect`. É permitido utilizar um plugin de execução para chamar a ferramenta de linha de comando e um script de apoio para preparar entradas e verificar a saída. A execução deve ser reproduzível sem depender de análise manual pela IDE.

A integração fornecida inclui a adaptação de reconhecimento de JUnit 5 e a correção do falso positivo de contagem zero em `MagicNumberTest`, descritas no `README.md`. Essas adaptações fazem parte da ferramenta disponibilizada para a atividade; não autorizam alterações adicionais para esconder ocorrências.

A integração deve:

- fixar a versão ou o commit do tsDetect e documentar sua obtenção, os requisitos de execução e os limiares utilizados;
- preparar os arquivos de entrada exigidos pela versão escolhida, abrangendo **todos os arquivos de teste da suíte entregue** e suas classes de produção correspondentes;
- executar os detectores disponíveis na versão adotada, com seus limiares padrão, e salvar o relatório original;
- conferir que todos os testes esperados foram reconhecidos e analisados, inclusive testes JUnit 5 e parametrizados, quando utilizados;
- produzir um resumo por tipo de smell e fazer a execução Maven falhar se houver qualquer ocorrência.

Refatore os testes até obter **zero ocorrências de todos os tipos de test smells analisados pelo tsDetect**, preservando as asserções, a cobertura de arestas, os caminhos básicos de McCabe, o MC/DC e a capacidade de matar mutantes. Documente os smells encontrados e as refatorações realizadas; se a primeira análise já apresentar zero ocorrências, registre esse resultado com suas evidências.

**Relatório vazio, arquivo ignorado, teste não reconhecido ou erro de análise não comprova ausência de smells.** Se a distribuição escolhida não reconhecer os recursos JUnit utilizados, será necessário adotar uma versão compatível ou adequar a escrita dos testes, mantendo os critérios da atividade. Não é permitido desativar detectores, aumentar limiares ou omitir arquivos para obter zero ocorrências.

O resultado comprova ausência de ocorrências segundo os detectores e limiares utilizados, e não ausência absoluta de qualquer problema de qualidade no código de teste.

### 5.6. Automação e relatório consolidado

As três ferramentas devem ser acionadas pelo **Maven**, com comandos documentados no `README.md`. É permitido organizar as análises em perfis separados, desde que todos os comandos necessários sejam informados e executados na entrega. O projeto já fornece os perfis de grupo `arestas`, `caminhos` e `mcdc`, além de `jacoco`, `pitest` e `tsdetect`, os verificadores e as instruções de execução no `README.md`. Cabe aos integrantes executá-los, analisar os resultados e elaborar os testes necessários, preservando o escopo e as metas fornecidos.

A automação deve executar os testes, gerar as evidências e verificar as metas. Apenas declarar plugins no `pom.xml` ou apresentar capturas de tela de uma IDE não atende ao requisito. Arquivos auxiliares devem usar caminhos relativos ao projeto ou parâmetros documentados, sem depender de caminhos pessoais da máquina dos integrantes.

No relatório consolidado, dentro de `resposta.md`, apresente:

- versões do Java, Maven, JUnit e das três ferramentas;
- comandos executados, perfis, escopos, operadores e limiares;
- quantidade de testes executados, falhas, erros e testes ignorados;
- cobertura de arestas do grafo e contagens de ramos do JaCoCo;
- complexidade ciclomática, caminhos independentes, testes correspondentes e justificativas de inviabilidade, se houver;
- identificação das duas decisões e evidências MC/DC;
- resultado do PIT, com justificativas individuais de mutantes não mortos, se houver;
- resultado do tsDetect e evidências de abrangência da análise;
- conclusão sobre o atendimento de cada requisito e qualquer limitação encontrada.

Os relatórios finais devem corresponder **à mesma versão entregue da suíte e do código de produção**. Após qualquer refatoração, execute novamente as análises necessárias para comprovar que todas as metas continuam atendidas. Não aceite falhas de testes, análises ignoradas ou relatórios antigos como uma execução válida.

## 6. Entrega e avaliação

Os alunos devem obrigatoriamente atualizar o **`artifactId` do projeto no `pom.xml`**, inicialmente `aluguel-carros-teste-estrutural`, para identificar os integrantes: `NomeSobrenome` no trabalho individual ou `Nome1Sobrenome1_Nome2Sobrenome2` no trabalho em dupla, substituindo os nomes pelos dos discentes, sem espaços ou acentos. Altere somente o identificador do projeto, preservando os `artifactId` das dependências e dos plugins, e registre o novo valor em `resposta.md`.

Entregue o projeto completo, contendo:

1. Código de produção fornecido, sem alterações, e suíte de testes JUnit 5 organizada nas três classes exigidas, cada uma demonstrando seu critério isoladamente.
2. `pom.xml` com as integrações e verificações exigidas, além dos scripts e arquivos de apoio necessários.
3. `README.md` com instruções de preparação do ambiente e execução, atualizado se houver mudanças necessárias à reprodução.
4. **`resposta.md` com todas as respostas**, identificação dos integrantes, grafo, tabelas de rastreabilidade, cálculo de McCabe, caminhos independentes, análises MC/DC, relatório consolidado e justificativas de mutantes não mortos, quando aplicável.
5. Diretório `evidencias/` com relatórios originais do JaCoCo, PIT e tsDetect e logs das execuções Maven, referenciados em `resposta.md`. Preserve os recursos necessários para abrir os relatórios HTML.

Antes da limpeza, copie os relatórios e logs para `evidencias/`, fora de `target`. Em seguida, execute `mvn clean` e compacte o projeto em um único arquivo **`.zip` ou `.tar.gz`**, incluindo as evidências e sem o diretório `target`. **Não utilize o formato `.rar`.**

O nome do arquivo compactado, sem a extensão, deve ser igual ao `artifactId`: `NomeSobrenome.zip` no trabalho individual ou `Nome1Sobrenome1_Nome2Sobrenome2.zip` no trabalho em dupla. Caso utilize `.tar.gz`, mantenha o mesmo padrão de nome.

A avaliação considerará:

- atendimento aos 100% de cobertura de arestas, com coerência entre grafo, testes e JaCoCo;
- correção do cálculo de McCabe, da identificação dos caminhos independentes e dos testes que os exercitam, incluindo eventuais justificativas de inviabilidade;
- correção dos pares de independência MC/DC nas duas decisões de três condições;
- capacidade de matar mutantes e rigor das justificativas para eventuais impossibilidades;
- zero ocorrências no tsDetect, abrangência da análise e qualidade das refatorações;
- correção das asserções, rastreabilidade, clareza e reprodutibilidade da automação e das evidências.

Não serão aceitas reduções artificiais do escopo, supressão de testes ou detectores, alteração do código de produção ou manipulação dos relatórios para atingir as metas. Justificativas de mutantes impossíveis de matar serão avaliadas individualmente e não dispensam os demais requisitos.

O arquivo compactado deve ser entregue via **SIGAA até a data prevista para a atividade**. Em caso de dupla, basta que um dos integrantes realize a entrega.

## 7. Referências das ferramentas

- [JaCoCo: definição dos contadores de cobertura](https://www.jacoco.org/jacoco/trunk/doc/counters.html).
- [JaCoCo: verificação de limites com `check`](https://www.jacoco.org/jacoco/trunk/doc/check-mojo.html).
- [PIT: configuração e execução com Maven](https://pitest.org/quickstart/maven/).
- [tsDetect: repositório e instruções de execução](https://github.com/TestSmells/TestSmellDetector).
- [NIST SP 500-235: teste estruturado, complexidade ciclomática e caminhos básicos](https://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication500-235.pdf).
