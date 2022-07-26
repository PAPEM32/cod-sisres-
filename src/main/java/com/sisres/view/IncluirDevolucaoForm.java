package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.utilitaria.Utilitaria;

public class IncluirDevolucaoForm extends ActionForm {

	/**
	 * 
	 */
	private String tipoDocAutorizacao = "";
	private String dataDocAutorizacao;
	private String numeroOB;
	private String dataOB;
	private String valor;
	private String observacao;
	private String origemDocAutorizacao;

	public String getOrigemDocAutorizacao() {
		return origemDocAutorizacao;
	}

	public void setOrigemDocAutorizacao(String origemDocAutorizacao) {
		this.origemDocAutorizacao = origemDocAutorizacao;
	}

	public String getTipoDocAutorizacao() {
		return tipoDocAutorizacao;
	}

	public void setTipoDocAutorizacao(String tipoDocAutorizacao) {
		this.tipoDocAutorizacao = tipoDocAutorizacao;
	}

	public String getDataDocAutorizacao() {
		return dataDocAutorizacao;
	}

	public void setDataDocAutorizacao(String dataDocAutorizacao) {
		this.dataDocAutorizacao = dataDocAutorizacao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public void setNumeroOB(String numeroOB) {
		this.numeroOB = numeroOB;
	}

	public String getNumeroOB() {
		return numeroOB;
	}

	public void setDataOB(String dataOB) {
		this.dataOB = dataOB;
	}

	public String getDataOB() {
		return dataOB;
	}

	private static final long serialVersionUID = 1L;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();
//        context.log(" Entrou no validate do devolucao divida ");

		if (tipoDocAutorizacao.equals(""))
			errors.add("tipoDocAutorizacao", new ActionMessage("error.campoTipoDocAut.required"));

		if (numeroOB.equals(""))
			errors.add("numeroOB", new ActionMessage("error.campoNumeroOB.required"));

		if (dataDocAutorizacao == null) {
			errors.add("dataDocAutorizacao", new ActionMessage("error.campoDataDocAut.required"));
		} else {
			try {
				Utilitaria.validaData(dataDocAutorizacao);
				dataDocAutorizacao = Utilitaria.formatarData(dataDocAutorizacao, "dd/MM/yyyy");

				// System.out.println("Data válida" + dataDocAutorizacao);
			} catch (Exception e) {
				// System.out.println("Data inválida");
				errors.add("dataDocAutorizacao", new ActionMessage("error.campoDataDocAut.range"));
			}

		}

		if (valor == null) {
			errors.add("valor", new ActionMessage("error.campoValorDevolucao.required"));
		} else {

			valor = valor.trim().replace(".", "");
			valor = valor.replace(",", ".");
			try {
				Double.valueOf(valor);
			} catch (Exception e) {
				errors.add("Valor", new ActionMessage("error.campoValor.range"));
			}

		}

		if (dataOB.equals("")) {
			errors.add("dataOB", new ActionMessage("error.campodataOB.required"));
		} else {
			try {
				dataOB = Utilitaria.formatarData(dataOB, "dd/MM/yyyy");
				// System.out.println(dataOB);
			} catch (Exception e) {
				errors.add("dataOB", new ActionMessage("error.campoDataOB.range"));
			}

		}

		return errors;

	}

}
