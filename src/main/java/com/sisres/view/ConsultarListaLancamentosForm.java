package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ConsultarListaLancamentosForm extends ActionForm {

	private String dividaId;

// CAMPO INFORMACOES SOBRE O LANCAMENTO

	public void setDividaId(String dividaId) {
		this.dividaId = dividaId;
	}

	public String getDividaId() {
		// System.out.println("form "+ dividaId);
		return dividaId;
	}

	// Método de validação de matrícula financeira

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors listaErros = new ActionErrors();
		// testes de parametros que ainda não foram feitos.

		// codDivida teste******************************************************
		if (!dividaId.equals("")) {
			int idDiv = 0;
			try {
				idDiv = Integer.parseInt(dividaId);
				// System.out.println("form - passei pelo validate");
			} catch (NumberFormatException nfe) {
				listaErros.add("campoCodDivida", new ActionMessage("error.campoCodDivida.range"));// procurar no
																									// properties
			}
		}

		return listaErros;
	}

}// fim da classe
