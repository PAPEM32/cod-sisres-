package com.sisres.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.sisres.view.FiltrarPedidoReversaoForm;

public class FiltrarPedidoReversaoSvc {

	public ArrayList<Divida> getDividaReversaoList(FiltrarPedidoReversaoForm myForm) throws SispagException {
		// TODO Auto-generated method stub

		// System.out.println("Entrei no service");

		DAODivida dAODivida = new DAODivida();
		ArrayList<Divida> DividasReversao = new ArrayList<Divida>();
		//// System.out.println("Svc - passei por aqui 1");
		try {
			DividasReversao = dAODivida.listDividasReversao(myForm);

			/* todo: P32 Divida */
			if (!DividasReversao.isEmpty()) {
				return DividasReversao;
			} else { // se retornar false
				// System.out.println("Entrei aqui SispagException");
				throw new SispagException("Não existe dívidas para o tipo e banco informado!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return DividasReversao;
	}

}
