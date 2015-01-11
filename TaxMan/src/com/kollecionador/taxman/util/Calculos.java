package com.kollecionador.taxman.util;

import java.util.List;

/**
 * Classe para apoio aos cálculos da aplicação
 * 
 * @author Marcus Sacramento
 * @version 1.0
 * @since 31/12/2014
 */
public class Calculos {
	/**
	 * /** Tipo enumerado para facilitar a compreensão do tipo passado como
	 * parametro sobre a contratação
	 *
	 * @author Marcus Sacramento
	 * @version 1.0
	 * @since 31/12/2014
	 */
	public enum Contratacao {
		CONTRATACAO_CLT("CLT", "Contrato Pessoa Física"), CONTRATACAO_PJ("PJ",
				"Contrato Pessoa Jurídica"), CONTRATACAO_ESTAGIARIO("STAG",
				"Contrato de Estágio");

		private String nome;

		Contratacao(String nome, String descricao) {
			this.nome = nome;

		}

		public String getNome() {
			return this.nome;
		}

		public String getDescricao() {
			return this.nome;
		}

	}

	/**
	 * Método para cálculo do Valor fechado de acordo com o Salário e benefícios
	 * informados
	 * 
	 * @param salario
	 *            Salário Bruto Acordado CLT ou PJ
	 * @param beneficio
	 *            Soma dos benefícios CLT
	 * @param encargo
	 *            Encargo trabalhista CLT aplicado sobre o salário Bruto
	 * @param cargaTributaria
	 *            Carga Tributária aplicada ao Faturamento efetuado
	 * @param dissidio
	 *            Dissídio anual que incide sobre o valor do salário
	 * @param margem
	 *            Margem desejada ao final do faturamento
	 * @param contratacao
	 *            Tipo de Contratação CLT ou PJ(Enumerado)
	 * @return Retorna o valor total de contrato
	 * @author Marcus Sacramento
	 * @version 1.0
	 * @since 31/12/2014
	 */
	public Float calculaTaxa(Float salario, Float beneficio, Float encargo,
			Float cargaTributaria, Float dissidio, Float margem,
			String contratação) {

		Float valorBase = Float.valueOf("0.0");
		Float valorAux = Float.valueOf("0.0");
		Float valorCarga = Float.valueOf("0.0");
		Float valorCusto = Float.valueOf("0.0");
		Float valorLucro = Float.valueOf("0.0");
		Float decremento = Float.valueOf("0.01");
		Float decrementoAux = Float.valueOf("0.01");
		Float incremento = Float.valueOf("0.01");
		encargo = encargo / 100;
		cargaTributaria = cargaTributaria / 100;
		dissidio = dissidio / 100;
		margem = margem / 100;

		boolean controle = false;

		if (margem > Float.valueOf("0.5")) {
			incremento = Float.valueOf("5.00");
			decremento = Float.valueOf("0.50");
		}

		if (margem > Float.valueOf("0.7")) {
			incremento = Float.valueOf("10.00");
			decremento = Float.valueOf("3.00");
		}

		if (margem > Float.valueOf("1")) {
			incremento = Float.valueOf("20.00");
			decremento = Float.valueOf("7.00");
		}

		if (contratação.equalsIgnoreCase(Contratacao.CONTRATACAO_CLT.getNome())) {
			valorBase = (salario + (salario * encargo) + (salario * dissidio))
					+ beneficio;

		} else {
			valorBase = salario;
		}
		valorAux = valorBase;
		do {
			valorCarga = valorAux * cargaTributaria;
			valorCusto = valorCarga + valorBase;

			valorLucro = 1 - (valorCusto / valorAux);

			if (valorLucro >= margem) {
				controle = true;
			}
			valorAux = valorAux + incremento;
		} while (!controle);

		controle = false;

		if (valorLucro > margem) {
			do {
				valorCarga = valorAux * cargaTributaria;
				valorCusto = valorCarga + valorBase;

				valorLucro = 1 - (valorCusto / valorAux);

				if (valorLucro <= margem) {
					controle = true;
				}
				valorAux = valorAux - decremento;
			} while (!controle);

		}

		if (valorLucro < margem) {
			do {
				valorCarga = valorAux * cargaTributaria;
				valorCusto = valorCarga + valorBase;

				valorLucro = 1 - (valorCusto / valorAux);

				if (valorLucro <= margem) {
					controle = true;
				}
				valorAux = valorAux + incremento;
			} while (!controle);

		}

		if (valorLucro > margem) {
			do {
				valorCarga = valorAux * cargaTributaria;
				valorCusto = valorCarga + valorBase;

				valorLucro = 1 - (valorCusto / valorAux);

				if (valorLucro <= margem) {
					controle = true;
				}
				valorAux = valorAux - decrementoAux;
			} while (!controle);

		}

		return valorAux;
	}

	/**
	 * Método para cálculo de Qual Faixa Salarial do INSS se adequa ao Salário
	 * Bruto Informado
	 * 
	 * @param salario
	 * @param faixaSalarial
	 * @return O Retorno é do Tipo ResultadoFaixaSalarial que é um tipo que
	 *         informa a Faixa e o percentual
	 * @author Marcus Sacramento
	 * @version 1.0
	 * @since 31/12/2014
	 */
	public ResultadoFaixaSalarial calculaFaixaINSS(Float salario,
			List<FaixaSalarial> faixaSalarial) {
		FaixaSalarial faixa = new FaixaSalarial();
		ResultadoFaixaSalarial resultado = new ResultadoFaixaSalarial();
		resultado.setValor(Float.valueOf(0));
		resultado.setPercentual(Float.valueOf(0));

		for (FaixaSalarial s : faixaSalarial) {

			if (salario >= s.salarioBase && salario < s.salarioTeto) {
				resultado.setValor(salario * (s.percentual / 100));
				resultado.setPercentual(s.percentual);

			}
			faixa = s;

		}
		if (resultado.getPercentual().equals(Float.valueOf(0))
				&& resultado.getValor().equals(Float.valueOf(0))
				&& !faixa.getPercentual().equals(Float.valueOf(0))) {
			resultado.setValor(faixa.getPercentual());

		}

		return resultado;

	}

	/**
	 * Método para cálculo de qual Faixa Salarial do IRRF se adequa ao Salário
	 * Bruto Informado
	 * 
	 * @param salario
	 * @param faixaSalarial
	 * @return O Retorno é do Tipo ResultadoFaixaSalarial que é um tipo que
	 *         informa a Faixa e o percentual
	 * @author Marcus Sacramento
	 * @version 1.0
	 * @since 31/12/2014
	 */
	public ResultadoFaixaSalarial calculaFaixaIRRF(Float salario,
			List<FaixaSalarial> faixaSalarial) {

		ResultadoFaixaSalarial resultado = new ResultadoFaixaSalarial();
		resultado.setValor(Float.valueOf(0));
		resultado.setPercentual(Float.valueOf(0));

		for (FaixaSalarial s : faixaSalarial) {

			if (salario >= s.salarioBase && salario < s.salarioTeto) {
				resultado.setValor((salario * (s.percentual / 100))
						- s.getDeducao());
				resultado.setPercentual(s.percentual);
			}
		}

		return resultado;

	}

	/**
	 * Método para calcular os valores a preencher a tabela
	 * 
	 * @param valor
	 *            Valor Fechado conforme método calculaTaxa()
	 * @author Marcus Sacramento
	 * @version 1.0
	 * @since 31/12/2014
	 */
	public TabelaTaxa calculoTabelaTaxa(Float salario, Float valor,
			Float taxaAtual, Preferencia pref, Contratacao contrato) {

		TabelaTaxa tabelaResultado = new TabelaTaxa();

		tabelaResultado.setSalario(salario);
		/**
		 * Cálculo da Taxa Mínima por hora
		 */
		tabelaResultado.setTaxa(valor
				/ (pref.getDiasUteis() * pref.getHorasTrabalho()));
		tabelaResultado.setImposto(valor * (pref.getImpFatura() / 100));

		/**
		 * Cálculo dos Encargos trabalhistas e benefícios
		 */
		if (contrato.equals(Contratacao.CONTRATACAO_CLT)) {
			tabelaResultado
					.setEncargos((salario * (pref.getImpEncargo() / 100))
							+ (salario * (pref.getDissidio() / 100))
							+ pref.getBeneficios());
			tabelaResultado.setBeneficios(pref.getBeneficios());
		} else {
			tabelaResultado.setEncargos((float) 0.00);
			tabelaResultado.setBeneficios((float) 0.00);
		}
		/**
		 * Cálculo dos custos
		 */
		if (contrato.equals(Contratacao.CONTRATACAO_CLT)) {

			/**
			 * Cálculo do Resultado Real(R$)
			 */
			tabelaResultado.setResultadoReal(valor
					- tabelaResultado.getCustos());
			/**
			 * Cálculo do Resultado percentual(%)
			 */
			tabelaResultado.setResultadoPercentual(1 - (tabelaResultado
					.getCustos() / valor));
			/**
			 * Cálculo do Resultado Real sob a taxa atual
			 */
			tabelaResultado.setResultadoTxAtual(1
					- tabelaResultado.getCustos()
					/ (taxaAtual * pref.getDiasUteis() * pref
							.getHorasTrabalho()));
			/**
			 * Cálculo do Resultado Real(R$) com variação da Ajuda de custo
			 */
			tabelaResultado.setResultadoRealA(valor
					- tabelaResultado.getCustos() - pref.getBenAjudaCusto());
			/**
			 * Cálculo do Resultado percentual(%) com variação da Ajuda de custo
			 */
			tabelaResultado.setResultadoPercentualA(1 - ((tabelaResultado
					.getCustos() + pref.getBenAjudaCusto()) / valor));
			/**
			 * Cálculo do Resultado(R$) com variação da Ajuda de custo
			 */
			tabelaResultado.setResultadoTxAtualA(1
					- (tabelaResultado.getCustos() + pref.getBenAjudaCusto())
					/ (taxaAtual * pref.getDiasUteis() * pref
							.getHorasTrabalho()));
		} else {
			/**
			 * Cálculo do Resultado Real(R$)
			 */
			tabelaResultado.setResultadoReal(valor
					- tabelaResultado.getCustos());
			/**
			 * Cálculo do Resultado percentual(%)
			 */
			tabelaResultado.setResultadoPercentual(1 - (tabelaResultado
					.getCustos() / valor));
			/**
			 * Cálculo do Resultado Real sob a taxa atual
			 */
			tabelaResultado.setResultadoTxAtual(1
					- tabelaResultado.getCustos()
					/ (taxaAtual * pref.getDiasUteis() * pref
							.getHorasTrabalho()));

			tabelaResultado.setResultadoRealA(valor
					- tabelaResultado.getCustos());

			tabelaResultado.setResultadoPercentualA(1 - ((tabelaResultado
					.getCustos()) / valor));

			tabelaResultado.setResultadoTxAtualA(1
					- (tabelaResultado.getCustos())
					/ (taxaAtual * pref.getDiasUteis() * pref
							.getHorasTrabalho()));
		}
		tabelaResultado.setFaturamento(valor);
		return tabelaResultado;
	}
}
