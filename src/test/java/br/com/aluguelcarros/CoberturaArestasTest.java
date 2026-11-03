package br.com.aluguelcarros;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CoberturaArestasTest
{
    private final CalculadoraCustoLocacao calculadora = new CalculadoraCustoLocacao();

    @Test
    void deveLancarNullPointerExceptionQuandoCategoriaForNula()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            null,
            NivelCliente.COMUM,
            -1,
            -50,
            -2,
            TipoSeguro.SEM_SEGURO,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void deveLancarNullPointerExceptionQuandoNivelClienteForNulo()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            null,
            0,
            -10,
            0,
            TipoSeguro.BASICO,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void deveLancarNullPointerExceptionQuandoSeguroForNulo()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.INTERMEDIARIO,
            NivelCliente.PRATA,
            5,
            100,
            1,
            null,
            false
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    void deveLancarIllegalArgumentExceptionQuandoDiariasForemZero()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            0,
            100,
            0,
            TipoSeguro.BASICO,
            false
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveLancarIllegalArgumentExceptionQuandoKmRodadosForemNegativos()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            3,
            -1,
            0,
            TipoSeguro.BASICO,
            false
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveLancarIllegalArgumentExceptionQuandoHorasAtrasoForemNegativas()
    {
        assertThatThrownBy(() -> calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            3,
            100,
            -1,
            TipoSeguro.BASICO,
            false
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveCalcularLocacaoEconomicaComDescontoPrata()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            7,
            700,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("718.20"));
    }

    @Test
    void deveCalcularLocacaoIntermediariaComSeguroBasicoEAtraso()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.INTERMEDIARIO,
            NivelCliente.OURO,
            5,
            700,
            2,
            TipoSeguro.BASICO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1166.50"));
    }

    @Test
    void deveCalcularLocacaoSUVComSeguroCompletoEAtrasoSuperiorATresHoras()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.SUV,
            NivelCliente.COMUM,
            2,
            450,
            5,
            TipoSeguro.COMPLETO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1180.00"));
    }

    @Test
    void deveCalcularLocacaoEconomicaSemDescontoFidelidadePorAtrasoAnterior()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            15,
            2000,
            0,
            TipoSeguro.SEM_SEGURO,
            true
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("2280.00"));
    }

    @Test
    void deveCalcularLocacaoEconomicaSemDescontoOuroPorDiariasInsuficientes()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.OURO,
            4,
            400,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("456.00"));
    }

    @Test
    void deveCalcularLocacaoEconomicaSemDescontoOuroPorAtrasoAnterior()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.OURO,
            6,
            600,
            0,
            TipoSeguro.SEM_SEGURO,
            true
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("684.00"));
    }

    @Test
    void deveCalcularLocacaoEconomicaSemDescontoPrataoPorDiariasInsuficientes()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            2,
            250,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("280.00"));
    }
}