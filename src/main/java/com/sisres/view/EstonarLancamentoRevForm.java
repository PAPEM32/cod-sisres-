package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

@SuppressWarnings("serial")
public class EstonarLancamentoRevForm extends ActionForm {

	private String motivo = null;

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		// Form verification
		if (motivo == null) {
			errors.add("title", new ActionMessage("error.motivo.required"));
			return errors;
		}

		if (motivo.length() == 0) {
			errors.add("title", new ActionMessage("error.motivo.required"));
		}
		return errors;
	}

}
