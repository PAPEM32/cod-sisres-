package com.sisres.model;

public class CadastrarDividaHistoricaSVC {

	private DAODivida daoDivida;
	private static final String TIPO_DIVIDA_HISTORICA = "2";

	public Divida confirmaDividaHistorica(Divida dividaFormulario, String codUsuario, String estado)
			throws SispagException {

		daoDivida = new DAODivida();
		dividaFormulario.getOc().setId(daoDivida.readOC(dividaFormulario.getOc().getOc()).getId());
		String tipoDivida = TIPO_DIVIDA_HISTORICA;

		// readDivida - se retornar false, posso inserir a dívida no SISRES
		if (daoDivida.readDivida(dividaFormulario.getCgs(),
				dividaFormulario.getCgs().equals("S") ? dividaFormulario.getPessoa().getMatFin()
						: dividaFormulario.getPessoa().getMatSIAPE(),
				dividaFormulario.getOc(), dividaFormulario.getMesProcessoGeracao(),
				dividaFormulario.getAnoProcessoGeracao()) == null) {

			// insereDivida
			System.out.println("Read Divida retornou null");

			try {
				daoDivida.insereDivida(dividaFormulario, tipoDivida, estado, codUsuario);
			} catch (SispagException e) {
				// TODO: handle exception
				throw new SispagException("Erro ao inserir a Dívida!");
			}

			// System.out.println("Inseriu a divida!");

		} else { // se retornar null, lanço exceção
			throw new SispagException("Dívida já cadastrada no SISRES!");
		}

		return dividaFormulario;
		// end-user-code
	}

}
