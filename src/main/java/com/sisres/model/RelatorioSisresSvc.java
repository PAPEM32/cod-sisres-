/**
 * 
 */
package com.sisres.model;

import java.util.List;

public class RelatorioSisresSvc {

	public String[][][][] getDividasPessoasOC(String mes, String ano, List listOC) throws SispagException {
		DAODivida daoDivida = new DAODivida();

		return daoDivida.getDividasOC(mes, ano, listOC);
	}

	// INCIO String GERADOR RELATORIO DIVIDAS POR OC DATA INCLUSAO
	public String[][][][] getDividasPessoasOCDTInclusao(String mes, String ano, List listOC) throws SispagException {
		DAODivida daoDivida = new DAODivida();

		return daoDivida.getDividasOCDTInclusao(mes, ano, listOC);
	}
	// FIM String GERADOR RELATORIO DIVIDAS POR OC DATA INCLUSAO

	public List<OC> getAllOC() throws SispagException {
		DAOOC daooc = new DAOOC();
		return daooc.getAllOC();
	}

	public OC getOC(String oc) throws SispagException {
		DAOOC daooc = new DAOOC();
		return daooc.getOC(oc);
	}
}