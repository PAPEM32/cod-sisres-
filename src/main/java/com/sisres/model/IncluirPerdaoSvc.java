package com.sisres.model;

import java.util.Date;

public class IncluirPerdaoSvc {

	/**
	 * @param args
	 */

	public double getSaldoRegularizar(int codDivida) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.verificaSaldoRegularizar(codDivida);

	}

	public boolean setLancamentoPerdao(Lancamento lancamento) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.inserirLancamentoPerdao(lancamento);
	}

	public static void main(String[] args) throws SispagException {
		// TODO Auto-generated method stub
		// dado para teste

		Lancamento lancamento = new Lancamento();
		lancamento.setNumeroDocAutorizacao("");
		lancamento.setTipoDocAutorizacao("Oficio");
		lancamento.setDataDocAutorizacao(new Date());
		lancamento.setOrigemDocAutorizacao("000000&&&&");
		lancamento.setTipo("4");
		lancamento.setOperador("C");
		lancamento.setValor(10.0);
		lancamento.setObservacao("Primeiro lanc PERDAO");
		lancamento.setResponsavel("Rigaud");
		lancamento.setDividaId(310);// pesquisar

		// testando funcoes
		IncluirPerdaoSvc inPerdSvc = new IncluirPerdaoSvc();
		Double saldo = inPerdSvc.getSaldoRegularizar(lancamento.getDividaId());
		// teste que deverá ser realizado no controller
		// System.out.println(saldo);
		if (saldo < 0) {
			// System.out.println("A Dívida possui Saldo a Regularizar e não pode haver
			// devolução");
			// System.out.println(saldo);
			return;
		}
		if (saldo < lancamento.getValor()) {
			// System.out.println("Valor do Lançamento de Perdão maior do que o Saldo a
			// Regularizar da Dívida");
			return;
		}
		// System.out.println(inPerdSvc.setLancamentoPerdao(lancamento));

	}

}
