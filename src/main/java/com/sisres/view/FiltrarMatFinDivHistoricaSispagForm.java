package com.sisres.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//Servlet imports
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
//Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class FiltrarMatFinDivHistoricaSispagForm extends ActionForm {

	private String matFin = null;
	private int inicioMes = -1;
	private int terminoMes = -1;
	private int inicioAno = -1;
	private int terminoAno = -1;
	private String processoInicioStr = null;
	private String processoTerminoStr = null;
	private String mesInicio = null;
	private String mesTermino = null;
	private String anoInicio = null;
	private String anoTermino = null;

	// Métodos gets e sets para os valores dos campos inseridos no JSP
	public String getMatFin() {
		return matFin;
	}

	public void setMatFin(String matFin) {
		this.matFin = matFin;
	}

	public void setProcessoInicioStr(String processoInicioStr) {
		this.processoInicioStr = processoInicioStr;
	}

	public String getProcessoInicioStr() {
		return processoInicioStr;
	}

	public void setProcessoTerminoStr(String processoTerminoStr) {
		this.processoTerminoStr = processoTerminoStr;
	}

	public String getProcessoTerminoStr() {
		return processoTerminoStr;
	}

	// Realizar as conversões de dados necessárias. Neste caso, não é preciso.

	// Implementação do Method validate, para realizar as validações (Na
	// implementação desse método deve-se codificar a inserção em um objeto
	// ActionErros todos os erros detectados durante a validação)
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		ServletContext context = getServlet().getServletContext();
//		context.log("Método validate chamado no form !");

		if (((matFin.length() == 8) || (matFin.length() == 9))
				&& ((matFin.matches("\\d\\d\\d\\d\\d\\d\\d\\d")) || (matFin.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\d")))) {
			// validaMatFin
			if (!validaMatFin(matFin)) {
				errors.add("matFin", new ActionMessage("error.campoMatFin.range"));
				// context.log("Erro na matrícula");
//        		context.log("Matrícula Financeira inválida");
			}
		} else if (matFin.length() == 0) {
			errors.add("matFin", new ActionMessage("error.campoMatFin.required"));

//        	context.log("Matrícula Financeira não informada");
		} else {

			errors.add("matFin", new ActionMessage("error.campoMatFin.range"));

//        	context.log("Tamanho da matricula Financeira é incorreta");

		}

		// Validação de anos menores que 1994

		// Validação de Mes / Ano -
		if (!(processoInicioStr.length() == 7) || (!(processoTerminoStr.length() == 7))) {
			errors.add("processoStr", new ActionMessage("error.campoprocessoStr.required"));
		} else {
			int pos = processoInicioStr.indexOf("/");
			mesInicio = processoInicioStr.substring(0, pos);
			anoInicio = processoInicioStr.substring(pos + 1);

			int poss = processoTerminoStr.indexOf("/");
			mesTermino = processoTerminoStr.substring(0, poss);
			anoTermino = processoTerminoStr.substring(poss + 1);

			try {
				inicioMes = Integer.parseInt(mesInicio);
//				context.log("Mês inicio válido");
				// //System.out.println(this.mesInicio);
			} catch (NumberFormatException nfe) {
				errors.add("mesini", new ActionMessage("error.campoDataIni.range"));
//				context.log("Mês inicio inválido");
			}

			try {
				terminoMes = Integer.parseInt(mesTermino);
//				context.log("Mês término válido");
				// //System.out.println(this.mesTermino);
			} catch (NumberFormatException nfe) {
				errors.add("mesterm", new ActionMessage("error.campoDataTerm.range"));

//				context.log("Mês término inválido");
			}

			if ((inicioMes != -1) && (inicioMes < 1) || (inicioMes > 12)) {
				errors.add("mesini", new ActionMessage("error.campoDataIni.range"));
			}

			if ((terminoMes != -1) && (terminoMes < 1) || (terminoMes > 12)) {
				errors.add("mesterm", new ActionMessage("error.campoDataTerm.range"));
			}

			// Validação de Ano
			try {
				inicioAno = Integer.parseInt(anoInicio);
			} catch (NumberFormatException nfe) {
				errors.add("anoini", new ActionMessage("error.campoDataTerm.invalid"));
			}

			try {
				terminoAno = Integer.parseInt(anoTermino);
			} catch (NumberFormatException nfe) {
				errors.add("anoterm", new ActionMessage("error.campoDataTerm.invalid"));
			}

			if ((inicioAno < 1994) || (terminoAno < 1994)) {
				errors.add("processoANO", new ActionMessage("error.campoprocessoANO.required"));
			}

			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date mesAnoInicio = null;
			try {
				mesAnoInicio = (Date) format.parse("01/" + mesInicio + "/" + anoInicio);
			} catch (ParseException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}

			Date mesAnoTermino = null;
			try {
				mesAnoTermino = (Date) format.parse("01/" + mesTermino + "/" + anoTermino);
			} catch (ParseException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}

			if (mesAnoInicio.after(mesAnoTermino)) {
				errors.add("anoterm", new ActionMessage("error.campoDataIniTerm.invalid"));
//				context.log("Período inicial maior que período final");
				// System.out.println(mesAnoInicio);
			}

			Date today = new Date();
			if (mesAnoTermino.after(today)) {
				errors.add("anoterm", new ActionMessage("error.campoDataTermmaiorcgs.invalid"));
//				context.log("Período ultrapassa o ano corrente");
				// System.out.println(mesAnoTermino);
			}

		}

		return errors;
	}

	public boolean validaMatFin(String matFin) {
		int vn1, vn2, vn3, vn4, vn5, vn6, vn7, vn8, vresto, vsoma, vdv;

		vn1 = 0;
		vn2 = 0;
		vn3 = 0;
		vn4 = 0;
		vn5 = 0;
		vn6 = 0;
		vn7 = 0;
		vn8 = 0;
		vresto = 0;
		vsoma = 0;
		vdv = -1;
		boolean result = false;
		String nip = matFin.substring(0, 8);

		// System.out.println("Entrei no valida matfin");

		// valida as 8 posições do NIP
		if (nip.length() == 8) {
			vn1 = Character.getNumericValue(nip.charAt(0)) * 8;
			vn2 = Character.getNumericValue(nip.charAt(1)) * 7;
			vn3 = Character.getNumericValue(nip.charAt(2)) * 6;
			vn4 = Character.getNumericValue(nip.charAt(3)) * 5;
			vn5 = Character.getNumericValue(nip.charAt(4)) * 4;
			vn6 = Character.getNumericValue(nip.charAt(5)) * 3;
			vn7 = Character.getNumericValue(nip.charAt(6)) * 2;
			vn8 = Character.getNumericValue(nip.charAt(7));

			vsoma = vn1 + vn2 + vn3 + vn4 + vn5 + vn6 + vn7;

			vresto = vsoma % 11;

			// System.out.println(Integer.parseInt(nip.substring(0,1)));
			// System.out.println(Integer.parseInt(nip.substring(0,1)) * 8);
			// System.out.println(vsoma);
			// System.out.println(vresto);

			if (vresto == 1) {
				vdv = 0;
			} else if (vresto == 0) {
				vdv = 1;
			} else if (vresto > 1) {
				vdv = 11 - vresto;
			}

			if (vn8 == vdv) {
				result = true;
			}
		}

		if (nip.length() == 9) {

			// System.out.println("tem 9 digito");

			result = true;

		}

		return result;
	}

}
