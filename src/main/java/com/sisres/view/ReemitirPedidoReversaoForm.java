package com.sisres.view;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ReemitirPedidoReversaoForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String codigoPedidoReversao;
	private String anoReversao;
	private String acao;
	private String ordenador = "";
	private String ptOrdenador = "";
	private String fcOrdenador = "";
	private String agente = "";
	private String ptAgente = "";
	private String fcAgente = "";

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (this.acao.equals("filtrarPedidoReversao")) {
			if (!codigoPedidoReversao.equals("")) {

				try {
					Integer.parseInt(codigoPedidoReversao);
				} catch (NumberFormatException nfe) {
					errors.add("campoCodDivida", new ActionMessage("error.campoCodDivida.range"));// procurar no
																									// properties
				}
			} else {
				errors.add("campoCodigoReversao", new ActionMessage("error.codigo.required"));
			}

			if (!anoReversao.equals("")) {
				Calendar calendar = new GregorianCalendar();
				java.util.Date trialTime = new java.util.Date();
				calendar.setTime(trialTime);
				String ANO = "" + calendar.get(Calendar.YEAR);
				int ANOATUAL = Integer.parseInt(ANO);
				int anoForm = Integer.parseInt(anoReversao);

				if ((anoForm < 1900) || (anoForm > ANOATUAL)) {
					errors.add("campoAno", new ActionMessage("error.campoAno.range"));
				}

			} else {
				errors.add("campoAnoReversao", new ActionMessage("error.campoAno.required"));
			}
		}
		if (this.acao.equals("reemitirPedidoReversao")) {
			if (ordenador.equals("") || ptOrdenador.equals("") || fcOrdenador.equals("")) {
				errors.add("EncDivisao", new ActionMessage("error.campoEncDivisao.required"));
			}

			if (agente.equals("") || ptAgente.equals("") || fcAgente.equals("")) {
				errors.add("TecFina", new ActionMessage("error.campoTecFina.required"));
			}
		}

		return errors;
	}

	public String getCodigoPedidoReversao() {
		return codigoPedidoReversao;
	}

	public void setCodigoPedidoReversao(String codigoPedidoReversao) {
		this.codigoPedidoReversao = codigoPedidoReversao;
	}

	public String getAnoReversao() {
		return anoReversao;
	}

	public void setAnoReversao(String anoReversao) {
		this.anoReversao = anoReversao;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
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

}
