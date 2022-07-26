package com.sisres.model;

import java.util.Date;

/*import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat; */

public class Pedido {
	private int id;
	private String codigo;
	private String tipo;
	private int banco;
	private String respAlt;
	private Date respData;
	private int ano;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getBanco() {
		return banco;
	}

	public void setBanco(int banco) {
		this.banco = banco;
	}

	public String getRespAlt() {
		return respAlt;
	}

	public void setRespAlt(String respAlt) {
		this.respAlt = respAlt;
	}

	public Date getRespData() {
		return respData;
	}

	public void setRespData(Date respData) {
		this.respData = respData;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public void getPedido() {

	}

}
