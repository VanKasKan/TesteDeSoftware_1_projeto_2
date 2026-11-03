# Relação entre Arestas e Casos de Teste (CT01 a CT13)


## Validações iniciais

| Aresta | Casos de teste |
| --- | --- |
| `START([INÍCIO: calcularCustoLocacao]) -> N1{categoria == null?}` | CT01–CT13 |
| `N1{categoria == null?} -- Sim --> E_NPE([SAÍDA: NullPointerException])` | CT01 |
| `N1{categoria == null?} -- Não --> N2{nivelCliente == null?}` | CT02–CT13 |
| `N2{nivelCliente == null?} -- Sim --> E_NPE([SAÍDA: NullPointerException])` | CT02 |
| `N2{nivelCliente == null?} -- Não --> N3{seguro == null?}` | CT03–CT13 |
| `N3{seguro == null?} -- Sim --> E_NPE([SAÍDA: NullPointerException])` | CT03 |
| `N3{seguro == null?} -- Não --> N4{numeroDiarias <= 0?}` | CT04–CT13 |
| `N4{numeroDiarias <= 0?} -- Sim --> E_IAE([SAÍDA: IllegalArgumentException])` | CT04 |
| `N4{numeroDiarias <= 0?} -- Não --> N5{quilometrosRodados < 0?}` | CT05–CT13 |
| `N5{quilometrosRodados < 0?} -- Sim --> E_IAE([SAÍDA: IllegalArgumentException])` | CT05 |
| `N5{quilometrosRodados < 0?} -- Não --> N6{horasAtraso < 0?}` | CT06–CT13 |
| `N6{horasAtraso < 0?} -- Sim --> E_IAE([SAÍDA: IllegalArgumentException])` | CT06 |
| `N6{horasAtraso < 0?} -- Não --> N7{categoria == ECONOMICO?}` | CT07–CT13 |

---

## Categoria do veículo

| Aresta | Casos de teste |
| --- | --- |
| `N7{categoria == ECONOMICO?} -- Sim --> P1[valorDiaria = 120.00]` | CT07, CT10, CT11, CT12, CT13 |
| `N7{categoria == ECONOMICO?} -- Não --> N8{categoria == INTERMEDIARIO?}` | CT08, CT09 |
| `N8{categoria == INTERMEDIARIO?} -- Sim --> P2[valorDiaria = 180.00]` | CT08 |
| `N8{categoria == INTERMEDIARIO?} -- Não --> P3[valorDiaria = 280.00]` | CT09 |
| `P1[valorDiaria = 120.00] -> N9{numeroDiarias <= 2?}` | CT07, CT10, CT11, CT12, CT13 |
| `P2[valorDiaria = 180.00] -> N9{numeroDiarias <= 2?}` | CT08 |
| `P3[valorDiaria = 280.00] -> N9{numeroDiarias <= 2?}` | CT09 |

---

## Taxa de duração

| Aresta | Casos de teste |
| --- | --- |
| `N9{numeroDiarias <= 2?} -- Sim --> P4[taxaDuracao = 0]` | CT09, CT13 |
| `N9{numeroDiarias <= 2?} -- Não --> N10{numeroDiarias <= 6?}` | CT07, CT08, CT10, CT11, CT12 |
| `N10{numeroDiarias <= 6?} -- Sim --> P5[taxaDuracao = 0.05]` | CT08, CT11, CT12 |
| `N10{numeroDiarias <= 6?} -- Não --> N11{numeroDiarias <= 14?}` | CT07, CT10 |
| `N11{numeroDiarias <= 14?} -- Sim --> P6[taxaDuracao = 0.10]` | CT07 |
| `N11{numeroDiarias <= 14?} -- Não --> P7[taxaDuracao = 0.15]` | CT10 |
| `P4[taxaDuracao = 0] -> P8[Calcula valorDiariasComDesconto]` | CT09, CT13 |
| `P5[taxaDuracao = 0.05] -> P8[Calcula valorDiariasComDesconto]` | CT08, CT11, CT12 |
| `P6[taxaDuracao = 0.10] -> P8[Calcula valorDiariasComDesconto]` | CT07 |
| `P7[taxaDuracao = 0.15] -> P8[Calcula valorDiariasComDesconto]` | CT10 |
| `P8[Calcula valorDiariasComDesconto] -> N12{nivel == PRATA?}` | CT07–CT13 |

---

## Desconto de fidelidade

| Aresta | Casos de teste |
| --- | --- |
| `N12{nivel == PRATA?} -- Sim --> N13{diarias >= 7?}` | CT07, CT10, CT13 |
| `N12{nivel == PRATA?} -- Não --> N15{nivel == OURO?}` | CT08, CT09, CT11, CT12 |
| `N13{diarias >= 7?} -- Sim --> N14{!possuiAtrasoAnterior?}` | CT07, CT10 |
| `N13{diarias >= 7?} -- Não --> N15{nivel == OURO?}` | CT13 |
| `N14{!possuiAtrasoAnterior?} -- Sim --> P9[taxaFidelidade = 0.05]` | CT07 |
| `N14{!possuiAtrasoAnterior?} -- Não --> N15{nivel == OURO?}` | CT10 |
| `N15{nivel == OURO?} -- Sim --> N16{diarias >= 5?}` | CT08, CT11, CT12 |
| `N15{nivel == OURO?} -- Não --> P11[taxaFidelidade = 0]` | CT09, CT10, CT13 |
| `N16{diarias >= 5?} -- Sim --> N17{!possuiAtrasoAnterior?}` | CT08, CT12 |
| `N16{diarias >= 5?} -- Não --> P11[taxaFidelidade = 0]` | CT11 |
| `N17{!possuiAtrasoAnterior?} -- Sim --> P10[taxaFidelidade = 0.10]` | CT08 |
| `N17{!possuiAtrasoAnterior?} -- Não --> P11[taxaFidelidade = 0]` | CT12 |
| `P9[taxaFidelidade = 0.05] -> P12[Calcula valorFinalDiarias e Franquia]` | CT07 |
| `P10[taxaFidelidade = 0.10] -> P12[Calcula valorFinalDiarias e Franquia]` | CT08 |
| `P11[taxaFidelidade = 0] -> P12[Calcula valorFinalDiarias e Franquia]` | CT09, CT10, CT11, CT12, CT13 |
| `P12[Calcula valorFinalDiarias e Franquia] -> N18{excedente <= 0?}` | CT07–CT13 |

---

## Custo de quilometragem

| Aresta | Casos de teste |
| --- | --- |
| `N18{excedente <= 0?} -- Sim --> P13[custoQuilometragem = 0]` | CT07, CT11, CT12 |
| `N18{excedente <= 0?} -- Não --> N19{excedente <= 100?}` | CT08, CT09, CT10, CT13 |
| `N19{excedente <= 100?} -- Sim --> P14[tarifa = 0.80]` | CT13  |
| `N19{excedente <= 100?} -- Não --> N20{excedente <= 300?}` | CT08, CT09, CT10 |
| `N20{excedente <= 300?} -- Sim --> P15[tarifa = 1.00]` | CT08, CT09 |
| `N20{excedente <= 300?} -- Não --> P16[tarifa = 1.50]` | CT10 |
| `P14[tarifa = 0.80] -> P17[Calcula custoQuilometragem]` | CT13  |
| `P15[tarifa = 1.00] -> P17[Calcula custoQuilometragem]` | CT08, CT09 |
| `P16[tarifa = 1.50] -> P17[Calcula custoQuilometragem]` | CT10 |
| `P13[custoQuilometragem = 0] -> N21{seguro == SEM_SEGURO?}` | CT07, CT11, CT12 |
| `P17[Calcula custoQuilometragem] -> N21{seguro == SEM_SEGURO?}` | CT08, CT09, CT10, CT13 |

---

## Seguro

| Aresta | Casos de teste |
| --- | --- |
| `N21{seguro == SEM_SEGURO?} -- Sim --> P18[valorSeguroDiaria = 0]` | CT07, CT10, CT11, CT12, CT13 |
| `N21{seguro == SEM_SEGURO?} -- Não --> N22{seguro == BASICO?}` | CT08, CT09 |
| `N22{seguro == BASICO?} -- Sim --> P19[valorSeguroDiaria = 25.00]` | CT08 |
| `N22{seguro == BASICO?} -- Não --> P20[valorSeguroDiaria = 45.00]` | CT09 |
| `P18[valorSeguroDiaria = 0] -> P21[Calcula custoSeguro]` | CT07, CT10, CT11, CT12, CT13 |
| `P19[valorSeguroDiaria = 25.00] -> P21[Calcula custoSeguro]` | CT08 |
| `P20[valorSeguroDiaria = 45.00] -> P21[Calcula custoSeguro]` | CT09 |
| `P21[Calcula custoSeguro] -> N23{horasAtraso <= 1?}` | CT07–CT13 |

---

## Custo de atraso

| Aresta | Casos de teste |
| --- | --- |
| `N23{horasAtraso <= 1?} -- Sim --> P22[custoAtraso = 0]` | CT07, CT10, CT11, CT12, CT13 |
| `N23{horasAtraso <= 1?} -- Não --> N24{horasAtraso <= 3?}` | CT08, CT09 |
| `N24{horasAtraso <= 3?} -- Sim --> P23["custoAtraso = 20% diária * horas"]` | CT08 |
| `N24{horasAtraso <= 3?} -- Não --> P24[custoAtraso = 1 diária]` | CT09 |
| `P22[custoAtraso = 0] -> P25[Soma Total e return]` | CT07, CT10, CT11, CT12, CT13 |
| `P23["custoAtraso = 20% diária * horas"] -> P25[Soma Total e return]` | CT08 |
| `P24[custoAtraso = 1 diária] -> P25[Soma Total e return]` | CT09 |
| `P25[Soma Total e return] -> EXIT([FIM: Retorna Total])` | CT07–CT13 |

---

### Resumo de Cobertura

* **Total de arestas:** 74
* **Arestas cobertas:** 74 (100,0%)