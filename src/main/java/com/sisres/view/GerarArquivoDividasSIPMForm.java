package com.sisres.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

@SuppressWarnings("serial")
public class GerarArquivoDividasSIPMForm extends ActionForm {

	private String mesAno = "";
	private String mesStr = "";
	private int mes = -1;
	private String anoStr = "";
	private int ano = -1;

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public int getMes() {
		return mes;
	}

	public int getAno() {
		return ano;
	}

	public String getMesStr() {
		return mesStr;
	}

	public String getAnoStr() {
		return anoStr;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

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

		return errors;

	}

}
