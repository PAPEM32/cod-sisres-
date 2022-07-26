package com.sisres.view;

import org.apache.struts.action.ActionForm;

public class FiltrarDividaHistForm extends ActionForm {

	private String matFin = null;
	private String dataIni = null;
	private String dataFim = null;
	private String oc = null;

	public String getMatFin() {
		return matFin;
	}

	public void setMatFin(String matFin) {
		this.matFin = matFin;
	}

	public String getDataIni() {
		return dataIni;
	}

	public void setDataIni(String dataIni) {
		this.dataIni = dataIni;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getOc() {
		return oc;
	}

	public void setOc(String oc) {
		this.oc = oc;
	}

}
