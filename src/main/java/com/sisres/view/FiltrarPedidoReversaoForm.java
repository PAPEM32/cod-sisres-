package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class FiltrarPedidoReversaoForm extends ActionForm {

	private String tipoDivida = "";
	private String banco = "";

	public String getTipoDivida() {
		return tipoDivida;
	}

	public void setTipoDivida(String tipoDivida) {
		this.tipoDivida = tipoDivida;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		int bancoInt = 0;

		ActionErrors listaErros = new ActionErrors();
		// testes de parametros que ainda não foram feitos.

		// System.out.println("Validei no form");

		// System.out.println(banco);
		// System.out.println(tipoDivida);

		if (tipoDivida.equals("")) {

			// System.out.println("Tipo da Divida " + tipoDivida);

			listaErros.add("TipoDivida", new ActionMessage("error.campoTipoDivida.invalid"));// procurar no properties
		}

		if (!banco.equals("")) {

			// System.out.println("Banco " + banco);

			try {
				bancoInt = Integer.parseInt(banco);
			} catch (NumberFormatException nfe) {
				listaErros.add("Banco", new ActionMessage("error.campoBanco.invalid"));// procurar no properties
			}

		} else {
			listaErros.add("Banco", new ActionMessage("error.campoBanco.required"));// procurar no properties
		}

		return listaErros;

	}
}
