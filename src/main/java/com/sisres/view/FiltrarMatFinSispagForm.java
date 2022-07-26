package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
//Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class FiltrarMatFinSispagForm extends ActionForm {

	private String matFin = null;

	// Métodos gets e sets para os valores dos campos inseridos no JSP
	public String getMatFin() {
		return matFin;
	}

	public void setMatFin(String matFin) {
		this.matFin = matFin;
	}

	// Realizar as conversões de dados necessárias. Neste caso, não é preciso.

	// Implementação do Method validate, para realizar as validações (Na
	// implementação desse método deve-se codificar a inserção em um objeto
	// ActionErros todos os erros detectados durante a validação)
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		ServletContext context = getServlet().getServletContext();
//        context.log("Método validate chamado!");
//        context.log("Valor do matfin:" + matFin);
		// Form verification
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
