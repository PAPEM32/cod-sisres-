package com.sisres.model;

public class IncluirReversaoSvc {

	private boolean reversaoOk = false;

	// private Lancamento lanc;

	/*
	 * public boolean incReversao(int numeroDocAutorizacao, String
	 * tipoDocAutorizacao, String dataDocAutorizacao, String origemDocAutorizacao,
	 * String operador, double valor, String observacao, int divId, int tipo, //int
	 * lancamentoRevertidoId, String codUsuario) throws SispagException {
	 */
	public boolean incReversao(String numeroDocAutorizacao, String tipoDocAutorizacao, String dataDocAutorizacao,
			String origemDocAutorizacao, String operador, double valor, String observacao, int divId, int tipo,
			// int lancamentoRevertidoId,
			String codUsuario) throws SispagException {

		DAODivida daoDivida = new DAODivida();
		// Lancamento reversao = new Lancamento();
		boolean reversao = false;

		if (daoDivida.verificarReversao(numeroDocAutorizacao, dataDocAutorizacao, origemDocAutorizacao, divId)) {

			reversao = daoDivida.incluirReversao(numeroDocAutorizacao, tipoDocAutorizacao, dataDocAutorizacao,
					origemDocAutorizacao, operador, valor, observacao, divId, tipo,
					// lancamentoRevertidoId,
					codUsuario);

			// System.out.println("Lançamento de reversão incluído com sucesso!");
			// return reversao;
		} else { // se retornar false
			// System.out.println("Entrei aqui SispagException");
			throw new SispagException("Lançamento de reversão já cadastrado!");
		}

		return reversao;

	}

	public Divida atualizaValorDivida(Divida divida) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.buscaValorDivida(divida);

	}
}
