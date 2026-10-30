package br.com.aluguelcarros;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraCustoLocacao
{
    public BigDecimal calcularCustoLocacao(CategoriaVeiculo categoria, NivelCliente nivelCliente, int numeroDiarias, int quilometrosRodados, int horasAtraso, TipoSeguro seguro, boolean possuiAtrasoAnterior)
    {
        if (categoria == null || nivelCliente == null || seguro == null)
        {
            throw new NullPointerException("Argumentos não podem ser nulos");
        }

        if (numeroDiarias <= 0)
        {
            throw new IllegalArgumentException("Número de diárias deve ser maior que zero");
        }

        if (quilometrosRodados < 0)
        {
            throw new IllegalArgumentException("Quilometragem rodada não pode ser negativa");
        }

        if (horasAtraso < 0)
        {
            throw new IllegalArgumentException("Horas de atraso não podem ser negativas");
        }

        BigDecimal valorDiaria;
        if (categoria == CategoriaVeiculo.ECONOMICO)
        {
            valorDiaria = new BigDecimal("120.00");
        }
        else if (categoria == CategoriaVeiculo.INTERMEDIARIO)
        {
            valorDiaria = new BigDecimal("180.00");
        }
        else
        {
            valorDiaria = new BigDecimal("280.00");
        }

        BigDecimal taxaDescontoDuracao;
        if (numeroDiarias <= 2)
        {
            taxaDescontoDuracao = BigDecimal.ZERO;
        }
        else if (numeroDiarias <= 6)
        {
            taxaDescontoDuracao = new BigDecimal("0.05");
        }
        else if (numeroDiarias <= 14)
        {
            taxaDescontoDuracao = new BigDecimal("0.10");
        }
        else
        {
            taxaDescontoDuracao = new BigDecimal("0.15");
        }

        BigDecimal valorBrutoDiarias = valorDiaria.multiply(BigDecimal.valueOf(numeroDiarias));
        BigDecimal descontoDuracao = valorBrutoDiarias.multiply(taxaDescontoDuracao);
        BigDecimal valorDiariasComDesconto = valorBrutoDiarias.subtract(descontoDuracao);

        // Cada condição representa um requisito independente para o benefício.
        BigDecimal taxaDescontoFidelidade;
        if (nivelCliente == NivelCliente.PRATA && numeroDiarias >= 7 && !possuiAtrasoAnterior)
        {
            taxaDescontoFidelidade = new BigDecimal("0.05");
        }
        else if (nivelCliente == NivelCliente.OURO && numeroDiarias >= 5 && !possuiAtrasoAnterior)
        {
            taxaDescontoFidelidade = new BigDecimal("0.10");
        }
        else
        {
            taxaDescontoFidelidade = BigDecimal.ZERO;
        }

        BigDecimal descontoFidelidade = valorDiariasComDesconto.multiply(taxaDescontoFidelidade);
        BigDecimal valorFinalDiarias = valorDiariasComDesconto.subtract(descontoFidelidade);

        // O uso de long evita estouro ao multiplicar uma quantidade grande de diárias.
        long franquiaQuilometros = 100L * numeroDiarias;
        long quilometrosExcedentes = (long) quilometrosRodados - franquiaQuilometros;
        BigDecimal custoQuilometragem;
        if (quilometrosExcedentes <= 0)
        {
            custoQuilometragem = BigDecimal.ZERO;
        }
        else
        {
            BigDecimal tarifaQuilometrica;
            if (quilometrosExcedentes <= 100)
            {
                tarifaQuilometrica = new BigDecimal("0.80");
            }
            else if (quilometrosExcedentes <= 300)
            {
                tarifaQuilometrica = new BigDecimal("1.00");
            }
            else
            {
                tarifaQuilometrica = new BigDecimal("1.50");
            }
            custoQuilometragem = BigDecimal.valueOf(quilometrosExcedentes).multiply(tarifaQuilometrica);
        }

        BigDecimal valorSeguroDiaria;
        if (seguro == TipoSeguro.SEM_SEGURO)
        {
            valorSeguroDiaria = BigDecimal.ZERO;
        }
        else if (seguro == TipoSeguro.BASICO)
        {
            valorSeguroDiaria = new BigDecimal("25.00");
        }
        else
        {
            valorSeguroDiaria = new BigDecimal("45.00");
        }
        BigDecimal custoSeguro = valorSeguroDiaria.multiply(BigDecimal.valueOf(numeroDiarias));

        BigDecimal custoAtraso;
        if (horasAtraso <= 1)
        {
            custoAtraso = BigDecimal.ZERO;
        }
        else if (horasAtraso <= 3)
        {
            custoAtraso = valorDiaria.multiply(new BigDecimal("0.20")).multiply(BigDecimal.valueOf(horasAtraso));
        }
        else
        {
            custoAtraso = valorDiaria;
        }

        BigDecimal total = valorFinalDiarias.add(custoQuilometragem).add(custoSeguro).add(custoAtraso);
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
