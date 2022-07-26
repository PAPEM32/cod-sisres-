package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sisres.utilitaria.Pager;

public class ConsultaReversaoSvc {

	private DAODivida dAODivida;
	private String usuario;
	private String senhaCripto;

	public ConsultaReversaoSvc() {
	}

	public ConsultaReversaoSvc(String usuario, String senhaCripto) throws SispagException {
		dAODivida = new DAODivida();
		this.usuario = usuario;
		this.senhaCripto = senhaCripto;
	}

	// **************************************************************************
	// ***
	public DAODivida getDAODivida() {

		return dAODivida;

	}

	// **************************************************************************
	// ***
	public void setDAODivida(DAODivida dAODivida) {

		this.dAODivida = dAODivida;

	}

	public Integer getQtdTotalReversao(String[] queryParameters) throws SispagException {
		DAODivida dAODivida = new DAODivida();

		return dAODivida.getQtdTotalReversao(queryParameters);
	}

	public Integer getQtdPaginas() {
		Pager pager = new Pager();
		return pager.getQtdPaginas();
	}

	// **************************************************************************

	public ArrayList<Divida> getListReversao(String[] args) throws SispagException {

		DAODivida dAODivida = new DAODivida();
		ArrayList<Divida> dividasRetorno = new ArrayList<Divida>();
		//// System.out.println("Svc - passei por aqui 1 - getDividaList");
		try {
			dividasRetorno = dAODivida.listReversao(args);
			if (!dividasRetorno.isEmpty()) {
				return dividasRetorno;
			} else {
				// System.out.println("Não há reversões para o parâmetro informado.");
				throw new SispagException("Não existem reversões para os parâmetros informados.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;

	}

	// **************************************************************************
	// *****
	public void getPedidoDetalhe() {

	}

}