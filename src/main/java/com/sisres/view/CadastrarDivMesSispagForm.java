package com.sisres.view;

//Servlet imports
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class CadastrarDivMesSispagForm extends ActionForm {

	private String processoDataStr = null;
	private String mes = null;
	private String ano = null;

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
	private String dataDocAut = null; //aqui

//	private int numeroDocAut = 0;
	private String numeroDocAut = null;
//	private String numeroDocAutStr = null;

	private String motivo = null;
	private String dataMotivo = null;
	private String observacao = null;

	public void setProcessoDataStr(String processoDataStr) {
		this.processoDataStr = processoDataStr;
	}

	public String getProcessoDataStr() {
		return processoDataStr;
	}

	public String getMes() {
		return mes;
	}

	public String getAno() {
		return ano;
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

	/*
	 * public String getNumeroDocAutStr(){ return numeroDocAutStr; }
	 * 
	 * public void setNumeroDocAutStr(String numeroDocAutStr){ this.numeroDocAutStr
	 * = numeroDocAutStr; }
	 */
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

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}

	// Realizar as conversões de dados necessárias. Neste caso, não é preciso.

	// Implementação do Method validate, para realizar as validações (Na
	// implementação desse método deve-se codificar a inserção em um objeto
	// ActionErros todos os erros detectados durante a validação)
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();
//		context.log("Método validate(CadastrarDivMesSispag) chamado!");

		// retrieve the "library" attribute from the session-scope
		HttpSession session = request.getSession();

		Double valorMaximoDivida = (Double) session.getAttribute("valorDividaSispag");

		if (processoDataStr.length() != 0) {

			int pos = processoDataStr.indexOf("/");
			mes = processoDataStr.substring(0, pos);
			ano = processoDataStr.substring(pos + 1);
		}
		// Validação do Valor da Dívida

		if ((valorDividaStr == null) || (valorDividaStr.equals(""))) {
			errors.add("valorDividaStr", new ActionMessage("error.campoValorDivida.required"));
		} else {

			// System.out.println(valorDividaStr);

			String Nvalor = valorDividaStr.replaceAll(",", ".");

			// System.out.println(Nvalor);

			this.valorDivida = Double.parseDouble(Nvalor);

			// System.out.println("Nvalor: " +Nvalor);
			// System.out.println("valor: " +valorDivida);

			if (valorDivida <= 0.0) {
				errors.add("valorDivida", new ActionMessage("error.campoValorDivida.range"));

//				context.log("Valor da dívida negativo");			
			} else if (valorDivida > valorMaximoDivida) {
				errors.add("valorDivida", new ActionMessage("error.campoValorDivida.range"));
			}
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
		/*
		 * } else { try { numeroDocAut = Integer.parseInt(numeroDocAut.trim()); } catch
		 * (NumberFormatException e) { errors.add("numeroDocAut", new
		 * ActionMessage("error.campoNumeroDocAut.range")); }
		 * 
		 * /*Pattern pattern = Pattern.compile("[0-9]?"); Matcher matcher =
		 * pattern.matcher(numeroDocAut);
		 * 
		 * if (! matcher.find()) { errors.add("numeroDocAut", new
		 * ActionMessage("error.campoNumeroDocAut.range"));
		 * 
		 * }
		 */
//		}  

		/*
		 * else if (! numeroDocAut.matches("\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d")) {
		 * errors.add("numeroDocAut", new
		 * ActionMessage("error.campoNumeroDocAut.range")); }
		 */

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

		// System.out.println(this.dataDocAut);

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

		/*
		 * CadDivMesSispagSvc sispagSvc = new CadDivMesSispagSvc(); if
		 * (sispagSvc.validaDivida((String)request.getSession().getAttribute("matFin"),
		 * ocCodigo, mes, ano)) { errors.add("dividaDuplicada", new
		 * ActionMessage("error.dividaDuplicada")); }
		 */

		//// System.out.println("passou validade");

		return errors;
	}

	public boolean validaData(String data) {
		boolean result = true;

		String[] arrayData = data.split("/");
		try {
			int dia = Integer.parseInt(arrayData[0]);
			int mes = Integer.parseInt(arrayData[1]);
			int ano = Integer.parseInt(arrayData[2]);

			// int ANOATUAL = ano;

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

			if (ano < 1900) {
				// System.out.println("passei por aqui validação do ano.");
				result = false;
			}
		} catch (NumberFormatException nfe) {
			result = false;
			// listaErros.add("msgData",new ActionMessage("Data Inválida"));// procurar no
			// properties
		}

		return result;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.processoDataStr = null;
		this.mes = null;
		this.ano = null;

		this.valorDivida = 0.0;
		this.valorDividaStr = null;

		this.nome = null;
		this.cpf = null;
		this.posto = null;
		this.situacao = null;
		this.ocCodigo = null;
		this.ocNome = null;

		this.banco = null;
		this.agencia = null;
		this.contaCorrente = null;

		this.origemDocAut = null;
		this.tipoDocAut = null;
		this.dataDocAut = null;

//			this.numeroDocAut = 0;
		this.numeroDocAut = null;

		this.motivo = null;
		this.dataMotivo = null;
		this.observacao = null;
	}

}
