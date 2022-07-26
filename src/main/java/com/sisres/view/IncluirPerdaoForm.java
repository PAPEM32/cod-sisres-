package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.utilitaria.Utilitaria;

public class IncluirPerdaoForm extends ActionForm {
	private String tipoDocAutorizacao;
	private String dataDocAutorizacao;
	private String numeroDoc;
	private String valor;
	private String observacao;

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

	private static final long serialVersionUID = 1L;

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (tipoDocAutorizacao.equals(""))
			errors.add("tipoDocAutorizacao", new ActionMessage("error.campoTipoDocAut.required"));

		if (numeroDoc.equals(""))
			errors.add("numeroDoc", new ActionMessage("error.campoNumeroDocAut.required"));

		if (dataDocAutorizacao == null) {
			errors.add("dataDocAutorizacao", new ActionMessage("error.campoDataDocAut.required"));
		} else {
			try {
				boolean bdata = Utilitaria.validaData(dataDocAutorizacao);
				if (!bdata) {
					errors.add("dataDocAutorizacao", new ActionMessage("error.campoDataDocAut.range"));
				} else {
					dataDocAutorizacao = Utilitaria.formatarData(dataDocAutorizacao, "dd/MM/yyyy");
				}

			} catch (Exception e) {
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

		return errors;

	}

	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
	}

	public String getNumeroDoc() {
		return numeroDoc;
	}
}
