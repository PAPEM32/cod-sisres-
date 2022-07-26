/**
 * 
 */
package com.sisres.model;

import java.util.ArrayList;
import java.util.List;

import com.sisres.externo.AcessoSispag;

public class CadDivMesSispagSvc extends CadastrarDividaMesSVC {

	private DAODivida dAODivida;
	
	private static final String SISTEMA_ORIGEM_SISPAG = "S";
	private static final String SISTEMA_ORIGEM_SIAPE = "N";

	// Construtor
	public CadDivMesSispagSvc(String usuario, String senhaCripto) throws SispagException {
		dAODivida = new DAODivida();

	}

	public Divida getDivMesSISPAG(String matFin) throws SispagException {
		// begin-user-code
		// TODO Stub de método gerado automaticamente

		boolean verificaDisponibilidadeSISPAG = false;
		Divida dadosdDivida = null;
		// AcessoSispag sispagInterfaceImpl = new AcessoSispag(usuario,senhaCripto);
		AcessoSispag sispagInterfaceImpl = new AcessoSispag();

		// VerificarDisponibilidade
		verificaDisponibilidadeSISPAG = sispagInterfaceImpl.verificarDisponibilidade();

		// Se o SISPAG não estiver disponível, lanão uma exceï¿½ï¿½o. Caso contrï¿½rio,
		// chamo getDivida
		if (!verificaDisponibilidadeSISPAG) {
			throw new SispagException("O SISPAG não se encontra disponível!");
		} else {
			dadosdDivida = sispagInterfaceImpl.getDivida(matFin);

			// Se dadosdDivida == null, lanão exceï¿½ï¿½o
			if (dadosdDivida == null) {
				throw new SispagException("Matrícula Financeira não encontrada no SISPAG!");
			} else {
				dadosdDivida.setOc(dAODivida.readOC(dadosdDivida.getOc().getOc()));
			}
		}

		return dadosdDivida;
	}

	public List<Motivo> getAllMotivo() throws SispagException {
		return dAODivida.getAllMotivo();
	}

	public ArrayList<OC> getAllOc() throws SispagException {
		// TODO Auto-generated method stub
		return dAODivida.getAllOc();
	}

}