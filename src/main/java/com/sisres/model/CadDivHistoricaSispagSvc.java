/**
 * 
 */
package com.sisres.model;

import java.util.ArrayList;
import java.util.List;

import com.sisres.externo.AcessoSispag;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author QUEIROZ
 * @generated "UML para Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class CadDivHistoricaSispagSvc extends CadastrarDividaHistoricaSVC {

	private DAODivida dAODivida;
	

	public CadDivHistoricaSispagSvc(String usuario, String senhaCripto) throws SispagException {
		dAODivida = new DAODivida();
	}

	/**
	 * Metodo que retornarï¿½ a divida em um array de 3 dimensï¿½es para que possa
	 * conferir se a divida esta quebrada por oc ou cc Dados de entrada
	 * 
	 * @param matFin
	 * @param processoInicioStr
	 * @param processoTerminoStr
	 * @return
	 * @throws SispagException
	 * 
	 *                         OBS Meu ARRAY 1ï¿½ OC 2ï¿½ Conta Corrente 3ï¿½ DADOS
	 *                         DA DIVIDA
	 * 
	 */

	public String[][][] getDivHistoricaSISPAG(String matFin, String processoInicioStr, String processoTerminoStr)
			throws SispagException {

		String[][][] div_OC_CC = null;

		AcessoSispag sispagInterfaceImpl = new AcessoSispag();

		div_OC_CC = sispagInterfaceImpl.getDividaHist(matFin, processoInicioStr, processoTerminoStr);

		return div_OC_CC;

	}

	public Divida getDadosDivHistoricaSISPAG(String matFin, String dataIni, String dataFim) throws SispagException {
		Divida dadosdDividaHist = null;
		AcessoSispag sispagInterfaceImpl = new AcessoSispag();

		dadosdDividaHist = sispagInterfaceImpl.getDadosDivHistorica_cgs2(matFin, dataIni, dataFim);

		if (dadosdDividaHist == null) {
			throw new SispagException("Problema com a respectiva Divida!");
		} else {
			dadosdDividaHist.setOc(dAODivida.readOC(dadosdDividaHist.getOc().getOc()));
		}
		return dadosdDividaHist;

	}

	public List<Motivo> getAllMotivo() throws SispagException {
		return dAODivida.getAllMotivo();
	}

	public ArrayList<OC> getAllOc() throws SispagException {
		return dAODivida.getAllOc();
	}

	public String[][][] verificaDivCadastrada(String[][][] div_OC_CC, String matFin, String processoInicioStr,
			String processoTerminoStr) throws SispagException {

		int qtdi = 0;
		int qtdj = 0;

		// System.out.println("Tamanho do length"+div_OC_CC.length);

		String[][][] div_OC_CC_Ver = null;

		qtdi = div_OC_CC.length;

		div_OC_CC_Ver = new String[qtdi][][];

		for (int i = 0; i < div_OC_CC.length; i++) {

			qtdj = div_OC_CC[i].length;

			div_OC_CC_Ver[i] = new String[qtdj][8];

			qtdj = 0;

		}

		String mesIni = null; // processoInicioStr.substring(0, 2);
		String anoIni = null; // processoInicioStr.substring(3, 7);

		for (int i = 0; i < div_OC_CC.length; i++) {

			for (int j = 0; j < div_OC_CC[i].length; j++) {

				div_OC_CC_Ver[i][j][0] = div_OC_CC[i][j][0];
				div_OC_CC_Ver[i][j][1] = div_OC_CC[i][j][1];
				div_OC_CC_Ver[i][j][2] = div_OC_CC[i][j][2];
				div_OC_CC_Ver[i][j][3] = div_OC_CC[i][j][3];
				div_OC_CC_Ver[i][j][4] = div_OC_CC[i][j][4];
				div_OC_CC_Ver[i][j][5] = div_OC_CC[i][j][5];
				div_OC_CC_Ver[i][j][6] = div_OC_CC[i][j][6];

				OC oc = dAODivida.readOC(div_OC_CC[i][j][0].trim());

				anoIni = div_OC_CC[i][j][3].substring(0, 4);
				mesIni = div_OC_CC[i][j][3].substring(4, 6);

				// TODO - REESCREVER TRECHO ABAIXO - TEN LUIS FERNANDO
				if (dAODivida.readDivida("", matFin, oc, mesIni, anoIni) == null) {
					div_OC_CC_Ver[i][j][7] = "N";
				} else {
					div_OC_CC_Ver[i][j][7] = "S";
				}
			}
		}
		
		return div_OC_CC_Ver;
	}

}
