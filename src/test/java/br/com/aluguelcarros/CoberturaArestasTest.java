package br.com.aluguelcarros;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CoberturaArestasTest
{
    private final CalculadoraCustoLocacao calculadora = new CalculadoraCustoLocacao();
    //CT01
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
    //CT02
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
    //CT03
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
    //CT04
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
    //CT05
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
    //CT06
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
    //CT07
    @Test
    void deveCalcularLocacaoEconomicaComDescontoPrataEUmaHoraDeAtraso()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            7,
            700,
            1,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("718.20"));
    }
    //CT08
    @Test
    void deveCalcularLocacaoEconomicaComDescontoPrataParaQuatorzeDias()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            14,
            700,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1436.40"));
    }
    //CT09
    @Test
    void deveCalcularLocacaoIntermediariaComSeguroBasicoEAtrasoDeDuasHoras()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.INTERMEDIARIO,
            NivelCliente.OURO,
            5,
            800,
            2,
            TipoSeguro.BASICO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1266.50"));
    }
    //CT10
    @Test
    void deveCalcularLocacaoIntermediariaComSeguroBasicoEAtrasoDeTresHoras()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.INTERMEDIARIO,
            NivelCliente.OURO,
            5,
            799,
            3,
            TipoSeguro.BASICO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1301.50"));
    }
    //CT11
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
    //CT12
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
    //CT13
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
    //CT14
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
    //CT15
    @Test
    void deveCalcularLocacaoEconomicaSemDescontoPrataoPorDiariasInsuficientesECemQuilometrosExcedentes()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            2,
            300,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("320.00"));
    }

    //CT16
    @Test
    void deveCalcularLocacaoEconomicaSemDescontoPrataoPorDiariasInsuficientesENoventaNoveQuilometrosExcedentes()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            2,
            299,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("319.20"));
    }

    //CT17
    @Test
    void deveCalcularLocacaoEconomicaSemDescontoPrataoPorDiariasInsuficientesEUmQuilometroExcedentes()
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.PRATA,
            2,
            201,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("240.80"));
    }
}