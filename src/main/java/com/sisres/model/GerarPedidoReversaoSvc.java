package com.sisres.model;

import java.util.List;

import com.sisres.view.GerarPedidoReversaoForm;

public class GerarPedidoReversaoSvc {

	// private DAODivida dAODivida;
	private String usuario;
	private String senhaCripto;

	public GerarPedidoReversaoSvc() {

	}

	public GerarPedidoReversaoSvc(String usuario, String senhaCripto) {
		// dAODivida = new DAODivida();
		this.usuario = usuario;
		this.senhaCripto = senhaCripto;
	}

	public Boolean gerarPedidoReversao(GerarPedidoReversaoForm myForm, String codUsuario) throws SispagException {
		// TODO Auto-generated method stub
		DAODivida daoDivida = new DAODivida();
		// System.out.println("Entrei no SVC Reversão");

		Boolean gerarPedido = false;

		try {
			gerarPedido = daoDivida.gerarPedidoReversao(myForm, codUsuario);
		} catch (SispagException e) {
			// TODO Auto-generated catch block
			throw new SispagException("Erro Criando Pedido de Reversão");
		}

		return gerarPedido;
	}

	public String[][] getGerarRelPedidoReversaoList() throws SispagException {
		// TODO Auto-generated method stub
		DAODivida daoDivida = new DAODivida();
		// System.out.println("Entrei no SVC Reversão");

		String[][] listPedidoReversao = null;

		try {
			listPedidoReversao = daoDivida.getlistPedidoReversao();
		} catch (SispagException e) {
			// TODO Auto-generated catch block
			throw new SispagException("Não Foi Possível gerar o Relátorio!");
		}

		return listPedidoReversao;
	}

	public List<Banco> getAllBanco() throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.getAllBanco();
	}

}
