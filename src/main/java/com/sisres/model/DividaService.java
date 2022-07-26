package com.sisres.model;

import java.util.List;

public class DividaService {
	public String[][][][] getDividasOC(String mes, String ano, List listOC) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.getDividasOC(mes, ano, listOC);
	}
}
