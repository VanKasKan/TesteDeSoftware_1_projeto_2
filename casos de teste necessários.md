# Suíte de Testes para `calcularCustoLocacao`

Esta suíte garante 100% de cobertura de instruções e arestas (Edge e Branch Coverage) no Grafo de Fluxo de Controle, respeitando o curto-circuito de operadores booleanos, a precedência de exceções (`NullPointerException` > `IllegalArgumentException`) e o cálculo exato dos custos e descontos.

---

## Tabela de Casos de Teste com Resultados Calculados

| ID | `categoria` | `nivelCliente` | `diarias` | `kmRodados` | `horasAtraso` | `seguro` | `atrasoAnterior` | Resultado Esperado | Detalhamento dos Cálculos |
| :---: | :--- | :--- | :---: | :---: | :---: | :--- | :---: | :---: | :--- |
| **CT01** | `null` | `BRONZE` | `-1` | `-50` | `-2` | `SEM_SEGURO` | `false` | **Lança `NullPointerException`** | Dispara na 1ª checagem (`categoria == null`), priorizando sobre argumentos inválidos. |
| **CT02** | `ECONOMICO` | `null` | `0` | `-10` | `0` | `BASICO` | `false` | **Lança `NullPointerException`** | Dispara na 2ª checagem (`nivelCliente == null`), priorizando sobre `numeroDiarias <= 0`. |
| **CT03** | `INTERMEDIARIO` | `PRATA` | `5` | `100` | `1` | `null` | `false` | **Lança `NullPointerException`** | Dispara na 3ª checagem (`seguro == null`). |
| **CT04** | `ECONOMICO` | `BRONZE` | `0` | `100` | `0` | `BASICO` | `false` | **Lança `IllegalArgumentException`** | Dispara no parâmetro inválido `numeroDiarias <= 0`. |
| **CT05** | `ECONOMICO` | `BRONZE` | `3` | `-1` | `0` | `BASICO` | `false` | **Lança `IllegalArgumentException`** | Dispara no parâmetro inválido `quilometrosRodados < 0`. |
| **CT06** | `ECONOMICO` | `BRONZE` | `3` | `100` | `-1` | `BASICO` | `false` | **Lança `IllegalArgumentException`** | Dispara no parâmetro inválido `horasAtraso < 0`. |
| **CT07** | `ECONOMICO` | `PRATA` | `7` | `700` | `0` | `SEM_SEGURO` | `false` | **`BigDecimal("718.20")`** | • Diária: R\$ 120,00 * 7 = R\$ 840,00<br>• Desc. Duração (10%): R\$ 84,00 → R\$ 756,00<br>• Desc. Fidelidade Prata (5%): R\$ 37,80 → R\$ 718,20<br>• KM Excedente: 700 - 700 = 0 (R\$ 0)<br>• Seguro: R\$ 0<br>• Atraso: R\$ 0 |
| **CT08** | `INTERMEDIARIO` | `OURO` | `5` | `700` | `2` | `BASICO` | `false` | **`BigDecimal("1096.50")`** | • Diária: R\$ 180,00 * 5 = R\$ 900,00<br>• Desc. Duração (5%): R\$ 45,00 → R\$ 855,00<br>• Desc. Fidelidade Ouro (10%): R\$ 85,50 → R\$ 769,50<br>• KM Excedente: 700 - 500 = 200km (<=100 a R\$ 0,80) = R\$ 160,00<br>• Seguro Básico: R\$ 25,00 * 5 = R\$ 125,00<br>• Atraso (2h = 40% da diária): 180 * 0.40 = R\$ 72,00<br>• Total: 769.50 + 160.00 + 125.00 + 72.00 = 1096.50 |
| **CT09** | `PREMIUM` | `BRONZE` | `2` | `450` | `5` | `COMPLETO` | `false` | **`BigDecimal---`** *(calculado)*<br>**`BigDecimal("1160.00")`** | • Diária: R\$ 280,00 * 2 = R\$ 560,00<br>• Desc. Duração (0%): R\$ 0 → R\$ 560,00<br>• Desc. Fidelidade (Bronze = 0%): R\$ 560,00<br>• KM Excedente: 450 - 200 = 250km (<=300 a R\$ 1,00) = R\$ 250,00<br>• Seguro Completo: R\$ 45,00 * 2 = R\$ 90,00<br>• Atraso (>3h = 1 diária): R\$ 280,00<br>• Total: 560.00 + 250.00 + 90.00 + 280.00 = 1160.00 |
| **CT10** | `ECONOMICO` | `PRATA` | `15` | `2000` | `0` | `SEM_SEGURO` | `true` | **`BigDecimal("2280.00")`** | • Diária: R\$ 120,00 * 15 = R\$ 1800,00<br>• Desc. Duração (15%): R\$ 270,00 → R\$ 1530,00<br>• Desc. Fidelidade: 0% (falha por `atrasoAnterior = true`) → R\$ 1530,00<br>• KM Excedente: 2000 - 1500 = 500km (>300 a R\$ 1,50) = R\$ 750,00<br>• Seguro: R\$ 0 / Atraso: R\$ 0<br>• Total: 1530.00 + 750.00 = 2280.00 |
| **CT11** | `ECONOMICO` | `OURO` | `4` | `400` | `0` | `SEM_SEGURO` | `false` | **`BigDecimal("456.00")`** | • Diária: R\$ 120,00 * 4 = R\$ 480,00<br>• Desc. Duração (5%): R\$ 24,00 → R\$ 456,00<br>• Desc. Fidelidade: 0% (falha Ouro por `diarias < 5`) → R\$ 456,00<br>• KM Excedente: 400 - 400 = 0 (R\$ 0)<br>• Seguro: R\$ 0 / Atraso: R\$ 0 |
| **CT12** | `ECONOMICO` | `OURO` | `6` | `600` | `0` | `SEM_SEGURO` | `true` | **`BigDecimal("684.00")`** | • Diária: R\$ 120,00 * 6 = R\$ 720,00<br>• Desc. Duração (5%): R\$ 36,00 → R\$ 684,00<br>• Desc. Fidelidade: 0% (falha Ouro por `atrasoAnterior = true`) → R\$ 684,00<br>• KM Excedente: 600 - 600 = 0 (R\$ 0)<br>• Seguro: R\$ 0 / Atraso: R\$ 0 |