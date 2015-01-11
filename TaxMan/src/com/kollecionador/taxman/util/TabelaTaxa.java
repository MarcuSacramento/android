package com.kollecionador.taxman.util;

public class TabelaTaxa {

	private Float salario, imposto, encargos, beneficios,
			resultadoReal, resultadoPercentual, resultadoTxAtual, resultadoRealA, resultadoPercentualA, resultadoTxAtualA,faturamento,
			taxa;

	public Float getSalario() {
		return salario;
	}

	public void setSalario(Float salario) {
		this.salario = salario;
	}

	public Float getCustos() {
		return getSalario()+getEncargos()+getImposto();
	}

	

	public Float getImposto() {
		return imposto;
	}

	public void setImposto(Float imposto) {
		this.imposto = imposto;
	}

	public Float getEncargos() {
		return encargos;
	}

	public void setEncargos(Float encargos) {
		this.encargos = encargos;
	}

	public Float getBeneficios() {
		return beneficios;
	}

	public void setBeneficios(Float beneficios) {
		this.beneficios = beneficios;
	}

	public Float getResultadoReal() {
		return resultadoReal;
	}

	public void setResultadoReal(Float resultadoReal) {
		this.resultadoReal = resultadoReal;
	}

	public Float getResultadoPercentual() {
		return resultadoPercentual;
	}

	public void setResultadoPercentual(Float resultadoPercentual) {
		this.resultadoPercentual = resultadoPercentual;
	}

	public Float getResultadoTxAtual() {
		return resultadoTxAtual;
	}

	public void setResultadoTxAtual(Float resultadoTxAtual) {
		this.resultadoTxAtual = resultadoTxAtual;
	}

	public Float getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(Float faturamento) {
		this.faturamento = faturamento;
	}

	public Float getTaxa() {
		return taxa;
	}

	public void setTaxa(Float taxa) {
		this.taxa = taxa;
	}

	public Float getResultadoRealA() {
		return resultadoRealA;
	}

	public void setResultadoRealA(Float resultadoRealA) {
		this.resultadoRealA = resultadoRealA;
	}

	public Float getResultadoPercentualA() {
		return resultadoPercentualA;
	}

	public void setResultadoPercentualA(Float resultadoPercentualA) {
		this.resultadoPercentualA = resultadoPercentualA;
	}

	public Float getResultadoTxAtualA() {
		return resultadoTxAtualA;
	}

	public void setResultadoTxAtualA(Float resultadoTxAtualA) {
		this.resultadoTxAtualA = resultadoTxAtualA;
	}

	
	
	
}
