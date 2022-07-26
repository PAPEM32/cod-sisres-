package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

@SuppressWarnings("deprecation")
public class IncluirReversaoForm extends ActionForm {

	private int numeroDocAutorizacao = 0;
	private String numeroDocAutorizacaoStr = null;
	private String tipoDocAutorizacao = null;
	private String dataDocAutorizacao = null;
	private String origemDocAutorizacao = null;
	private double valor = 0.0;
	private String valorStr = null;
	private String observacao = null;

	public void setdataDocAutorizacao(String dataDocAutorizacao) {
		this.dataDocAutorizacao = dataDocAutorizacao;
	}

	public String getDataDocAutorizacao() {
		return dataDocAutorizacao;
	}

	public int getNumeroDocAutorizacao() {
		return numeroDocAutorizacao;
	}

	public String getNumeroDocAutorizacaoStr() {
		return numeroDocAutorizacaoStr;
	}

	public void setNumeroDocAutorizacaoStr(String numeroDocAutorizacaoStr) {
		this.numeroDocAutorizacaoStr = numeroDocAutorizacaoStr;
	}

	public String getTipoDocAutorizacao() {
		return tipoDocAutorizacao;
	}

	public void setTipoDocAutorizacao(String tipoDocAutorizacao) {
		this.tipoDocAutorizacao = tipoDocAutorizacao;
	}

	public String getOrigemDocAutorizacao() {
		return origemDocAutorizacao;
	}

	public void setOrigemDocAutorizacao(String origemDocAutorizacao) {
		this.origemDocAutorizacao = origemDocAutorizacao;
	}

	public String getValorStr() {
		return valorStr;
	}

	public void setValorStr(String valorStr) {
		this.valorStr = valorStr;
	}

	public double getValor() {
		return valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();
//		context.log("Método validate(Incluir Reversao) chamado!");

		if (valorStr == null || valorStr.equals("")) {
			errors.add("valor", new ActionMessage("error.campovalor.required"));
		} else {

			// System.out.println(valorStr);

			String Nvalor = valorStr.replaceAll("[.]", "");

			// System.out.println(Nvalor);

			Nvalor = Nvalor.replaceAll(",", ".");

			// System.out.println(Nvalor);

			valor = Double.parseDouble(Nvalor);

			// System.out.println("Nvalor : " +Nvalor);
			// System.out.println("TESTE valor : " +valor);

			if (valor < 0.0) {
				errors.add("valor", new ActionMessage("error.valor.range"));
//				context.log("Valor do lançamento negativo");
			}

		}

		// ===============Transformação da Data========================//
		if (dataDocAutorizacao.length() == 0) {
			errors.add("dataDocAutorizacao", new ActionMessage("error.dataDocAutorizacao.required"));

//			context.log("Data do Documento de Autorização não informada");
		} else {
			if (!validaData(dataDocAutorizacao)) {
				errors.add("dataDocAutorizacao", new ActionMessage("error.dataDocAutorizacao.range"));
				// System.out.println(dataDocAutorizacao);

//				context.log("Data do Documento de Autorização inválida");

			} else {
				String[] arrayData = dataDocAutorizacao.trim().split("/");
				dataDocAutorizacao = arrayData[2].trim() + "-" + arrayData[1].trim() + "-" + arrayData[0].trim();
			}
		}

		/*
		 * try {
		 * 
		 * numeroDocAutorizacao = Integer.parseInt(numeroDocAutorizacaoStr.trim()); if
		 * (numeroDocAutorizacao <= 0) { errors.add("numeroDocAutorizacao", new
		 * ActionMessage("error.numeroDocAutorizacao.range"));
		 * context.log("Número da autorização inválido"); } } catch
		 * (NumberFormatException e) { errors.add("numeroDocAutorizacao", new
		 * ActionMessage("error.numeroDocAutorizacao.required"));
		 * context.log("Número da autorização não preenchido"); }
		 * 
		 */
		if (numeroDocAutorizacaoStr.length() == 0) {
			errors.add("numeroDocAutorizacao", new ActionError("error.numeroDocAutorizacao.required"));
		}

		if (tipoDocAutorizacao.length() == 0) {
			errors.add("tipoDocAutorizacao", new ActionError("error.campoTipoDocAut.required"));
		}

		if (origemDocAutorizacao.length() == 0) {
			errors.add("origemDocAutorizacao", new ActionError("error.origemDocAutorizacao.required"));
		}

		/*
		 * Campo não é mais obrigatório if (observacao.length() == 0) {
		 * errors.add("observacao", new ActionError("error.observacao.required")); }
		 */
		return errors;
	}

	// ==============Método para Validar Data==========================//

	public boolean validaData(String data) {
		boolean result = true;
		String[] arrayData = data.split("/");
		try {
			int dia = Integer.parseInt(arrayData[0]);
			int mes = Integer.parseInt(arrayData[1]);
			int ano = Integer.parseInt(arrayData[2]);

			int bisex = ano % 4;

			switch (mes) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if ((dia <= 0) || (dia > 31)) {
					result = false;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if ((dia <= 0) || (dia > 30)) {
					result = false;
				}
				break;
			case 2:
				if (bisex == 0) {
					if ((dia <= 0) || (dia > 29)) {
						result = false;
					}
				} else {
					if ((dia <= 0) || (dia > 28)) {
						result = false;
					}
				}
				break;
			default:
				result = false;
			}// fim do switch

		} catch (NumberFormatException nfe) {
			result = false;
		}
		return result;
	}
}
