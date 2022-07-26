package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

//*******************************************************************************
public class ConsultaLancamentoSvc {

	private DAODivida dAODivida;
	private ArrayList<Lancamento> LancamentosRetorno = new ArrayList<Lancamento>();

//*******************************************************************************
	public DAODivida getDAODivida() {
		return dAODivida;
	}

	public void setDAODivida(DAODivida dAODivida) {
		this.dAODivida = dAODivida;
	}

//*******************************************************************************
	public ArrayList<Lancamento> readLancamentosporDivida(String dividaID) throws SispagException {

		DAODivida dAODivida = new DAODivida();

		try {
			LancamentosRetorno = dAODivida.readLancamentosporDivida(dividaID);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return LancamentosRetorno;

	}

//*******************************************************************************
	public void getLancamentoDetalhe() {

	}
}
