package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

@SuppressWarnings("serial")
public class RelatorioMovimentacaoSisresForm extends ActionForm {

	private String mesAno = "";
	private String mesStr = "";
	private int mes = -1;
	private String anoStr = "";
	private int ano = -1;
	private String ordenador = "";
	private String ptOrdenador = "";
	private String funcOrdenador = "";
	private String agente = "";
	private String ptAgente = "";
	private String funcAgente = "";
	private String encarregado = "";
	private String ptEncarregado = "";
	private String funcEncarregado = "";
	private String relator = "";
	private String ptRelator = "";
	private String funcRelator = "";
	private String detalhado = "";
	private String totalGeral = "";

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public String getMesStr() {
		return mesStr;
	}

	public void setMesStr(String mesStr) {
		this.mesStr = mesStr;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getAnoStr() {
		return anoStr;
	}

	public void setAnoStr(String anoStr) {
		this.anoStr = anoStr;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getOrdenador() {
		return ordenador;
	}

	public void setOrdenador(String ordenador) {
		this.ordenador = ordenador;
	}

	public String getPtOrdenador() {
		return ptOrdenador.toUpperCase();
	}

	public void setPtOrdenador(String ptOrdenador) {
		this.ptOrdenador = ptOrdenador;
	}

	public String getFuncOrdenador() {
		return funcOrdenador;
	}

	public void setFuncOrdenador(String funcOrdenador) {
		this.funcOrdenador = funcOrdenador;
	}

	public String getAgente() {
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public String getPtAgente() {
		return ptAgente.toUpperCase();
	}

	public void setPtAgente(String ptAgente) {
		this.ptAgente = ptAgente;
	}

	public String getFuncAgente() {
		return funcAgente;
	}

	public void setFuncAgente(String funcAgente) {
		this.funcAgente = funcAgente;
	}

	public String getEncarregado() {
		return encarregado;
	}

	public void setEncarregado(String encarregado) {
		this.encarregado = encarregado;
	}

	public String getPtEncarregado() {
		return ptEncarregado.toUpperCase();
	}

	public void setPtEncarregado(String ptEncarregado) {
		this.ptEncarregado = ptEncarregado;
	}

	public String getFuncEncarregado() {
		return funcEncarregado;
	}

	public void setFuncEncarregado(String funcEncarregado) {
		this.funcEncarregado = funcEncarregado;
	}

	public String getRelator() {
		return relator;
	}

	public void setRelator(String relator) {
		this.relator = relator;
	}

	public String getPtRelator() {
		return ptRelator.toUpperCase();
	}

	public void setPtRelator(String ptRelator) {
		this.ptRelator = ptRelator;
	}

	public String getFuncRelator() {
		return funcRelator;
	}

	public void setFuncRelator(String funcRelator) {
		this.funcRelator = funcRelator;
	}

	public String getDetalhado() {
		return detalhado;
	}

	public void setDetalhado(String detalhado) {
		this.detalhado = detalhado;
	}

	public String getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(String totalGeral) {
		this.totalGeral = totalGeral;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		errors.clear();
		// validando a opção escolhida
		if (detalhado.equals("") && totalGeral.equals("")) {
			errors.add("opcao", new ActionMessage("error.selecao.required"));
		}
		// validando o mês/ano
		if (!mesAno.equals("")) {
			if (mesAno.length() == 7 && mesAno.indexOf("/") != -1) {
				String[] mesAnoArray = mesAno.split("/");
				mesStr = mesAnoArray[0];
				anoStr = mesAnoArray[1];
				try {
					this.mes = Integer.parseInt(mesStr);
				} catch (NumberFormatException nfe) {
					errors.add("mesAno", new ActionMessage("error.campoMesAno.invalid"));
				}
				try {
					this.ano = Integer.parseInt(anoStr);
				} catch (NumberFormatException nfe) {
					errors.add("mesAno", new ActionMessage("error.campoMesAno.invalid"));
				}
			} else {
				errors.add("mesAno", new ActionMessage("error.campoMesAno.invalid"));
			}
		} else {
			errors.add("mesAno", new ActionMessage("error.campoMesAno.required"));
		}

		// validando os dados dos responsaveis
		if (ordenador.equals("") || ptOrdenador.equals("") || funcOrdenador.equals("")) {
			errors.add("ordenador", new ActionMessage("error.campoOrdenador.required"));
		}

		if (agente.equals("") || ptAgente.equals("") || funcAgente.equals("")) {
			errors.add("agente", new ActionMessage("error.campoAgente.required"));
		}

		if (encarregado.equals("") || ptEncarregado.equals("") || funcEncarregado.equals("")) {
			errors.add("encarregado", new ActionMessage("error.campoEncarregado.required"));
		}

		if (relator.equals("") || ptRelator.equals("") || funcRelator.equals("")) {
			errors.add("relator", new ActionMessage("error.campoRelator.required"));
		}

		return errors;
	} // end do método
}// end da classe
