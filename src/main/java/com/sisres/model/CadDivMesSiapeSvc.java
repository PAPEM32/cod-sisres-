package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CadDivMesSiapeSvc extends CadastrarDividaMesSVC {

	private DAOBanco daoBanco;
	private DAODivida daoDivida;
	private DAOSituacao daoSituacao;
	private String processo;
	// mesProcessoGeracao;
	// private String anoProcessoGeracao;
	private String usuario;
	private String senhaCripto;

	public CadDivMesSiapeSvc(String usuario, String senhaCripto) {
		daoBanco = new DAOBanco();
		this.usuario = usuario;
		this.senhaCripto = senhaCripto;

	}

	/*
	 * public String obterProcesso() throws SispagException, SQLException{
	 * daoDivida= new DAODivida(); processo = daoDivida.buscarProcessoDivida();
	 * 
	 * return processo; }
	 */

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

	public Divida obterProcesso() throws SispagException {

		Divida dividaRetorno = new Divida();
		Date data = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(data);
		int mes = dataCal.get(Calendar.MONTH); // 0 - JANEIRO, 11 - DEZEMBRO
		mes++; // ajusta os meses --> 1 - JANEIRO, 12 - DEZEMBRO
		int mesAnterior = mes - 1;
		int ano = dataCal.get(Calendar.YEAR);
		if (mesAnterior <= 0) {
			mesAnterior = 12;
			ano = ano - 1;
		}
		dividaRetorno.setAnoProcessoGeracao(Integer.toString(ano));
		dividaRetorno.setMesProcessoGeracao(Integer.toString(mesAnterior));
		return dividaRetorno;
	}

}
