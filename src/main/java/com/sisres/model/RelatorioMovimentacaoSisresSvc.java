package com.sisres.model;

public class RelatorioMovimentacaoSisresSvc {

	public String[][] getDividasMovimentacao(String mes, String ano) throws SispagException {
		DAODivida daoDividaMovimentacao = new DAODivida();
		return daoDividaMovimentacao.getDividasMovimentacaoSisres(mes, ano);
	}
}
