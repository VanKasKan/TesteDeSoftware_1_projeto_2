package br.com.aluguelcarros;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CaminhosBasicosTest
{
    private final CalculadoraCustoLocacao calculadora = new CalculadoraCustoLocacao();

    @Test
    @DisplayName("P1: Categoria nula")
    void testP1_CategoriaNula() {
        assertThrows(NullPointerException.class, () ->
            calculadora.calcularCustoLocacao(
                null, NivelCliente.COMUM, 1, 0, 0,
                TipoSeguro.SEM_SEGURO, false
            )
        );
    }

    @Test
    @DisplayName("P2: NivelCliente nulo")
    void testP2_NivelClienteNulo() {
        assertThrows(NullPointerException.class, () ->
            calculadora.calcularCustoLocacao(
                CategoriaVeiculo.ECONOMICO, null, 1, 0, 0,
                TipoSeguro.SEM_SEGURO, false
            )
        );
    }

    @Test
    @DisplayName("P3: Seguro nulo")
    void testP3_SeguroNulo() {
        assertThrows(NullPointerException.class, () ->
            calculadora.calcularCustoLocacao(
                CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
                1, 0, 0, null, false
            )
        );
    }

    @Test
    @DisplayName("P4: Número de diárias menor ou igual a zero")
    void testP4_DiariasInvalidas() {
        assertThrows(IllegalArgumentException.class, () ->
            calculadora.calcularCustoLocacao(
                CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
                0, 0, 0, TipoSeguro.SEM_SEGURO, false
            )
        );
    }

    @Test
    @DisplayName("P5: Quilômetros rodados negativos")
    void testP5_QuilometrosNegativos() {
        assertThrows(IllegalArgumentException.class, () ->
            calculadora.calcularCustoLocacao(
                CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
                1, -1, 0, TipoSeguro.SEM_SEGURO, false
            )
        );
    }

    @Test
    @DisplayName("P6: Horas de atraso negativas")
    void testP6_HorasAtrasoNegativas() {
        assertThrows(IllegalArgumentException.class, () ->
            calculadora.calcularCustoLocacao(
                CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
                1, 0, -1, TipoSeguro.SEM_SEGURO, false
            )
        );
    }

    @Test
    @DisplayName("P7: Categoria econômica")
    void testP7_CaminhoBaseEconomico() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 200, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("240.00"), resultado);
    }

    @Test
    @DisplayName("P8: Categoria intermediária")
    void testP8_CategoriaIntermediario() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.INTERMEDIARIO, NivelCliente.COMUM,
            2, 200, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("360.00"), resultado);
    }

    @Test
    @DisplayName("P9: Categoria SUV")
    void testP9_CategoriaSUV() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.SUV, NivelCliente.COMUM,
            2, 200, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("560.00"), resultado);
    }

    @Test
    @DisplayName("P10: Desconto de duração de 5%")
    void testP10_DescontoDuracao5PorCento() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            5, 500, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("570.00"), resultado);
    }

    @Test
    @DisplayName("P11: Desconto de duração de 10%")
    void testP11_DescontoDuracao10PorCento() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            10, 1000, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("1080.00"), resultado);
    }

    @Test
    @DisplayName("P12: Desconto de duração de 15%")
    void testP12_DescontoDuracao15PorCento() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            15, 1500, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("1530.00"), resultado);
    }

    @Test
    @DisplayName("P13: Desconto de fidelidade Prata")
    void testP13_DescontoFidelidadePrata() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.PRATA,
            7, 700, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("718.20"), resultado);
    }

    @Test
    @DisplayName("P14: Desconto de fidelidade Ouro")
    void testP14_DescontoFidelidadeOuro() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.OURO,
            5, 500, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("513.00"), resultado);
    }

    @Test
    @DisplayName("P15: Excedente de quilometragem até 100 km")
    void testP15_ExcedenteKmAte100() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 250, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("280.00"), resultado);
    }

    @Test
    @DisplayName("P16: Excedente de quilometragem entre 101 e 300 km")
    void testP16_ExcedenteKm101Ate300() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 400, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("440.00"), resultado);
    }

    @Test
    @DisplayName("P17: Excedente de quilometragem acima de 300 km")
    void testP17_ExcedenteKmAcima300() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 600, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("840.00"), resultado);
    }

    @Test
    @DisplayName("P18: Seguro básico")
    void testP18_SeguroBasico() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 200, 0, TipoSeguro.BASICO, false
        );

        assertEquals(new BigDecimal("290.00"), resultado);
    }

    @Test
    @DisplayName("P19: Seguro completo")
    void testP19_SeguroCompleto() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 200, 0, TipoSeguro.COMPLETO, false
        );

        assertEquals(new BigDecimal("330.00"), resultado);
    }

    @Test
    @DisplayName("P20: Cliente Prata com menos de 7 diárias")
    void testP20_PrataMenosSeteDiarias() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.PRATA,
            5, 200, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("570.00"), resultado);
    }

    @Test
    @DisplayName("P21: Cliente Prata com atraso anterior")
    void testP21_PrataComAtrasoAnterior() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.PRATA,
            7, 200, 0, TipoSeguro.SEM_SEGURO, true
        );

        assertEquals(new BigDecimal("756.00"), resultado);
    }

    @Test
    @DisplayName("P22: Cliente Ouro com menos de 5 diárias")
    void testP22_OuroMenosCincoDiarias() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.OURO,
            2, 200, 0, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("240.00"), resultado);
    }

    @Test
    @DisplayName("P23: Cliente Ouro com atraso anterior")
    void testP23_OuroComAtrasoAnterior() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.OURO,
            5, 200, 0, TipoSeguro.SEM_SEGURO, true
        );

        assertEquals(new BigDecimal("570.00"), resultado);
    }

    @Test
    @DisplayName("P24: Atraso entre 2 e 3 horas")
    void testP24_AtrasoAteTresHoras() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 200, 2, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("288.00"), resultado);
    }

    @Test
    @DisplayName("P25: Atraso superior a 3 horas")
    void testP25_AtrasoAcimaTresHoras() {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO, NivelCliente.COMUM,
            2, 200, 4, TipoSeguro.SEM_SEGURO, false
        );

        assertEquals(new BigDecimal("360.00"), resultado);
    }
}
