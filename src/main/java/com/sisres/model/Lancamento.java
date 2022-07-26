package com.sisres.model;

import java.util.Date;

public class Lancamento {

	private int idLancamento;
	private int codigo;
	private String tipoDocAutorizacao;
	private String responsavel;
	private String numeroDocAutorizacao;
	private Date dataDocAutorizacao;
	private Date dataLancamento;
	private String origemDocAutorizacao;
	private Double valor;
	private String operador;
	private String observacao;
	private String tipo;
	private int dividaId;
	private String numeroOB;
	private Date dataOB;
	private String tipoLancNome;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getNumeroDocAutorizacao() {
		return numeroDocAutorizacao;
	}

	public void setNumeroDocAutorizacao(String numeroDocAutorizacao) {
		this.numeroDocAutorizacao = numeroDocAutorizacao;
	}

	public String getTipoDocAutorizacao() {
		return tipoDocAutorizacao;
	}

	public void setTipoDocAutorizacao(String tipoDocAutorizacao) {
		this.tipoDocAutorizacao = tipoDocAutorizacao;
	}

	public Date getDataDocAutorizacao() {
		return dataDocAutorizacao;
	}

	public void setDataDocAutorizacao(Date dateParam) {
		this.dataDocAutorizacao = dateParam;
	}

	public String getOrigemDocAutorizacao() {
		return origemDocAutorizacao;
	}

	public void setOrigemDocAutorizacao(String origemDocAutorizacao) {
		this.origemDocAutorizacao = origemDocAutorizacao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setIdLancamento(int idLancamento) {
		this.idLancamento = idLancamento;
	}

	public int getIdLancamento() {
		return idLancamento;
	}

	public void setDividaId(int dividaId) {
		this.dividaId = dividaId;
	}

	public int getDividaId() {
		return dividaId;
	}

	public void setNumeroOB(String numeroOB) {
		this.numeroOB = numeroOB;
	}

	public String getNumeroOB() {
		return numeroOB;
	}

	public void setDataOB(Date dataOB) {
		this.dataOB = dataOB;
	}

	public Date getDataOB() {
		return dataOB;
	}

	public void setTipoLancNome(String tipoLancNome) {
		this.tipoLancNome = tipoLancNome;
	}

	public String getTipoLancNome() {
		return tipoLancNome;
	}

}