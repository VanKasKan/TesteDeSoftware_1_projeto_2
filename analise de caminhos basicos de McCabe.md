# Análise de Caminhos Básicos de McCabe (Basis Path Testing)
**Método:** `CalculadoraCustoLocacao.calcularCustoLocacao`

---

## 1. Cálculo da Complexidade Ciclomática

Conceitualmente é dado por $V(G) = E - N + 2$
* $V(G)$: Complexidade Ciclomática
* $E$: Número de arestas 
* $N$: Número de nós

Aplicando:
$V(G) = 52 - 29 + 2 = 25$

Sendo:
* 1 nó inicial.
* 24 nós de decisão.
* 3 nós de saída diferente ('NullPointerException','IllegalArgumentException' e sem exceção).
* 1 nó unificando a saída global.

---

## 2. Cálculo por Nós de Decisão Binária

O cálculo alternativo pela contagem de nós de decisão com 2 saídas (condições atômicas de curto-circuito) é dado por:

$$\mathbf{V(G) = \text{Nós de Decisão Binária} + 1}$$

### Mapeamento dos 18 Nós de Decisão ($d_1 \dots d_{18}$):
1. `N1`: `categoria == null`
2. `N2`: `nivelCliente == null`
3. `N3`: `seguro == null`
4. `N4`: `numeroDiarias <= 0`
5. `N5`: `quilometrosRodados < 0`
6. `N6`: `horasAtraso < 0`
7. `N7`: `categoria == ECONOMICO`
8. `N8`: `categoria == INTERMEDIARIO`
9. `N9`: `numeroDiarias <= 2`
10. `N10`: `numeroDiarias <= 6`
11. `N11`: `numeroDiarias <= 14`
12. `N12`: `nivelCliente == PRATA`
13. `N13`: `numeroDiarias >= 7` 
14. `N14`: `!possuiAtrasoAnterior`
15. `N15`: `nivelCliente == OURO`
16. `N16`: `numeroDiarias >= 5` 
17. `N17`: `!possuiAtrasoAnterior`
18. `N18`: `quilometrosExcedentes <= 0`
19. `N19`: `quilometrosExcedentes <= 100`
20. `N20`: `quilometrosExcedentes <= 300`
21. `N21`: `seguro == SEM_SEGURO` 
22. `N22`: `seguro == BASICO`
23. `N23`: `horasAtraso <= 1`
24. `N24`: `horasAtraso <= 3`

Com 24 condições atômicas simples representadas no grafo:
$$\mathbf{V(G) = 24 + 1 = 25}$$

---

## 3. Conjunto de Caminhos Linearmente Independentes (P1 a P25)

Para facilitar a leitura, defino dois trechos recorrentes:

- A: `START → N1 → N2 → N3 → N4 → N5 → N6`
- T: `N18 → N19 → N20 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL`

Os caminhos completos são:

| Caminho | Sequência de nós                                                                                          |
| ------- | --------------------------------------------------------------------------------------------------------- |
| P1      | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → T`                                                            |
| P2      | `START → N1 → E_NPE → EXIT_GLOBAL`                                                                        |
| P3      | `A → N2 → E_NPE → EXIT_GLOBAL`                                                                            |
| P4      | `A → N3 → E_NPE → EXIT_GLOBAL`                                                                            |
| P5      | `A → N4 → E_IAE → EXIT_GLOBAL`                                                                            |
| P6      | `A → N5 → E_IAE → EXIT_GLOBAL`                                                                            |
| P7      | `A → N6 → E_IAE → EXIT_GLOBAL`                                                                            |
| P8      | `A → N7 → N9 → N10 → N11 → N12 → N15 → T`                                                                 |
| P9      | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → T`                                                            |
| P10     | `A → N7 → N8 → N9 → N12 → N15 → T`                                                                        |
| P11     | `A → N7 → N8 → N9 → N10 → N12 → N15 → T`                                                                  |
| P12     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → T`                                                            |
| P13     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N13 → N15 → T`                                                      |
| P14     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N13 → N14 → N15 → T`                                                |
| P15     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N13 → N14 → T`                                                      |
| P16     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N16 → T`                                                      |
| P17     | `A → N7 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 (Não) → N21 (Sim) → N23 → EXIT → EXIT_GLOBAL`            |
| P18     | `A → N7 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 (Sim) → N21 (Não) → N22 (Sim) → N23 → EXIT → EXIT_GLOBAL`      |
| P19     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL`             |
| P20     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL`       |
| P21     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL` |
| P22     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 → N21 → N23 → N24 → EXIT → EXIT_GLOBAL`       |
| P23     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL` |
| P24     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 → N21 → N22 → N23 → EXIT → EXIT_GLOBAL`       |
| P25     | `A → N7 → N8 → N9 → N10 → N11 → N12 → N15 → N18 → N19 → N20 → N21 → N22 → N23 → N24 → EXIT → EXIT_GLOBAL` |

## 4. Demonstração incremental da independência

A independência pode ser demonstrada pela introdução de uma aresta nova em cada caminho.

| Caminho | Aresta exclusiva introduzida                  |
| ------- | --------------------------------------------- |
| P1      | Caminho-base                                  |
| P2      | N1 → E_NPE                                    |
| P3      | N2 → E_NPE                                    |
| P4      | N3 → E_NPE                                    |
| P5      | N4 → E_IAE                                    |
| P6      | N5 → E_IAE                                    |
| P7      | N6 → E_IAE                                    |
| P8      | N7 → N9                                       |
| P9      | N8 → N9                                       |
| P10     | N9 → N12                                      |
| P11     | N10 → N12                                     |
| P12     | N11 → N12                                     |
| P13     | N12 → N13                                     |
| P14     | N13 → N14                                     |
| P15     | N14 → N18                                     |
| P16     | N15 → N16                                     |
| P17     | N20 (Não) → N21 (Sim)                         |
| P18     | N21 (Não) → N22 (Sim)                         |
| P19     | N18 → N21                                     |
| P20     | N19 → N21                                     |
| P21     | N20 → N21                                     |
| P22     | N21 → N23                                     |
| P23     | N22 → N23                                     |
| P24     | N23 → EXIT                                    |
| P25     | N24 → EXIT                                    |

## 5. Mapeamento Caminho → Entradas → Resultado Esperado

| Caminho | Entradas (`categoria`, `nivelCliente`, `diarias`, `km`, `atraso`, `seguro`, `atrasoAnt`) | Resultado Esperado         |
| ------- | ---------------------------------------------------------------------------------------- | -------------------------- |
| P1      | `(null, COMUM, 1, 0, 0, SEM_SEGURO, false)`                                              | `NullPointerException`     |
| P2      | `(ECONOMICO, null, 1, 0, 0, SEM_SEGURO, false)`                                          | `NullPointerException`     |
| P3      | `(ECONOMICO, COMUM, 1, 0, 0, null, false)`                                               | `NullPointerException`     |
| P4      | `(ECONOMICO, COMUM, 0, 0, 0, SEM_SEGURO, false)`                                         | `IllegalArgumentException` |
| P5      | `(ECONOMICO, COMUM, 1, -1, 0, SEM_SEGURO, false)`                                        | `IllegalArgumentException` |
| P6      | `(ECONOMICO, COMUM, 1, 0, -1, SEM_SEGURO, false)`                                        | `IllegalArgumentException` |
| P7      | `(ECONOMICO, COMUM, 2, 200, 0, SEM_SEGURO, false)`                                       | `240.00`                   |
| P8      | `(INTERMEDIARIO, COMUM, 2, 200, 0, SEM_SEGURO, false)`                                   | `360.00`                   |
| P9      | `(SUV, COMUM, 2, 200, 0, SEM_SEGURO, false)`                                             | `560.00`                   |
| P10     | `(ECONOMICO, COMUM, 5, 500, 0, SEM_SEGURO, false)`                                       | `570.00`                   |
| P11     | `(ECONOMICO, COMUM, 10, 1000, 0, SEM_SEGURO, false)`                                     | `1080.00`                  |
| P12     | `(ECONOMICO, COMUM, 15, 1500, 0, SEM_SEGURO, false)`                                     | `1530.00`                  |
| P13     | `(ECONOMICO, PRATA, 7, 700, 0, SEM_SEGURO, false)`                                       | `718.20`                   |
| P14     | `(ECONOMICO, OURO, 5, 500, 0, SEM_SEGURO, false)`                                        | `513.00`                   |
| P15     | `(ECONOMICO, COMUM, 2, 250, 0, SEM_SEGURO, false)`                                       | `280.00`                   |
| P16     | `(ECONOMICO, COMUM, 2, 400, 0, SEM_SEGURO, false)`                                       | `440.00`                   |
| P17     | `(ECONOMICO, COMUM, 2, 600, 0, SEM_SEGURO, false)`                                       | `840.00`                   |
| P18     | `(ECONOMICO, COMUM, 2, 200, 0, BASICO, false)`                                           | `290.00`                   |
| P19     | `(ECONOMICO, COMUM, 2, 200, 0, OUTRO_SEGURO, false)`                                     | `330.00`                   |
| P20     | `(ECONOMICO, PRATA, 5, 200, 0, SEM_SEGURO, false)`                                       | `570.00`                   |
| P21     | `(ECONOMICO, PRATA, 7, 200, 0, SEM_SEGURO, true)`                                        | `756.00`                   |
| P22     | `(ECONOMICO, OURO, 2, 200, 0, SEM_SEGURO, false)`                                        | `240.00`                   |
| P23     | `(ECONOMICO, OURO, 5, 200, 0, SEM_SEGURO, true)`                                         | `570.00`                   |
| P24     | `(ECONOMICO, COMUM, 2, 200, 2, SEM_SEGURO, false)`                                       | `288.00`                   |
| P25     | `(ECONOMICO, COMUM, 2, 200, 4, SEM_SEGURO, false)`                                       | `360.00`                   |