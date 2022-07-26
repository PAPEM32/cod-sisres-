package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CadDivHistoricaSiapeSvc extends CadastrarDividaHistoricaSVC {

	private DAOBanco daoBanco;
	private DAODivida daoDivida;
	private DAOSituacao daoSituacao;
	private String usuario;
	private String senhaCripto;

	public CadDivHistoricaSiapeSvc(String usuario, String senhaCripto) {
		daoBanco = new DAOBanco();
		this.usuario = usuario;
		this.senhaCripto = senhaCripto;
	}

	public List<Banco> obterListaBancos() throws SispagException, SQLException {
		daoBanco = new DAOBanco();
		List<Banco> listaRetorno = daoBanco.obterListaBancos();
		if (listaRetorno.size() == 0) {
			throw new SispagException("Não existem bancos cadastrados");
		}

		return listaRetorno;
	}

	public List<Motivo> obterMotivos() throws SispagException {
		daoDivida = new DAODivida();
		List<Motivo> listaRetono = new ArrayList<Motivo>();
		listaRetono = daoDivida.getAllMotivo();
		if (listaRetono.size() == 0) {
			throw new SispagException("Não existem Motivos cadastrados");
		}
		return listaRetono;
	}

	public List<OC> obterOc() throws SispagException {
		daoDivida = new DAODivida();
		List<OC> listaRetono = new ArrayList<OC>();
		listaRetono = daoDivida.getAllOc();
		if (listaRetono.size() == 0) {
			throw new SispagException("Não existem OC cadastrados");
		}
		return listaRetono;
	}

	public List<Situacao> obterSituacoes() throws SispagException, SQLException {
		daoSituacao = new DAOSituacao();
		List<Situacao> listaRetorno = new ArrayList<Situacao>();
		listaRetorno = daoSituacao.obterListaSituacoes();
		if (listaRetorno.size() == 0) {
			throw new SispagException("Não existem bancos cadastrados");
		}
		return listaRetorno;
	}
}
