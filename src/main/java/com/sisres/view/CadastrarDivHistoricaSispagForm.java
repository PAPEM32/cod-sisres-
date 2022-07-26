package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
//Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class CadastrarDivHistoricaSispagForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String matFin = null;
	private String codDivida = null;
	private String processoInicioStr = null;
	private String processoTerminoStr = null;
	private String mesini = null;
	private String mesterm = null;
	private String anoini = null;
	private String anoterm = null;
	private int iniMes = -1;
	private int terMes = -1;
	private int iniAno = -1;
	private int terAno = -1;
	private Double valorDivida = 0.0;
	private String valorDividaStr = null;

	private String nome = null;
	private String cpf = null;
	private String posto = null;
	private String situacao = null;
//	private int    idOc = -1;
	private String ocCodigo = null;
	private String ocNome = null;

	private String banco = null;
	private String agencia = null;
	private String contaCorrente = null;

	private String origemDocAut = null;
	private String tipoDocAut = null;
	private String numeroDocAut = null;
	private String dataDocAut = null;

	private String motivo = null;
	private String dataMotivo = null;
	private String observacao = null;

	private String tipoDocEnvio = null;
	private String dataDocEnvio = null;
	private String numDocEnvio = null;

	// Métodos gets e sets para os valores dos campos inseridos no JSP

	public void setMatFin(String matFin) {
		this.matFin = matFin;
	}

	public String getMatFin() {
		return matFin;
	}

	public void setCodDivida(String codDivida) {
		this.codDivida = codDivida;
	}

	public String getCodDivida() {
		return codDivida;
	}

	public void SetprocessoiniStr(String processoInicioStr) {
		this.processoInicioStr = processoInicioStr;
	}

	public String getProcessoiniStr() {
		return processoInicioStr;
	}

	public void SetprocessotermStr(String processoTerminoStr) {
		this.processoTerminoStr = processoTerminoStr;
	}

	public String getProcessotermStr() {
		return processoTerminoStr;
	}

	public String getMesIni() {
		return mesini;
	}

	public String getMesTerm() {
		return mesterm;
	}

	public void setMesIni(String mesini) {
		this.mesini = mesini;
	}

	public void setMesTerm(String mesterm) {
		this.mesterm = mesterm;
	}

	public String getAnoIni() {
		return anoini;
	}

	public String getAnoTerm() {
		return anoterm;
	}

	public void setAnoIni(String anoini) {
		this.anoini = anoini;
	}

	public void setAnoTerm(String anoterm) {
		this.anoterm = anoterm;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPosto() {
		return posto;
	}

	public void setPosto(String posto) {
		this.posto = posto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getOcCodigo() {
		return ocCodigo;
	}

	public void setOcCodigo(String ocCodigo) {
		this.ocCodigo = ocCodigo;
	}

	public String getOcNome() {
		return ocNome;
	}

	public void setOcNome(String ocNome) {
		this.ocNome = ocNome;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public String getOrigemDocAut() {
		return origemDocAut;
	}

	public void setOrigemDocAut(String origemDocAut) {
		this.origemDocAut = origemDocAut;
	}

	public String getTipoDocAut() {
		return tipoDocAut;
	}

	public void setTipoDocAut(String tipoDocAut) {
		this.tipoDocAut = tipoDocAut;
	}

	public String getNumeroDocAut() {
		return numeroDocAut;
	}

	public void setNumeroDocAut(String numeroDocAut) {
		this.numeroDocAut = numeroDocAut;
	}

	public String getDataDocAut() {
		return dataDocAut;
	}

	public String getValorDividaStr() {
		return valorDividaStr;
	}

	public void setValorDividaStr(String valorDividaStr) {
		this.valorDividaStr = valorDividaStr;
	}

	public Double getValorDivida() {
		return valorDivida;
	}

	public void setDataDocAut(String dataDocAut) {
		this.dataDocAut = dataDocAut;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDataMotivo() {
		return dataMotivo;
	}

	public void setDataMotivo(String dataMotivo) {
		this.dataMotivo = dataMotivo;
	}

	public String getTipoDocEnvio() {
		return tipoDocEnvio;
	}

	public void setTipoDocEnvio(String tipoDocEnvio) {
		this.tipoDocEnvio = tipoDocEnvio;
	}

	public String getDataDocEnvio() {
		return dataDocEnvio;
	}

	public void setDataDocEnvio(String dataDocEnvio) {
		this.dataDocEnvio = dataDocEnvio;
	}

	public String getNumDocEnvio() {
		return numDocEnvio;
	}

	public void setNumDocEnvio(String numDocEnvio) {
		this.numDocEnvio = numDocEnvio;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}

	// Implementação do Method validate, para realizar as validações (Na
	// implementação desse método deve-se codificar a inserção em um objeto
	// ActionErros todos os erros detectados durante a validação)

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		// retrieve the "library" attribute from the session-scope
		HttpSession session = request.getSession();

		ServletContext context = getServlet().getServletContext();
//		context.log("Método validate chamado!");

		// System.out.println("entrou no validate");

		this.processoInicioStr = (String) session.getAttribute("DataIni");
		this.processoTerminoStr = (String) session.getAttribute("DataFim");

		Double valorMaximoDivida = (Double) session.getAttribute("valorDividaSispag");

		/******/

		// Validação do Banco
		if ((banco == null) || (banco.equals(""))) {
			errors.add("banco", new ActionMessage("error.campoBanco.required"));
		}

		if ((agencia == null) || (agencia.equals(""))) {
			errors.add("agencia", new ActionMessage("error.campoAgencia.required"));
		}

		if ((contaCorrente == null) || (contaCorrente.equals(""))) {
			errors.add("contaCorrente", new ActionMessage("error.campoContaCorrente.required"));
		}

		/*****/

		// Validação do Valor da Dívida
		if ((valorDividaStr == null) || (valorDividaStr.equals(""))) {
			errors.add("valorDividaStr", new ActionMessage("error.campoValorDivida.required"));
		} else {

			String Nvalor = valorDividaStr.replaceAll(",", ".");

			this.valorDivida = Double.parseDouble(Nvalor);

			if (valorDivida <= 0.0) {
				errors.add("valorDivida", new ActionMessage("error.campoValorDivida.range"));

				// context.log("Valor da dívida negativo");
			} else if (valorDivida > valorMaximoDivida) {
				errors.add("valorDivida", new ActionMessage("error.campoValorDivida.range"));

			}
		}

		if (processoInicioStr == null) {
			errors.add("processoIni", new ActionMessage("error.processoIni.required"));
		} else {

			int pos = processoInicioStr.indexOf("/");
			mesini = processoInicioStr.substring(0, pos);
			anoini = processoInicioStr.substring(pos + 1);

		}

		if (processoTerminoStr == null) {
			errors.add("processoTerm", new ActionMessage("error.processoTerm.required"));
		} else {

			int poss = processoTerminoStr.indexOf("/");
			mesterm = processoTerminoStr.substring(0, poss);
			anoterm = processoTerminoStr.substring(poss + 1);
		}

		// Form verification
		if (origemDocAut.length() == 0) {
			errors.add("origemDocAut", new ActionMessage("error.campoOrigemDocAut.required"));
		}

		if (tipoDocAut.length() == 0) {
			errors.add("tipoDocAut", new ActionMessage("error.campoTipoDocAut.required"));
		}

		if (numeroDocAut.length() == 0) {
			errors.add("numeroDocAut", new ActionMessage("error.campoNumeroDocAut.required"));
		}

		if (dataDocAut.length() == 0) {
			errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.required"));

//			context.log("Data Doc Aut não informada");	
		} else {
			if (!validaData(dataDocAut)) {
				errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.range"));

//				context.log("Data Doc Aut inválida");	
			} else {
				String[] arrayData = dataDocAut.split("/");
				String dia = arrayData[0];
				String mes = arrayData[1];
				String ano = arrayData[2];

				dataDocAut = ano + "-" + mes + "-" + dia;
			}
		}

		if (dataDocEnvio.length() != 0) {

			if (!validaData(dataDocEnvio)) {
				errors.add("dataDocEnvio", new ActionMessage("error.campoDataDocEnvio.range"));

//				context.log("Data Doc Aut inválida");	
			} else {
				String[] arrayData = dataDocEnvio.split("/");
				String dia = arrayData[0];
				String mes = arrayData[1];
				String ano = arrayData[2];
				if (Integer.parseInt(ano) < 1994) {
					errors.add("dataDocEnvio", new ActionMessage("error.campoDataDocEnvio.range"));

//					context.log("Data Doc Aut ano inferior a 1994");	

				}
				dataDocEnvio = ano + "-" + mes + "-" + dia;
			}
		}

		if (motivo.length() == 0) {
			errors.add("motivo", new ActionMessage("error.campoMotivo.required"));
		}

		if (dataMotivo.length() == 0) {
			errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.required"));
		} else {
			if (!validaData(dataMotivo)) {
				errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.range"));
			} else {

				String[] arrayData = dataMotivo.split("/");
				String dia = arrayData[0];
				String mes = arrayData[1];
				String ano = arrayData[2];

				dataMotivo = ano + "-" + mes + "-" + dia;
			}
		}

		return errors;
	}

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

			if (ano < 1900) {
				result = false;
			}

		} catch (NumberFormatException nfe) {
			result = false;
		}

		return result;
	}

}
