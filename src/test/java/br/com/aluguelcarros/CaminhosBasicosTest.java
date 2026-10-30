package br.com.aluguelcarros;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CaminhosBasicosTest
{
    private final CalculadoraCustoLocacao calculadora = new CalculadoraCustoLocacao();

    // Exemplo inicial: acrescente os testes necessários para caminhos básicos de McCabe.
    @ParameterizedTest
    @CsvSource({"1, 120.00", "2, 240.00"})
    void deveCalcularLocacaoBasica(int numeroDiarias, BigDecimal valorEsperado)
    {
        BigDecimal resultado = calculadora.calcularCustoLocacao(
            CategoriaVeiculo.ECONOMICO,
            NivelCliente.COMUM,
            numeroDiarias,
            0,
            0,
            TipoSeguro.SEM_SEGURO,
            false
        );

        assertThat(resultado).isEqualByComparingTo(valorEsperado);
    }
}
