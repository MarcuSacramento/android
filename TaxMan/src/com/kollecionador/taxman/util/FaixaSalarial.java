package com.kollecionador.taxman.util;

public class FaixaSalarial {
	Float salarioBase;
	Float salarioTeto;
	Float percentual;
	Float deducao;

	public FaixaSalarial(Float salarioBase, Float salarioTeto,
			Float percentual, Float deducao) {
		super();
		this.salarioBase = salarioBase;
		this.salarioTeto = salarioTeto;
		this.percentual = percentual;
		this.deducao = deducao;
	}

	public Float getDeducao() {
		return deducao;
	}

	public void setDeducao(Float deducao) {
		this.deducao = deducao;
	}

	public FaixaSalarial() {

	}

	public Float getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(Float salarioBase) {
		this.salarioBase = salarioBase;
	}

	public Float getSalarioTeto() {
		return salarioTeto;
	}

	public void setSalarioTeto(Float salarioTeto) {
		this.salarioTeto = salarioTeto;
	}

	public Float getSalario() {
		return salarioBase;
	}

	public void setSalario(Float salario) {
		this.salarioBase = salario;
	}

	public Float getPercentual() {
		return percentual;
	}

	public void setPercentual(Float percentual) {
		this.percentual = percentual;
	}

}
