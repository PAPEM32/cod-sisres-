package com.sisres.model;

public class CadastrarDividaMesSVC {

	private DAODivida daoDivida;

	public Divida confirmaDivMes(String sistemaOrigem, String perfil, String matFin, String mesProcessoPagto,
			String anoProcessoPagto, String nome, String cpf, String posto, String situacao, String codigoOc,
			String nomeOc, String banco, String agencia, String contaCorrente, Double valorDivida, String origemDocAut,
			String tipoDocAut, String numeroDocAut, String dataDocAut, String dataMotivo, String motivo,
			String observacao, String codUsuario) throws SispagException {

		daoDivida = new DAODivida();

		// System.out.println("perfil no SVC: " + perfil);

		int idOc = daoDivida.readOC(codigoOc).getId();
		String tipoDivida = "1"; // 1 - Tipo divida MES || 2 - Tipo divida historico

		// String sistemaOrigem = "S"; //S --> Dívida oriunda do SISPAG | N --> Dívida
		// oriunda do SIAPE

		Divida divida = new Divida(matFin, mesProcessoPagto, anoProcessoPagto, cpf, nome, posto, situacao, idOc,
				codigoOc, nomeOc, banco, agencia, contaCorrente, valorDivida, origemDocAut, tipoDocAut, numeroDocAut,
				dataDocAut, dataMotivo, motivo, observacao, sistemaOrigem);

		String estado = null;

		if ((perfil.equals("ADMINISTRADOR")) || (perfil.equals("PAPEM-23"))) {

			estado = "CONCLUIDO";
		} else {

			estado = "EM ESPERA";
		}

		// pessoaDiv = divida.getPessoa();

		if (daoDivida.readDivida(divida.getCgs(), matFin, divida.getOc(), divida.getMesProcessoGeracao(),
				divida.getAnoProcessoGeracao()) == null) {
			// System.out.println("Read Divida retornou null");

			try {
				daoDivida.insereDivida(divida, tipoDivida, estado, codUsuario);
			} catch (SispagException e) {
				throw new SispagException("Erro inserindo em Dívida!");
			}

			// System.out.println("Inseriu a divida!");

		} else {
			throw new SispagException("Dívida já cadastrada no SISRES!");
		}

		return divida;
		// end-user-code
	}

}
