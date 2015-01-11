package com.kollecionador.taxman.util;

public class Preferencia {

	Float diasUteis, horasTrabalho, benAlimentacao, benCombustivel, benOutros,
			benAjudaCusto, impEncargo, impFatura, dissidio, percMargem,
			percFGTS, simplesNacional;

	public Float getDiasUteis() {
		return diasUteis;
	}

	public void setDiasUteis(Float diasUteis) {
		this.diasUteis = diasUteis;
	}

	public Float getHorasTrabalho() {
		return horasTrabalho;
	}

	public void setHorasTrabalho(Float horasTrabalho) {
		this.horasTrabalho = horasTrabalho;
	}

	public Float getBenAlimentacao() {
		return benAlimentacao;
	}

	public void setBenAlimentacao(Float benalimentacao) {
		this.benAlimentacao = benalimentacao;
	}

	public Float getBenCombustivel() {
		return benCombustivel;
	}

	public void setBenCombustivel(Float benCombustivel) {
		this.benCombustivel = benCombustivel;
	}

	public Float getBenOutros() {
		return benOutros;
	}

	public void setBenOutros(Float benOutros) {
		this.benOutros = benOutros;
	}

	public Float getBenAjudaCusto() {
		return benAjudaCusto;
	}

	public void setBenAjudaCusto(Float benAjudaCusto) {
		this.benAjudaCusto = benAjudaCusto;
	}

	public Float getImpEncargo() {
		return impEncargo;
	}

	public void setImpEncargo(Float impEncargo) {
		this.impEncargo = impEncargo;
	}

	public Float getImpFatura() {
		return impFatura;
	}

	public void setImpFatura(Float impFatura) {
		this.impFatura = impFatura;
	}

	public Float getDissidio() {
		return dissidio;
	}

	public void setDissidio(Float dissidio) {
		this.dissidio = dissidio;
	}

	public Float getPercMargem() {
		return percMargem;
	}

	public void setPercMargem(Float percMargem) {
		this.percMargem = percMargem;
	}

	public Float getPercFGTS() {
		return percFGTS;
	}

	public void setPercFGTS(Float percFGTS) {
		this.percFGTS = percFGTS;
	}

	public Float getSimplesNacional() {
		return simplesNacional;
	}

	public void setSimplesNacional(Float simplesNacional) {
		this.simplesNacional = simplesNacional;
	}

	public Float getTotalBeneficios() {

		return this.benAjudaCusto + this.benAlimentacao + this.benCombustivel
				+ this.benOutros;
	}
	
	public Float getBeneficios() {

		return this.benAlimentacao + this.benCombustivel
				+ this.benOutros;
	}

}
