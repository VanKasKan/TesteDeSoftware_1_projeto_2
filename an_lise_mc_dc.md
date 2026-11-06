# Análise de Cobertura MC/DC (Modified Condition/Decision Coverage)
**Pacote:** `br.com.aluguelcarros`  
**Classe sob Teste:** `CalculadoraCustoLocacao`

---

## 1. Decisão 1 (D1): Validação de Parâmetros Nulos

### 1.1 Transcrição da Expressão
```java
if (categoria == null || nivelCliente == null || seguro == null)
```

* **Condição A:** `categoria == null`
* **Condição B:** `nivelCliente == null`
* **Condição C:** `seguro == null`
* **Expressão Lógica ($D_1$):** $A \lor B \lor C$

---

### 1.2 Tabela de Verdade e Curto-Circuito no Runtime

Em Java, o operador `||` realiza a avaliação com curto-circuito (Lazy Evaluation): caso uma condição seja `true`, as condições à direita **não são avaliadas** pelo interpretador.

| Caso | Entrada Concreta (`categoria`, `nivelCliente`, `seguro`) | $A$ (Val) | $B$ (Val) | $C$ (Val) | $A$ (Efet) | $B$ (Efet) | $C$ (Efet) | Resultado $D_1$ | Ação / Exceção Esperada |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **C1.1** | `(null, COMUM, SEM_SEGURO)` | T | F | F | **T** | *N/A* | *N/A* | **T** | Lança `NullPointerException` |
| **C1.2** | `(ECONOMICO, null, SEM_SEGURO)` | F | T | F | **F** | **T** | *N/A* | **T** | Lança `NullPointerException` |
| **C1.3** | `(ECONOMICO, COMUM, null)` | F | F | T | **F** | **F** | **T** | **T** | Lança `NullPointerException` |
| **C1.4** | `(ECONOMICO, COMUM, SEM_SEGURO)` | F | F | F | **F** | **F** | **F** | **F** | Execução normal prossegue |

*Legenda:* **Val** = Valor lógico atribuído aos parâmetros | **Efet** = Valor efetivamente avaliado em runtime (*N/A* = Curto-circuitada).

---

### 1.3 Pares de Independência de Causa Única ($D_1$)

* **Condição A (`categoria == null`): Par `{C1.1, C1.4}`**
  * Mantendo $B=\text{F}$ e $C=\text{F}$ fixos:
  * Em **C1.1**, $A=\text{T} \implies D_1=\text{T}$ (Lança `NullPointerException`).
  * Em **C4**, $A=\text{F} \implies D_1=\text{F}$ (Fluxo prossegue).
* **Condição B (`nivelCliente == null`): Par `{C1.2, C1.4}`**
  * Mantendo $A=\text{F}$ e $C=\text{F}$ fixos:
  * Em **C1.2**, $B=\text{T} \implies D_1=\text{T}$ (Lança `NullPointerException`).
  * Em **C4**, $B=\text{F} \implies D_1=\text{F}$ (Fluxo prossegue).
* **Condição C (`seguro == null`): Par `{C1.3, C1.4}`**
  * Mantendo $A=\text{F}$ e $B=\text{F}$ fixos:
  * Em **C1.3**, $C=\text{T} \implies D_1=\text{T}$ (Lança `NullPointerException`).
  * Em **C4**, $C=\text{F} \implies D_1=\text{F}$ (Fluxo prossegue).

---

## 2. Decisão 2 (D2): Desconto de Fidelidade Prata

### 2.1 Transcrição da Expressão
```java
else if (nivelCliente == NivelCliente.PRATA && numeroDiarias >= 7 && !possuiAtrasoAnterior)
```

* **Condição A:** `nivelCliente == NivelCliente.PRATA`
* **Condição B:** `numeroDiarias >= 7`
* **Condição C:** `!possuiAtrasoAnterior` (ou seja, `possuiAtrasoAnterior == false`)
* **Expressão Lógica ($D_2$):** $A \land B \land C$

---

### 2.2 Tabela de Verdade e Curto-Circuito no Runtime

Para o operador `&&`, a avaliação ocorre da esquerda para a direita. O curto-circuito interrompe a execução assim que qualquer condição avalia para `false`. Como $D_2$ se encontra no começo de um `else if`, ela não depende das demais condições da estrutura para ser atingida, no entanto ainda é necessário passar sem exceções pelas decisões N1-N6 relativas a entradas inválidas. É o caso para os testes considerados, em todos verifica-se que: `categoria != null`,`nivelCliente != null`,`seguro != null`,`numeroDiarias > 0`,`quilometrosRodados => 0`,`horasAtraso => 0`.

| Caso | Entrada Concreta (`nivelCliente`, `numeroDiarias`, `possuiAtrasoAnterior`) | $A$ (Val) | $B$ (Val) | $C$ (Val) | $A$ (Efet) | $B$ (Efet) | $C$ (Efet) | Resultado $D_2$ | Desconto Fidelidade |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **C2.1** | `(PRATA, 7, false)` | T | T | T | **T** | **T** | **T** | **T** | Aplica 5% |
| **C2.2** | `(COMUM, 7, false)` | F | T | T | **F** | *N/A* | *N/A* | **F** | 0% (Cai no `else`) |
| **C2.3** | `(PRATA, 6, false)` | T | F | T | **T** | **F** | *N/A* | **F** | 0% (Sem desconto) |
| **C2.4** | `(PRATA, 7, true)` | T | T | F | **T** | **T** | **F** | **F** | 0% (Sem desconto) |

---

### 2.3 Pares de Independência de Causa Única ($D_2$)

* **Condição A (`nivelCliente == PRATA`): Par `{C2.1, C2.2}`**
  * Mantendo $B=\text{T}$ e $C=\text{T}$ fixos:
  * Em **C2.1**, $A=\text{T} \implies D_2=\text{T}$ (Retorna `718.20`).
  * Em **C2.2**, $A=\text{F} \implies D_2=\text{F}$ (Retorna `756.00`).
* **Condição B (`numeroDiarias >= 7`): Par `{C2.1, C2.3}`**
  * Mantendo $A=\text{T}$ e $C=\text{T}$ fixos:
  * Em **C2.1**, $B=\text{T} \implies D_2=\text{T}$ (Retorna `718.20`).
  * Em **C2.3**, $B=\text{F} \implies D_2=\text{F}$ (Retorna `684.00`).
* **Condição C (`!possuiAtrasoAnterior`): Par `{C2.1, C2.4}`**
  * Mantendo $A=\text{T}$ e $B=\text{T}$ fixos:
  * Em **C2.1**, $C=\text{T} \implies D_2=\text{T}$ (Retorna `718.20`).
  * Em **C2.4**, $C=\text{F} \implies D_2=\text{F}$ (Retorna `756.00`).