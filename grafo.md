flowchart TD
START([START: Entrada]) --> N1{categoria == null}

%% Validação Nulos (Curto-circuito do ||)
N1 -- Sim --> E1[E1: throw NullPointerException]
N1 -- Não --> N2{nivelCliente == null}
N2 -- Sim --> E1
N2 -- Não --> N3{seguro == null}
N3 -- Sim --> E1
E1 --> THROW_EX([THROW_EX: Saída por Exceção])

%% Validação Parâmetros Inválidos
N3 -- Não --> N4{numeroDiarias <= 0}
N4 -- Sim --> E2[E2: throw IllegalArgumentException] --> THROW_EX
N4 -- Não --> N5{quilometrosRodados < 0}
N5 -- Sim --> E3[E3: throw IllegalArgumentException] --> THROW_EX
N5 -- Não --> N6{horasAtraso < 0}
N6 -- Sim --> E4[E4: throw IllegalArgumentException] --> THROW_EX

%% Categoria Veículo
N6 -- Não --> N7{categoria == ECONOMICO}
N7 -- Sim --> P1[P1: valorDiaria = 120]
N7 -- Não --> N8{categoria == INTERMEDIARIO}
N8 -- Sim --> P2[P2: valorDiaria = 180]
N8 -- Não --> P3[P3: valorDiaria = 280]

%% Desconto Duração
P1 --> N9{numeroDiarias <= 2}
P2 --> N9
P3 --> N9
N9 -- Sim --> P4[P4: taxaDuracao = 0]
N9 -- Não --> N10{numeroDiarias <= 6}
N10 -- Sim --> P5[P5: taxaDuracao = 0.05]
N10 -- Não --> N11{numeroDiarias <= 14}
N11 -- Sim --> P6[P6: taxaDuracao = 0.10]
N11 -- Não --> P7[P7: taxaDuracao = 0.15]

%% Cálculo valorDiariasComDesconto
P4 --> P8[P8: valorDiariasComDesconto]
P5 --> P8
P6 --> P8
P7 --> P8

%% Desconto Fidelidade (Curto-circuito do &&)
P8 --> N12{nivelCliente == PRATA}
N12 -- Sim --> N13{numeroDiarias >= 7}
N13 -- Sim --> N14{!possuiAtrasoAnterior}
N14 -- Sim --> P9[P9: taxaFidelidade = 0.05]

N12 -- Não --> N15{nivelCliente == OURO}
N13 -- Não --> N15
N14 -- Não --> N15

N15 -- Sim --> N16{numeroDiarias >= 5}
N16 -- Sim --> N17{!possuiAtrasoAnterior}
N17 -- Sim --> P10[P10: taxaFidelidade = 0.10]

N15 -- Não --> P11[P11: taxaFidelidade = 0]
N16 -- Não --> P11
N17 -- Não --> P11

%% Franquia e Quilometragem
P9 --> P12[P12: franquia e excedente]
P10 --> P12
P11 --> P12

P12 --> N18{quilometrosExcedentes <= 0}
N18 -- Sim --> P13[P13: custoQuilometragem = 0]
N18 -- Não --> N19{quilometrosExcedentes <= 100}
N19 -- Sim --> P14[P14: tarifa = 0.80] --> P17[P17: custoQuilometragem]
N19 -- Não --> N20{quilometrosExcedentes <= 300}
N20 -- Sim --> P15[P15: tarifa = 1.00] --> P17
N20 -- Não --> P16[P16: tarifa = 1.50] --> P17

%% Seguro
P13 --> N21{seguro == SEM_SEGURO}
P17 --> N21
N21 -- Sim --> P18[P18: valorSeguroDiaria = 0]
N21 -- Não --> N22{seguro == BASICO}
N22 -- Sim --> P19[P19: valorSeguroDiaria = 25]
N22 -- Não --> P20[P20: valorSeguroDiaria = 45]

%% Atraso
P18 --> P21[P21: custoSeguro]
P19 --> P21
P20 --> P21
P21 --> N23{horasAtraso <= 1}
N23 -- Sim --> P22[P22: custoAtraso = 0]
N23 -- Não --> N24{horasAtraso <= 3}
N24 -- Sim --> P23[P23: custoAtraso parcial]
N24 -- Não --> P24[P24: custoAtraso = valorDiaria]

%% Fechamento
P22 --> P25[P25: total e return]
P23 --> P25
P24 --> P25
P25 --> EXIT([EXIT: Retorno Normal])