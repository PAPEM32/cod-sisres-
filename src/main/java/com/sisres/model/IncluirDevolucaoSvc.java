package com.sisres.model;

public class IncluirDevolucaoSvc {

	/**
	 * @param args
	 */
	public double getSaldoRegularizar(int codDivida) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.verificaSaldoRegularizar(codDivida);

	}

	public boolean setLancamentoDevolucao(Lancamento lancamento) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.inserirLancamentoDevolucao(lancamento);
	}

}
