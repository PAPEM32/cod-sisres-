package com.sisres.model;

import java.sql.Date;
import java.util.List;

import com.sisres.view.ConfirmarDividaMesForm;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author HERMINIO
 * @generated "UML para Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ConfirmarDividaMesSvc {

	private DAODivida dAODivida;
	private String usuario;
	private String senhaCripto;

	// Construtor
	public ConfirmarDividaMesSvc(String usuario, String senhaCripto) throws SispagException {
		dAODivida = new DAODivida();
		this.usuario = usuario;
		this.senhaCripto = senhaCripto;
	}

	/*
	 * public Divida confirmarDividaHistorica(String codDivida, String matFin,
	 * Double valorDivida, String origemDocAut, String tipoDocAut, int
	 * numeroDocAutInt, String dataDocAut, String tipoDocEnvio, int
	 * numeroDocEnvioInt, String DataDocEnvio,String dataMotivo, int idMotivo,
	 * String motivo) throws SispagException{
	 */
	public Divida confirmarDividaMes(Divida divida, ConfirmarDividaMesForm myForm, String codUsuario)
			throws SispagException {

		/*
		 * Dados relativos ao documento de autorização da dívida Essas informações estão
		 * contidas no lançamento inicial da dívida
		 */

		String[] arrayData1 = myForm.getDataDocAut().split("/");
		String dia1 = arrayData1[0];
		String mes1 = arrayData1[1];
		String ano1 = arrayData1[2];
		String dataDocAutStr = ano1 + "-" + mes1 + "-" + dia1;
		Date dataDocAut = Date.valueOf(dataDocAutStr);

//		int numeroDocAut = Integer.parseInt(myForm.getNumeroDocAut());
		String numeroDocAut = myForm.getNumeroDocAut();
		String tipoDocAut = myForm.getTipoDocAut();
		String origemDocAut = myForm.getOrigemDocAut();

		/*
		 * DateFormat df = new SimpleDateFormat("dd'/'mm'/'yyyy"); java.util.Date
		 * dataDocEnvioTeste; try { dataDocEnvioTeste =
		 * df.parse(myForm.getDataDocEnvio()); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		// Dados relativos ao documento de envio para confirmação da dívida
		/*
		 * String []arrayData2 = myForm.getDataDocEnvio().split("/"); String dia2 =
		 * arrayData2[0]; String mes2 = arrayData2[1]; String ano2 = arrayData2[2];
		 * String dataDocEnvioStr = ano2 + "-" + mes2 + "-" + dia2;
		 * 
		 * Date dataDocEnvio = Date.valueOf(dataDocEnvioStr);
		 */

		// Date dataDocEnvio = new Date(ano2,mes2,dia2);
//		int numeroDocEnvio = Integer.parseInt(myForm.getNumeroDocEnvio());

		dAODivida.confirmaAtualizaLancamentoMensal(divida, dataDocAut, numeroDocAut, tipoDocAut, origemDocAut,
				codUsuario);

		return divida;

	}

	public List<Divida> getAllDividaMesEmEspera() throws SispagException {
		return dAODivida.getAllDividaMesEmEspera();
	}
}