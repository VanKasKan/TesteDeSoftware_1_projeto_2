package br.com.aluguelcarros;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class McdcTest
{
    private final CalculadoraCustoLocacao calculadora = new CalculadoraCustoLocacao(); 

    // =========================================================================
    // DECISÃO 1 (D1): (categoria == null || nivelCliente == null || seguro == null)
    // =========================================================================

    @Test
    @DisplayName("D1 - Par A: A=True (categoria == null). Dispara curto-circuito em D1 e lança NPE.")
    void testD1_CondicaoA_True()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            null,
            NivelCliente.COMUM,
            1,
            0,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("D1 - Par B: A=False, B=True (nivelCliente == null). Dispara curto-circuito em B e lança NPE.")
    void testD1_CondicaoB_True()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            null,
            1,
            0,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("D1 - Par C: A=False, B=False, C=True (seguro == null). Avalia C e lança NPE.")
    void testD1_CondicaoC_True()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            1,
            0,
            0,
            null,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("D1 - Caso Base Falso: A=False, B=False, C=False. Permite que o fluxo prossiga normalmente.")
    void testD1_TodasFalsas()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            1,
            0,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo("120.00");
    }

    // =========================================================================
    // DECISÃO 2 (D2): (nivelCliente == PRATA && numeroDiarias >= 7 && !possuiAtrasoAnterior)
    // =========================================================================

    @Test
    @DisplayName("D2 - Caso Base Verdadeiro: A=T, B=T, C=T. Aplica desconto Fidelidade Prata (5%).")
    void testD2_TodasVerdadeiras()
    {
        // 7 diarias * 120 = 840. Desc duracao 10% (756.00). Desc fidelidade 5% (718.20).
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            7,
            700,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo("718.20");
    }

    @Test
    @DisplayName("D2 - Par A: A=False (nivelCliente != PRATA). Curto-circuita em A e não aplica desc. Prata.")
    void testD2_CondicaoA_Falsa()
    {
        // Nivel COMUM (A=False). 7 diarias * 120 = 840. Desc duracao 10% (756.00). Sem desc fidelidade.
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            7,
            700,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo("756.00");
    }

    @Test
    @DisplayName("D2 - Par B: A=True, B=False (numeroDiarias < 7). Curto-circuita em B e não aplica desc. Prata.")
    void testD2_CondicaoB_Falsa()
    {
        // 6 diarias (B=False). 6 * 120 = 720. Desc duracao 5% (684.00). Sem desc fidelidade.
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            6,
            600,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo("684.00");
    }

    @Test
    @DisplayName("D2 - Par C: A=True, B=True, C=False (possuiAtrasoAnterior = true). Avalia C e não aplica desc. Prata.")
    void testD2_CondicaoC_Falsa()
    {
        // possuiAtrasoAnterior = true (C=False). 7 * 120 = 840. Desc duracao 10% (756.00). Sem desc fidelidade.
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            7,
            700,
            0,
            TipoSeguro.SEM_SEGURO,
            true
        );

        assertThat(resultado).isEqualByComparingTo("756.00");
    }
}