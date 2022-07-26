package com.sisres.view;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.model.Divida;
import com.sisres.model.Lancamento;
import com.sisres.model.Pedido;
import com.sisres.model.Pessoa;

public class ConsultarDividaForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String codDivida = "";
	private String tipoDivida = "";
	private String sistemaOrigem = "";
	private String matfin = "";
	private String nome = "";
	private String inicioDivHist = "";
	private String terminoDivHist = "";
	private String statusDivida = "";
	private String tipoDocEnvio = "";
	private String numDocEnvio = "";
	private String dataDocEnvio = "";
	private String origemDocInclusao = "";
	private String tipoDocInclusao = "";
	private String numDocInclusao = "";
	private String dtDocInclusao = "";
	private String pedidoReversao = "";
	private String tipoSaldo = "";
	private String motivo = "";
//	private String registrosPorPagina = "";
	private String opvalor = "";
	private String pvalor = "";
	private String cpf = "";
	private String oc = "";
	private String estado = "";
	private String periodo = "";
	private String valorAtual = "";

	// private String reversao ="";

// CAMPO INFORMACOES SOBRE A DIVIDA

	public void setCodDivida(String codDivida) {
		this.codDivida = codDivida;
	}

	public String getCodDivida() {
		return codDivida;
	}

	public void setTipoDivida(String tipoDivida) {
		this.tipoDivida = tipoDivida;
	}

	public String getTipoDivida() {
		return tipoDivida;
	}

	public void setSistemaOrigem(String sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
	}

	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	public void setMatfin(String matfin) {
		this.matfin = matfin;
		// System.out.println("Form "+ getMatfin());
	}

	public String getMatfin() {
		return matfin;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	// campos de filtros exclusivos para Divida Historica

	// CAMPO FILTROS DA DIVIDA HISTORICA
	public void setInicioDivHist(String inicioDivHist) {
		this.inicioDivHist = inicioDivHist;
	}

	public String getInicioDivHist() {
		return inicioDivHist;
	}

	public void setTerminoDivHist(String terminoDivHist) {
		this.terminoDivHist = terminoDivHist;
	}

	public String getTerminoDivHist() {
		return terminoDivHist;
	}

	public void setStatusDivida(String statusDivida) {
		this.statusDivida = statusDivida;
	}

	public String getStatusDivida() {
		return statusDivida;
	}

	// CAMPO DOCUMENTO DE ENVIO A PAPEM
	public void setTipoDocEnvio(String tipoDocEnvio) {
		this.tipoDocEnvio = tipoDocEnvio;
	}

	public String getTipoDocEnvio() {
		return tipoDocEnvio;
	}

	public void setNumDocEnvio(String numDocEnvio) {
		this.numDocEnvio = numDocEnvio;
	}

	public String getNumDocEnvio() {
		return numDocEnvio;
	}

	public void setDataDocEnvio(String dataDocEnvio) {
		this.dataDocEnvio = dataDocEnvio;
	}

	public String getDataDocEnvio() {
		return dataDocEnvio;
	}

	// CAMPO DOCUMENTO AUTORIZANDO INCLUSAO DA DIVIDA
	public void setOrigemDocInclusao(String origemDocInclusao) {
		this.origemDocInclusao = origemDocInclusao;
	}

	public String getOrigemDocInclusao() {
		return origemDocInclusao;
	}

	public void setNumDocInclusao(String numDocInclusao) {
		this.numDocInclusao = numDocInclusao;
	}

	public String getNumDocInclusao() {
		return numDocInclusao;
	}

	public void setDtDocInclusao(String dtDocInclusao) {
		this.dtDocInclusao = dtDocInclusao;
	}

	public String getDtDocInclusao() {
		return dtDocInclusao;
	}

	public void setTipoDocInclusao(String tipoDocInclusao) {
		this.tipoDocInclusao = tipoDocInclusao;
	}

	public String getTipoDocInclusao() {
		return tipoDocInclusao;
	}

	// CAMPO INFORMACOES COMPLEMENTARES
	public void setPedidoReversao(String pedidoReversao) {
		this.pedidoReversao = pedidoReversao;
		// System.out.println("Form Ped"+ getPedidoReversao());
	}

	public String getPedidoReversao() {
		return pedidoReversao;
	}

	public void setTipoSaldo(String tipoSaldo) {
		this.tipoSaldo = tipoSaldo;
	}

	public String getTipoSaldo() {
		return tipoSaldo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
		// System.out.println("motivo"+ getMotivo());
	}

	public String getMotivo() {
		return motivo;
	}

	// Método de validação de matrícula financeira

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		int codDiv = 0;
		int origemDocIncl = 0;
		int pedReversao = 0;

		ActionErrors listaErros = new ActionErrors();
		// testes de parametros que ainda não foram feitos.

		if (!codDivida.equals("")) {

			// System.out.println("Codigo da Divida " + codDivida);

			try {
				codDiv = Integer.parseInt(codDivida);
			} catch (NumberFormatException nfe) {
				listaErros.add("campoCodDivida", new ActionMessage("error.campoCodDivida.range"));// procurar no
																									// properties
			}
		}

		if (!pedidoReversao.equals("")) {

			// System.out.println("Número do pedido de reversão: " + pedidoReversao);
			/*
			 * Author: Ten Luis Fernando Trecho de código abaixo comentando para permitir
			 * que o sistema consulte os pedidos de reversão preenchidos pelo usuário com
			 * caracteres
			 */
//                try{
//                	pedReversao = Integer.parseInt(pedidoReversao);
//                }catch (NumberFormatException nfe) {
//                	listaErros.add("campoPedReversao",new ActionMessage("error.campoPedReversao.range"));// procurar no properties
//                }
		}

		// teste matfin - validar com ten. Renato******************************

		if (!matfin.equals("")) {

			try {
				codDiv = Integer.parseInt(matfin);
				boolean resultado = true;
				// Verificar a forma de validar matricula do SIAPE
				// resultado = validaMatFin(matfin);
				if (!resultado) {
					listaErros.add("campoMatfin", new ActionMessage("error.campoMatFin.required"));
				}
				;
			} catch (NumberFormatException nfe) {
				listaErros.add("campoMatfin", new ActionMessage("error.campoMatFin.required"));// procurar no properties
			}

		}

		// *******************************************************************

		/*
		 * testes de divida historica inicioDivHist = null; terminoDivHist = null;
		 * statusDivida = null; tipoDocEnvio = null; numDocEnvio = null; dataDocEnvio =
		 * null;
		 */

		// origemDocInclusao - verificar se é código de 3 dígitos*********************.
		if (!origemDocInclusao.equals("")) {
			try {
				origemDocIncl = Integer.parseInt(origemDocInclusao);
			} catch (NumberFormatException nfe) {
				listaErros.add("campoOrigemDocIncl", new ActionMessage("error.campoOrigemDocIncl.range"));// procurar no
																											// properties
			}

			if (origemDocInclusao.length() != 3) {
				listaErros.add("campoOrigemDocIncl", new ActionMessage("error.campoOrigemDocIncl.range"));// procurar no
																											// properties
			}

		}

		// dtDocInclusao - teste de data ************************************
		if (!dtDocInclusao.equals("")) {

			String[] arrayData = dtDocInclusao.split("/");
			int dia = 1, mes = 1, ano = 1900;
			try {
				dia = Integer.parseInt(arrayData[0]);
				mes = Integer.parseInt(arrayData[1]);
				ano = Integer.parseInt(arrayData[2]);

			} catch (NumberFormatException nfe) {
				listaErros.add("campoData", new ActionMessage("error.campoData.range"));// procurar no properties
			}

			Calendar calendar = new GregorianCalendar();
			java.util.Date trialTime = new java.util.Date();
			calendar.setTime(trialTime);
			String ANO = "" + calendar.get(Calendar.YEAR);
			int ANOATUAL = Integer.parseInt(ANO);

			if ((ano < 1900) || (ano > ANOATUAL)) {
				// System.out.println("passei por aqui validação do ano.");
				listaErros.add("campoAno", new ActionMessage("error.campoAno.range"));
			}

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
					listaErros.add("campoDia", new ActionMessage("error.campoDia.range"));
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if ((dia <= 0) || (dia > 30)) {
					listaErros.add("campoDia", new ActionMessage("error.campoDia.range"));
				}
				break;
			case 2:
				if (bisex == 0) {
					if ((dia <= 0) || (dia > 29)) {
						listaErros.add("campoDia", new ActionMessage("error.campoDia.range"));
					}
				} else {
					if ((dia <= 0) || (dia > 28)) {
						listaErros.add("campoDia", new ActionMessage("error.campoDia.range"));
					}
				}

				break;
			default:
				listaErros.add("campoMes", new ActionMessage("error.campoMes.range"));
			}// fim do switch
			dtDocInclusao = (ano + "-" + mes + "-" + dia);
		} // fim do if de teste da data

		getServlet().getServletContext().log(listaErros.toString());

		if (listaErros.size() > 0) {
			request.getSession().removeAttribute("listDivida");

		}
		// Verifica se somente um dos parâmetros do valor foi preenchido

		if ((!opvalor.trim().equals("") & pvalor.trim().equals(""))
				| (opvalor.trim().equals("") & !pvalor.trim().equals("")) | (opvalor == null & pvalor != null)
				| (opvalor != null & pvalor == null)) {
			listaErros.add("campoValor", new ActionMessage("error.campoValor.range"));
		}

		/**
		 * Colocando os filtros pertinentes à dívida em memória (sessão)
		 */
		Divida dividaFiltroDivida = new Divida();
		Pedido pedidoFiltroDivida = new Pedido();
		Pessoa pessoaFiltroDivida = new Pessoa();
		Lancamento lancamentoInicialFiltroDivida = new Lancamento();

		if (!this.codDivida.isEmpty()) {
			dividaFiltroDivida.setCodigo(Integer.parseInt(this.codDivida));
		}

		// combo de TIPO DIVIDA
		/*
		 * if(!this.tipoDivida.isEmpty()) { dividaFiltroDivida.setTipo(tipoDivida); }
		 */

		// combo de ESTADO
		/*
		 * if(!this.statusDivida.isEmpty()) {
		 * dividaFiltroDivida.setEstado(statusDivida); }
		 */

		// Radio Button referente ao sistema de Origem (SIAPE ou SISPAG)
		if (!this.sistemaOrigem.isEmpty()) {
			dividaFiltroDivida.setCgs(this.sistemaOrigem);
		}

		if (!this.matfin.isEmpty()) {
			pessoaFiltroDivida.setMatFin(this.matfin);
		}

		if (!this.pedidoReversao.isEmpty()) {
			pedidoFiltroDivida.setCodigo(this.pedidoReversao);
		}

		if (!this.nome.isEmpty()) {
			pessoaFiltroDivida.setNome(this.nome);
		}

		if (!this.opvalor.isEmpty()) {
			request.getSession().setAttribute("opvalor", this.opvalor);
		} else {
			request.getSession().removeAttribute("opvalor");
		}

		if (!this.pvalor.isEmpty()) {
			request.getSession().setAttribute("pvalor", this.pvalor);
		} else {
			request.getSession().removeAttribute("pvalor");
		}

		/**
		 * Colocando os filtros pertinentes ao documento autorizando a inclusão da
		 * divida
		 */

		// combo OC
		/*
		 * if(!this.origemDocInclusao.isEmpty()) {
		 * 
		 * }
		 */

		// combo Tipo Documento de autorizacao
		if (!this.tipoDocInclusao.isEmpty()) {
			lancamentoInicialFiltroDivida.setTipoDocAutorizacao(this.tipoDocInclusao);
		}

		if (!this.numDocInclusao.isEmpty()) {
			lancamentoInicialFiltroDivida.setNumeroDocAutorizacao(this.numDocInclusao);
		}

		dividaFiltroDivida.setPessoa(pessoaFiltroDivida);
		dividaFiltroDivida.setPedido(pedidoFiltroDivida);
		dividaFiltroDivida.setLancamento(lancamentoInicialFiltroDivida);

		request.getSession().setAttribute("dividaFiltro" + "", dividaFiltroDivida);

		return listaErros;
	}// fim do método validate

	public boolean validaMatFin(String matricula) {
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
		String nip = matricula.substring(0, 8);

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
		return result;
	}

	/*
	 * public String getPagina() { return pagina; }
	 * 
	 * public void setPagina(String pagina) { this.pagina = pagina; }
	 * 
	 * public String getRegistrosPorPagina() { return registrosPorPagina; }
	 * 
	 * public void setRegistrosPorPagina(String registrosPorPagina) {
	 * this.registrosPorPagina = registrosPorPagina; }
	 */

	public void setOpvalor(String opvalor) {
		this.opvalor = opvalor;
	}

	public String getOpvalor() {
		return opvalor;
	}

	public void setPvalor(String pvalor) {
		this.pvalor = pvalor;
	}

	public String getPvalor() {
		return pvalor;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getOc() {
		return oc;
	}

	public void setOc(String oc) {
		this.oc = oc;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(String valorAtual) {
		this.valorAtual = valorAtual;
	}

	/*
	 * public void setReversao(String reversao) { this.reversao = reversao; }
	 * 
	 * public String getReversao() { return reversao; }
	 */

}// fim da classe
