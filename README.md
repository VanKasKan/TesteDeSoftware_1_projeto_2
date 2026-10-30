# Aluguel de carros

Projeto prático da segunda unidade de Testes de Software: cobertura de arestas, caminhos básicos de McCabe, MC/DC, mutação e test smells. As regras de negócio e de validação permanecem as mesmas da primeira unidade; a implementação fornecida foi corrigida.

O [enunciado](ENUNCIADO.md) define as atividades. Os alunos devem criar `resposta.md` na raiz com todas as respostas, análises e justificativas. O código dos testes permanece em `src/test/java`; relatórios originais e logs devem ser copiados para `evidencias/` e referenciados em `resposta.md`.

## Identificação do aluno ou da dupla

Antes da entrega, os alunos devem atualizar o **`artifactId` do projeto no `pom.xml`**, inicialmente `aluguel-carros-teste-estrutural`, para identificar os integrantes:

- Individual: `NomeSobrenome`.
- Dupla: `Nome1Sobrenome1_Nome2Sobrenome2`.

Substitua os nomes pelos dos alunos, sem espaços ou acentos. Altere apenas o `artifactId` do projeto, sem modificar os identificadores das dependências e dos plugins. Registre o valor em `resposta.md` e utilize-o também como nome do arquivo compactado de entrega, conforme o enunciado.

## Preparação

Instale um JDK 17 ou superior e Maven. Para os verificadores de mutação e smells, instale também **Python 3.9 ou superior**; para o tsDetect, **Git**. Os comandos `java`, `mvn`, `git` e `python3` devem estar no PATH. A primeira execução exige internet para obter plugins e dependências.

Se o Python for chamado `python` em seu ambiente, acrescente `-Dpython.executable=python` aos comandos. Execute todos os comandos na raiz do projeto. Também é possível usar `./mvnw` (Linux/macOS) ou `mvnw.cmd` (Windows) como alternativa ao Maven principal; a compilação interna do tsDetect ainda requer `mvn` no PATH.

## Testes JUnit

```bash
mvn test
```

Os resultados da suíte completa ficam em `target/surefire-reports/suite/`. Os perfis de análise abaixo também executam os testes antes de gerar relatórios. A suíte fornecida é um ponto de partida; as metas devem ser verificadas e atendidas pelos alunos.

## Classes por critério

| Classe | Responsabilidade | Executar apenas seus testes |
|---|---|---|
| `CoberturaArestasTest` | 100% de arestas | `mvn -Parestas test` |
| `CaminhosBasicosTest` | Caminhos básicos de McCabe | `mvn -Pcaminhos test` |
| `McdcTest` | MC/DC em duas decisões | `mvn -Pmcdc test` |

Cada classe contém somente um exemplo parametrizado básico. Cabe aos alunos ampliar os testes, demonstrando cada critério isoladamente. Cenários podem ser repetidos entre classes quando necessários para critérios diferentes. PIT e tsDetect analisam a suíte completa, executada por `mvn test`, sem classes adicionais para essas ferramentas.

Use um único perfil de grupo por comando. Os relatórios JUnit ficam em `target/surefire-reports/<grupo>/`. Execute PIT e tsDetect sem perfis de grupo nem filtros `-Dtest`.

## JaCoCo

```bash
mvn -Pjacoco,arestas clean verify
```

Essa execução instrumenta somente `CoberturaArestasTest` e gera `target/site/jacoco/arestas/index.html` e `target/site/jacoco/arestas/jacoco.xml`. A verificação exige **zero ramos não cobertos** na classe `CalculadoraCustoLocacao`; se a meta não for atendida, o Maven termina com falha depois de gerar os relatórios. A métrica exigida é `BRANCH`, não apenas cobertura de linhas. MC/DC e caminhos independentes precisam da demonstração em `resposta.md`.

Para gerar evidências isoladas dos outros critérios:

```bash
mvn -Pjacoco,caminhos verify
mvn -Pjacoco,mcdc verify
```

Os relatórios ficam, respectivamente, em `target/site/jacoco/caminhos/` e `target/site/jacoco/mcdc/`, com arquivos HTML e XML. Esses perfis geram cobertura de apoio, sem exigir 100% de ramos: os critérios são demonstrados pelos caminhos e pares de independência em `resposta.md`. A verificação de 100% de ramos permanece obrigatória no grupo `arestas`.

Cada grupo usa um arquivo `target/jacoco-<grupo>.exec`, sem acumular dados de execuções anteriores. Assim, os testes de um grupo não completam a cobertura de outro. O perfil `jacoco` sem grupo mede a suíte conjunta em `target/site/jacoco/suite/`, mas não substitui a execução isolada exigida.

## PIT (PITest)

```bash
mvn -Ppitest verify
```

O perfil usa JUnit 5, os operadores `DEFAULTS` e a classe `CalculadoraCustoLocacao` como alvo. Gera `target/pit-reports/index.html` e `target/pit-reports/mutations.xml`, exige limiar de mutação de 100% e confere adicionalmente que **todos os status sejam `KILLED`**. Timeout não satisfaz essa conferência. O Maven termina com falha quando a meta não é atingida; o relatório permanece disponível para análise.

Para uma execução exclusivamente diagnóstica, quando for necessário gerar relatórios sem bloquear por mutantes não mortos:

```bash
mvn -Ppitest -Dpit.threshold=0 -Dquality.skip=true verify
```

Essas opções desativam as duas barreiras de mutação, mas não alteram os mutantes gerados. **Não comprovam cumprimento da meta**. Qualquer uso na entrega precisa ser explicitado e acompanhado das justificativas individuais em `resposta.md`, conforme o enunciado. Sem essas opções, as verificações são obrigatórias. Quando o verificador complementar é executado, ele também grava `target/pit-reports/status-summary.json`.

## tsDetect

```bash
mvn -Ptsdetect verify
```

O perfil chama `scripts/quality.py`, que baixa o [tsDetect oficial](https://github.com/TestSmells/TestSmellDetector) no commit fixo `e89ce1a28d56db314d159e8957591aef6002173d`, compila seu JAR e o executa com os **21 detectores e limiares padrão**. O código e o JAR ficam em `target/tools/tsdetect/`; `mvn clean` remove esse cache, exigindo uma nova preparação na execução seguinte.

A versão original reconhece apenas testes públicos com `@Test`. O script aplica uma adaptação de compatibilidade ao código da ferramenta: reconhece `@Test`, `@ParameterizedTest` e `@RepeatedTest` sem exigir `public`, além de `@BeforeEach` na identificação da preparação. Corrige também a contagem para informar métodos de teste reconhecidos, em vez de todos os métodos da classe. Nenhum detector é desativado e os limiares são preservados. Há também uma correção explícita em `MagicNumberTest`: a comparação `magicCount >= 0` do código original marcava até métodos sem qualquer número mágico. A integração usa `magicCount > 0`, mantendo a detecção de toda contagem positiva. Essa adaptação é identificada como `junit5-magic-zero-v1` no resumo da análise. A adaptação está explícita no script; os fontes dos testes e da aplicação não são reescritos.

Todos os arquivos Java sob `src/test/java` são incluídos. O arquivo `scripts/tsdetect-mapping.json` associa explicitamente as três classes por critério a `CalculadoraCustoLocacao.java`. Para outros nomes, o script mantém a convenção **`ClasseTest.java` → `Classe.java`**, no mesmo pacote relativo; mapeamentos adicionais devem ser registrados no JSON. Arquivos auxiliares nessa árvore, testes dinâmicos, estruturas aninhadas ou convenções diferentes podem exigir uma ampliação explícita da integração. A conferência com os métodos registrados pelo Surefire impede aceitar uma divergência de contagem como sucesso; ela não substitui a revisão de compatibilidade dos recursos utilizados.

Arquivos gerados:

- `target/tsdetect/input.csv`: arquivos enviados à ferramenta;
- `target/tsdetect/test-smells.csv`: relatório original, com contagens por detector;
- `target/tsdetect/summary.json`: resumo, quantidade de arquivos e métodos analisados e versão adotada.

O Maven termina com falha se houver qualquer ocorrência, erro de análise, arquivo sem testes reconhecidos ou divergência entre a abrangência do relatório e a suíte executada. Uma falha por smells é um resultado da análise: os testes devem ser refatorados e as ferramentas executadas novamente.

## Execução e preservação das evidências

Execute os perfis separadamente para conseguir examinar todas as análises mesmo quando uma meta falhar. Execute `clean` apenas antes da primeira análise para preservar os relatórios das anteriores:

```bash
mvn -Pjacoco,arestas clean verify
mvn -Pjacoco,caminhos verify
mvn -Pjacoco,mcdc verify
mvn -Ppitest verify
mvn -Ptsdetect verify
```

As análises por grupo devem ser executadas separadamente. PIT e tsDetect usam a suíte completa, e seus relatórios não substituem os relatórios isolados de cada critério.

Registre os logs de cada comando pelo terminal, preservando seu resultado de sucesso ou falha. Copie os diretórios dos relatórios completos e os logs para `evidencias/` **antes** de executar `mvn clean` para preparar o arquivo de entrega. Relacione as evidências em `resposta.md`. A limpeza remove todos os relatórios e o cache do tsDetect em `target`.
