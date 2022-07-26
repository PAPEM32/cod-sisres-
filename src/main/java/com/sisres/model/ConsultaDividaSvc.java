package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sisres.utilitaria.Pager;

public class ConsultaDividaSvc {

	private DAODivida dAODivida;
//	private String usuario;
//	private String senhaCripto;

	public ConsultaDividaSvc() {
	}

	public ConsultaDividaSvc(String usuario, String senhaCripto) throws SispagException {
		dAODivida = new DAODivida();
//		this.usuario = usuario;
		// this.senhaCripto = senhaCripto;
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

	public Integer getQtdTotalDividas(String[] queryParameters) throws SispagException {
		DAODivida dAODivida = new DAODivida();

		return dAODivida.getQtdTotalDividas(queryParameters);
	}

	public Integer getQtdPaginas() {
		Pager pager = new Pager();
		return pager.getQtdPaginas();
	} 

	// **************************************************************************
	// ***
	public ArrayList<Divida> getDividaList(String[] args) throws SispagException {

		DAODivida dAODivida = new DAODivida();
		ArrayList<Divida> dividasRetorno = new ArrayList<Divida>();
		//// System.out.println("Svc - passei por aqui 1 - getDividaList");
		try {
			dividasRetorno = dAODivida.listDividas(args);
			if (!dividasRetorno.isEmpty()) {
				return dividasRetorno;
			} else {
				// System.out.println("Não há dívidas para o parâmetro informado.");
				throw new SispagException("Não existem dívidas para os parâmetros informados.");
			}

		} catch (SQLException e) {
			// System.out.println("[getDividaList]Erro de execução SQL: " + e.getMessage());
			throw new SispagException(
					"Ocorreu um erro ao executar a consulta SQL. <br>Por favor contacte o administrador do sistema.");
		}
	}
        
//        public ArrayList<DividaEmAndamento> getDividasEmAndamento(){
//            
//        }

	// **************************************************************************
	// *****
	public void getDividaDetalhe() {

	}

	public ArrayList<OC> getAllOc() throws SispagException {
		// TODO Auto-generated method stub
		return dAODivida.getAllOc();
	}

	public List<Motivo> getAllMotivo() throws SispagException {
		// TODO Auto-generated method stub
		return dAODivida.getAllMotivo();
	}

}