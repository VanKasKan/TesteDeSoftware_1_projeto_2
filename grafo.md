# Grafo de Fluxo de Controle (CFG) - `calcularCustoLocacao`

```
graph TD
    START([INÍCIO: calcularCustoLocacao])
    EXIT([FIM: Retorna Total])
    E_NPE([SAÍDA: NullPointerException])
    E_IAE([SAÍDA: IllegalArgumentException])

    N1{categoria == null?}
    N2{nivelCliente == null?}
    N3{seguro == null?}
    N4{numeroDiarias <= 0?}
    N5{quilometrosRodados < 0?}
    N6{horasAtraso < 0?}
    N7{categoria == ECONOMICO?}
    N8{categoria == INTERMEDIARIO?}
    P1[valorDiaria = 120.00]
    P2[valorDiaria = 180.00]
    P3[valorDiaria = 280.00]
    N9{numeroDiarias <= 2?}
    N10{numeroDiarias <= 6?}
    N11{numeroDiarias <= 14?}
    P4[taxaDuracao = 0]
    P5[taxaDuracao = 0.05]
    P6[taxaDuracao = 0.10]
    P7[taxaDuracao = 0.15]
    P8[Calcula valorDiariasComDesconto]
    N12{nivel == PRATA?}
    N13{diarias >= 7?}
    N14{!possuiAtrasoAnterior?}
    N15{nivel == OURO?}
    N16{diarias >= 5?}
    N17{!possuiAtrasoAnterior?}
    P9[taxaFidelidade = 0.05]
    P10[taxaFidelidade = 0.10]
    P11[taxaFidelidade = 0]
    P12[Calcula valorFinalDiarias e Franquia]
    N18{excedente <= 0?}
    N19{excedente <= 100?}
    N20{excedente <= 300?}
    P13[custoQuilometragem = 0]
    P14[tarifa = 0.80]
    P15[tarifa = 1.00]
    P16[tarifa = 1.50]
    P17[Calcula custoQuilometragem]
    N21{seguro == SEM_SEGURO?}
    N22{seguro == BASICO?}
    P18[valorSeguroDiaria = 0]
    P19[valorSeguroDiaria = 25.00]
    P20[valorSeguroDiaria = 45.00]
    P21[Calcula custoSeguro]
    N23{horasAtraso <= 1?}
    N24{horasAtraso <= 3?}
    P22[custoAtraso = 0]
    P23["custoAtraso = 20% diária * horas"]
    P24[custoAtraso = 1 diária]
    P25[Soma Total e return]

    START --> N1
    N1 -->|Sim| E_NPE
    N1 -->|Não| N2
    N2 -->|Sim| E_NPE
    N2 -->|Não| N3
    N3 -->|Sim| E_NPE
    N3 -->|Não| N4
    N4 -->|Sim| E_IAE
    N4 -->|Não| N5
    N5 -->|Sim| E_IAE
    N5 -->|Não| N6
    N6 -->|Sim| E_IAE
    N6 -->|Não| N7
    N7 -->|Sim| P1
    N7 -->|Não| N8
    N8 -->|Sim| P2
    N8 -->|Não| P3
    P1 --> N9
    P2 --> N9
    P3 --> N9
    N9 -->|Sim| P4
    N9 -->|Não| N10
    N10 -->|Sim| P5
    N10 -->|Não| N11
    N11 -->|Sim| P6
    N11 -->|Não| P7
    P4 --> P8
    P5 --> P8
    P6 --> P8
    P7 --> P8
    P8 --> N12
    N12 -->|Sim| N13
    N13 -->|Sim| N14
    N14 -->|Sim| P9
    N12 -->|Não| N15
    N13 -->|Não| N15
    N14 -->|Não| N15
    N15 -->|Sim| N16
    N16 -->|Sim| N17
    N17 -->|Sim| P10
    N15 -->|Não| P11
    N16 -->|Não| P11
    N17 -->|Não| P11
    P9 --> P12
    P10 --> P12
    P11 --> P12
    P12 --> N18
    N18 -->|Sim| P13
    N18 -->|Não| N19
    N19 -->|Sim| P14
    P14 --> P17
    N19 -->|Não| N20
    N20 -->|Sim| P15
    P15 --> P17
    N20 -->|Não| P16
    P16 --> P17
    P13 --> N21
    P17 --> N21
    N21 -->|Sim| P18
    N21 -->|Não| N22
    N22 -->|Sim| P19
    N22 -->|Não| P20
    P18 --> P21
    P19 --> P21
    P20 --> P21
    P21 --> N23
    N23 -->|Sim| P22
    N23 -->|Não| N24
    N24 -->|Sim| P23
    N24 -->|Não| P24
    P22 --> P25
    P23 --> P25
    P24 --> P25
    P25 --> EXIT

```