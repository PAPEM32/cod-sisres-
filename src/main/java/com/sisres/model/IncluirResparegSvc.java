package com.sisres.model;

public class IncluirResparegSvc {
	int qtdRespareg, qtdRegaresp;

	public int verificaEstagioCGS() throws SispagException {
		DAODivida daoDivida = new DAODivida();

		return daoDivida.buscarEstagioCGS();
	}

	public boolean verificaExisteResparegSISPAG() throws SispagException {
		DAODivida daoDivida = new DAODivida();
		int[] resultado = new int[2];
		resultado = daoDivida.verificaExisteResparegSISPAG();
		qtdRespareg = resultado[0];
		qtdRegaresp = resultado[1];
		if (qtdRegaresp > 0 | qtdRespareg > 0) {
			return true;
		}
		return false;

	}

	public boolean verificaExisteResparegSISRES() throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.verificaExisteResparegSISRES();

	}

	public boolean insereRespareg() throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.insereRespareg(qtdRespareg, qtdRegaresp);

	}

}