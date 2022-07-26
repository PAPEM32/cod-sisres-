package com.sisres.view;

//Servlet imports
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
//Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ConfirmarDividaHistoricaForm extends ActionForm {

	private String matFin = null;
	private String codDivida = null;

	private String mesStrIni = null;
	private String anoStrIni = null;
	private String processoDataInicio = null;

	private String mesStrTer = null;
	private String anoStrTer = null;
	private String processoDataTermino = null;

	private double valorDivida = 0.0;
	private String valorDividaStr = null;

	private String nome = null;
	private String cpf = null;
	private String posto = null;
	private String situacao = null;
	private String ocCodigo = null;
	private String ocNome = null;

	private String banco = null;
	private String agencia = null;
	private String contaCorrente = null;

	private String origemDocAut = null;
	private String tipoDocAut = null;
	private String numeroDocAut = null;
	private int numeroDocAutInt = 0;

	public int getNumeroDocAutInt() {
		return numeroDocAutInt;
	}

	public void setNumeroDocAutInt(int numeroDocAutInt) {
		this.numeroDocAutInt = numeroDocAutInt;
	}

	private String dataDocAut = null;

	private String tipoDocEnvio = null;
	private String numeroDocEnvio = null;
	private int numeroDocEnvioInt = 0;

	public int getNumeroDocEnvioInt() {
		return numeroDocEnvioInt;
	}

	public void setNumeroDocEnvioInt(int numeroDocEnvioInt) {
		this.numeroDocEnvioInt = numeroDocEnvioInt;
	}

	private String dataDocEnvio = null;

	private String dataMotivo = null;
	private String motivo = null;

	// private String observacao = null;

//Métodos gets e sets para os valores dos campos inseridos no JSP

	/*
	 * public String getMatFin(){ return matFin; }
	 * 
	 * public void setMatFin(String matFin){ this.matFin = matFin; }
	 */

	public void setCodDivida(String codDivida) {
		this.codDivida = codDivida;
	}

	public String getCodDivida() {
		return codDivida;
	}

	public void setMatFin(String matFin) {
		this.matFin = matFin;
	}

	public String getMatFin() {
		return matFin.trim();
	}

	public void setMesStrIni(String mesStrIni) {
		this.mesStrIni = mesStrIni;
	}

	public String getMesStrIni() {
		return mesStrIni;
	}

	public void setAnoStrIni(String anoStrIni) {
		this.anoStrIni = anoStrIni;
	}

	public String getAnoStrIni() {
		return anoStrIni;
	}

	public void setProcessoDataInicio(String processoDataInicio) {
		this.processoDataInicio = processoDataInicio;
	}

	public String getProcessoDataInicio() {
		return processoDataInicio;
	}

	public void setProcessoDataTermino(String processoDataTermino) {
		this.processoDataTermino = processoDataTermino;
	}

	public String getProcessoDataTermino() {
		return processoDataTermino;
	}

	public void setMesStrTer(String mesStrTer) {
		this.mesStrTer = mesStrTer;
	}

	public String getMesStrTer() {
		return mesStrTer;
	}

	public void setAnoStrTer(String anoStrTer) {
		this.anoStrTer = anoStrTer;
	}

	public String getAnoStrTer() {
		return anoStrTer;
	}

	public String getValorDividaStr() {
		return valorDividaStr;
	}

	public void setValorDividaStr(String valorDividaStr) {
		this.valorDividaStr = valorDividaStr;
	}

	public double getValorDivida() {
		return valorDivida;
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

	public void setDataDocAut(String dataDocAut) {
		this.dataDocAut = dataDocAut;
	}

	public String getTipoDocEnvio() {
		return tipoDocEnvio;
	}

	public void setTipoDocEnvio(String tipoDocEnvio) {
		this.tipoDocEnvio = tipoDocEnvio;
	}

	public String getNumeroDocEnvio() {
		return numeroDocEnvio;
	}

	public void setNumeroDocEnvio(String numeroDocEnvio) {
		this.numeroDocEnvio = numeroDocEnvio;
	}

	public String getDataDocEnvio() {
		return dataDocEnvio;
	}

	public void setDataDocEnvio(String dataDocEnvio) {
		this.dataDocEnvio = dataDocEnvio;
	}

	public String getDataMotivo() {
		return dataMotivo;
	}

	public void setDataMotivo(String dataMotivo) {
		this.dataMotivo = dataMotivo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/*
	 * public void setObservacao(String observacao) { this.observacao = observacao;
	 * }
	 * 
	 * public String getObservacao() { return observacao; }
	 */
//Realizar as conversões de dados necessárias. Neste caso, não é preciso.

//Implementação do Method validate, para realizar as validações (Na implementação desse método deve-se codificar a inserção em um objeto 
//ActionErros todos os erros detectados durante a validação) 

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();
//    context.log("Método validate chamado!");

		// Form verification
		/*
		 * if ( matFin.length() == 0 ) { errors.add("matFin", new
		 * ActionMessage("error.titleField.required")); } else { //validaMatFin ???
		 * 
		 * }
		 */

		// Validação de Mes - Não será necessário, pq será preenchido automaticamente
		/*
		 * try{ this.mes = Integer.parseInt(mesStr);
		 * 
		 * // context.log("Mês válido"); //System.out.println(this.mes);
		 * 
		 * } catch (NumberFormatException nfe){ errors.add("mesStr", new
		 * ActionMessage("erro.campoMes.required"));
		 * 
		 * // context.log("Mês inválido"); }
		 * 
		 * if ((mes != -1) && (mes < 1) || (mes > 31)){ errors.add("mesStr", new
		 * ActionMessage("erro.campoMes.range")); }
		 * 
		 * //Validação de Ano - Não será necessário, pq será preenchido automaticamente
		 * try{ this.ano = Integer.parseInt(anoStr); } catch (NumberFormatException
		 * nfe){ errors.add("anoStr", new ActionMessage("erro.campoAno.required")); }
		 */

		/*
		 * if (!ano.matches("\\d\\d\\d\\d")) { errors.add("anoStr", new
		 * ActionMessage("error.anoCampo.invalid")); }
		 */

		/*
		 * if ((ano != -1) && (ano < 1900) || (mes > 2100)){ errors.add("anoStr", new
		 * ActionMessage("erro.campoAno.range")); }
		 */

		// SÓ ESTAVA PASSANDO NULL E APRESENTAVA ERRO - SOLUÇÃO APAGAR OS ARQUIVOS DO
		// JBOOS E IMPLANTAR NOVAMENTE
		// EXECUTOU APLICAÇÃO PELO BROWSE E FUNCIONOU
		/*
		 * context.log("Separação do mês e ano (Inicio/Término)" );
		 * context.log(processoDataInicio); int posI = processoDataInicio.indexOf("/");
		 * mesStrIni = processoDataInicio.substring(0, posI); anoStrIni =
		 * processoDataInicio.substring(posI+1); context.log("Mes  mesStrIni = " +
		 * mesStrIni); context.log("Mes  anoStrIni = " + anoStrIni); int posT =
		 * processoDataTermino.indexOf("/"); mesStrTer =
		 * processoDataTermino.substring(0, posT); anoStrTer =
		 * processoDataTermino.substring(posT+1); context.log("Mes  mesStrTer = " +
		 * mesStrTer); context.log("Mes  anoStrTer = " + anoStrTer);
		 */

		// Validação do Valor da Dívida

		try {

			this.valorDivida = Double.parseDouble(valorDividaStr);
		} catch (NumberFormatException nfe) {
			errors.add("valorDividaStr", new ActionMessage("error.campoValorDivida.required"));
//			context.log("Valor da dívida não informado");	
		}

		// System.out.println("Valor Dívida = " + this.valorDivida);

		if (valorDivida <= 0.0) {
			errors.add("valorDivida", new ActionMessage("error.campoValorDivida.range"));
//			context.log("Valor da dívida negativo");			
		}

		// Form verification

		///// System.out.println(this.valorDivida);

		if (origemDocAut.length() == 0) {
			errors.add("origemDocAut", new ActionMessage("error.campoOrigemDocAut.required"));
//			context.log("Origem do Documento não informado");

		}

		// System.out.println(this.origemDocAut);

		if (tipoDocAut.length() == 0) {
			errors.add("tipoDocAut", new ActionMessage("error.campoTipoDocAut.required"));
//			context.log("Tipo do Documento não informado");
		}

		// System.out.println(this.tipoDocAut);

		this.numeroDocAutInt = Integer.parseInt(numeroDocAut);
		if (numeroDocAut.length() == 0) {
			errors.add("numeroDocAut", new ActionMessage("error.campoNumeroDocAut.required"));
//			context.log("Número do Documento não informado");
		}

		// System.out.println(this.numeroDocAut);

		if (dataDocAut.length() == 0) {
			errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.required"));
//			context.log("Data Doc Aut não informada");	
		} else {

			if (!validaData(dataDocAut)) {
				errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.range"));
//				context.log("Data Doc Aut inválida");	
			} else {
				String[] arrayData = dataDocAut.split("/");
				int dia = Integer.parseInt(arrayData[0]);
				int mes = Integer.parseInt(arrayData[1]);
				int ano = Integer.parseInt(arrayData[2]);

				dataDocAut = ano + "-" + mes + "-" + dia;
			}
		}

		// System.out.println(this.dataDocAut);

		if (tipoDocEnvio.length() == 0) {
			errors.add("tipoDocEnvio", new ActionMessage("error.campoTipoDocAut.required"));
//			context.log("Tipo do Documento SIPM não informado");
		}

		// System.out.println(this.tipoDocEnvio);

		if (numeroDocEnvio.length() == 0) {
			errors.add("numeroDocEnvio", new ActionMessage("error.campoNumeroDocAut.required"));
//			context.log("Número do Documento SIPM não informado");

		}
		this.numeroDocEnvioInt = Integer.parseInt(numeroDocEnvio);
		// System.out.println(this.numeroDocEnvio);

		if (dataDocEnvio.length() == 0) {
			errors.add("dataDocEnvio", new ActionMessage("error.campoDataDocAut.required"));
//			context.log("Data Doc SIPM não informada");	
		} else {

			if (!validaData(dataDocEnvio)) {
				errors.add("dataDocSIPM", new ActionMessage("error.campoDataDocAut.range"));
//				context.log("Data Doc SIPM  inválida");	
			} else {
				String[] arrayData = dataDocEnvio.split("/");
				int dia = Integer.parseInt(arrayData[0]);
				int mes = Integer.parseInt(arrayData[1]);
				int ano = Integer.parseInt(arrayData[2]);

				dataDocEnvio = ano + "-" + mes + "-" + dia;
			}
		}

		// System.out.println(this.dataDocAut);

		if (dataMotivo.length() == 0) {
			errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.required"));
//			context.log("Data Motivo não informada");	
		} else {
			if (!validaData(dataMotivo)) {
				errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.range"));
//				context.log("Data Motivo inválida");	
			} else {
				String[] arrayData = dataMotivo.split("/");
				int dia = Integer.parseInt(arrayData[0]);
				int mes = Integer.parseInt(arrayData[1]);
				int ano = Integer.parseInt(arrayData[2]);

				dataMotivo = ano + "-" + mes + "-" + dia;
			}
		}

		// System.out.println(this.dataMotivo);

		if (motivo.length() == 0) {
			errors.add("motivo", new ActionMessage("error.campoMotivo.required"));
//			context.log("Motivo não informado");	
		}

		// System.out.println(this.motivo);

//		context.log("Parou aki");

		return errors;
	}

	public boolean validaData(String data) {
		boolean result = true;

		String[] arrayData = data.split("/");
		try {
			int dia = Integer.parseInt(arrayData[0]);
			int mes = Integer.parseInt(arrayData[1]);
			int ano = Integer.parseInt(arrayData[2]);

			// if( (ano!=-1)&&(ano<1900)&&(ano)) Aconselhável fazer no form

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
			// listaErros.add("msgData",new ActionMessage("Data Inválida"));// procurar no
			// properties
		}

		return result;
	}
}
