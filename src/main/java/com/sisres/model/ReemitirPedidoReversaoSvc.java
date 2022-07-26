package com.sisres.model;

import java.sql.SQLException;

public class ReemitirPedidoReversaoSvc {

	private DAODivida daoDivida;

	public Integer obterIdPedidoReversao(Integer codigoPedidoReversao, Integer ano) throws SispagException {
		daoDivida = new DAODivida();
		Integer retorno = null;
		retorno = daoDivida.obterIdPedidoReversao(codigoPedidoReversao, ano);
		if (retorno == null) {
			throw new SispagException("Código do pedido de reversão não encontrado");
		}
		return retorno;
	}

	public String[][] recuperarListaDividasPedidoReversao(Integer idPedidoCodigoReversao, Integer ano)
			throws SispagException, SQLException {
		daoDivida = new DAODivida();
		String[][] retorno = null;
		retorno = daoDivida.recuperarListaDividasPedidoReversao(idPedidoCodigoReversao, String.valueOf(ano));
		if (retorno == null) {
			throw new SispagException("Não existem dívidas para o código de reversão informado.");
		}
		return retorno;
	}

	public DAODivida getDaoDivida() {
		return daoDivida;
	}

	public void setDaoDivida(DAODivida daoDivida) {
		this.daoDivida = daoDivida;
	}

}
