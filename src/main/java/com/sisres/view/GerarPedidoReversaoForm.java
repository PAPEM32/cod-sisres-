package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class GerarPedidoReversaoForm extends ActionForm {

	private String[] listaDeDividas;
	private String banco;
	private String codTipo;
	private String ordenador = "";
	private String ptOrdenador = "";
	private String fcOrdenador = "";
	private String agente = "";
	private String ptAgente = "";
	private String fcAgente = "";

	public String[] getListaDeDividas() {
		return listaDeDividas;
	}

	public void setListaDeDividas(String[] listaDeDividas) {
		this.listaDeDividas = listaDeDividas;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getBanco() {
		return banco;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getCodTipo() {
		return codTipo;
	}

	public String getOrdenador() {
		return ordenador;
	}

	public void setOrdenador(String ordenador) {
		this.ordenador = ordenador;
	}

	public String getPtOrdenador() {
		return ptOrdenador;
	}

	public void setPtOrdenador(String ptOrdenador) {
		this.ptOrdenador = ptOrdenador;
	}

	public String getFcOrdenador() {
		return fcOrdenador;
	}

	public void setFcOrdenador(String fcOrdenador) {
		this.fcOrdenador = fcOrdenador;
	}

	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public String getPtAgente() {
		return ptAgente;
	}

	public void setPtAgente(String ptAgente) {
		this.ptAgente = ptAgente;
	}

	public String getFcAgente() {
		return fcAgente;
	}

	public void setFcAgente(String fcAgente) {
		this.fcAgente = fcAgente;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		// System.out.println("Entrei no validate do gerar pedido de reversão");

		ActionErrors errors = new ActionErrors();

		if ((listaDeDividas == null) || (listaDeDividas.length == 0)) {

			// System.out.println("Nulo");

			errors.add("listadeDivReversao", new ActionMessage("error.campolistadeDivReversao.required"));
		}

		if (ordenador.equals("") || ptOrdenador.equals("") || fcOrdenador.equals("")) {
			errors.add("EncDivisao", new ActionMessage("error.campoEncDivisao.required"));
		}

		if (agente.equals("") || ptAgente.equals("") || fcAgente.equals("")) {
			errors.add("TecFina", new ActionMessage("error.campoTecFina.required"));
		}

		return errors;
	}

}
