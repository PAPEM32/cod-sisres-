package com.sisres.view;

//Front Controller
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.model.Divida;
import com.sisres.model.Pedido;
import com.sisres.model.Pessoa;

public class ConsultarReversaoForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	/*
	 * private String codReversao = ""; private String tipoDivida = ""; private
	 * String pagina = ""; private String codBanco = ""; private String ano; private
	 * String dataReversao; private String registrosPorPagina = ""; private String
	 * opvalor = ""; private String pvalor= "";
	 */

	private String codReversao;
	private String tipoDivida;
	private String codBanco;
	private String ano;
	private String dataReversao;
	private String opvalor;
	private String pvalor;

// CAMPO INFORMACOES SOBRE A DIVIDA

	public void setCodReversao(String codReversao) {
		this.codReversao = codReversao;
	}

	public String getCodReversao() {
		return codReversao;
	}

	public void setTipoDivida(String tipoDivida) {
		this.tipoDivida = tipoDivida;
	}

	public String getTipoDivida() {
		return tipoDivida;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getAno() {
		return ano;
	}

	public void setdataReversao(String dataReversao) {
		this.dataReversao = dataReversao;
	}

	public String getdataReversao() {
		return dataReversao;
	}

	/*
	 * public String getPagina() { return pagina; }
	 * 
	 * public void setPagina(String pagina) { this.pagina = pagina; }
	 */

	/*
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

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		int codRev = 0;

		ActionErrors errors = new ActionErrors();
		if (!codReversao.equals("")) {

			try {
				codRev = Integer.parseInt(codReversao);
			} catch (NumberFormatException nfe) {
				errors.add("campoCodDivida", new ActionMessage("error.campoCodDivida.range"));// procurar no properties
			}
		}

		if (!dataReversao.equals("")) {
			try {
				String[] arrayData = dataReversao.split("/");
				int dia = 1, mes = 1, ano = 1900;

				dia = Integer.parseInt(arrayData[0]);
				mes = Integer.parseInt(arrayData[1]);
				ano = Integer.parseInt(arrayData[2]);

				Calendar calendar = new GregorianCalendar();
				java.util.Date trialTime = new java.util.Date();
				calendar.setTime(trialTime);
				String ANO = "" + calendar.get(Calendar.YEAR);
				int ANOATUAL = Integer.parseInt(ANO);

				if ((ano < 1900) || (ano > ANOATUAL)) {
					// System.out.println("passei por aqui validação do ano.");
					errors.add("campoAno", new ActionMessage("error.campoAno.range"));
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
						errors.add("campoDia", new ActionMessage("error.campoDia.range"));
					}
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					if ((dia <= 0) || (dia > 30)) {
						errors.add("campoDia", new ActionMessage("error.campoDia.range"));
					}
					break;
				case 2:
					if (bisex == 0) {
						if ((dia <= 0) || (dia > 29)) {
							errors.add("campoDia", new ActionMessage("error.campoDia.range"));
						}
					} else {
						if ((dia <= 0) || (dia > 28)) {
							errors.add("campoDia", new ActionMessage("error.campoDia.range"));
						}
					}

					break;
				default:
					errors.add("campoMes", new ActionMessage("error.campoMes.range"));
				}// fim do switch

			} catch (NumberFormatException nfe) {
				errors.add("campoData", new ActionMessage("error.campoData.range"));
			} catch (Exception e) {

				errors.add("dataReversao", new ActionMessage("error.campoDataReversao.range"));
			}
		}

		if (!ano.equals("")) {
			Calendar calendar = new GregorianCalendar();
			java.util.Date trialTime = new java.util.Date();
			calendar.setTime(trialTime);
			String ANO = "" + calendar.get(Calendar.YEAR);
			int ANOATUAL = Integer.parseInt(ANO);
			int anoForm = Integer.parseInt(ano);

			if ((anoForm < 1900) || (anoForm > ANOATUAL)) {
				// System.out.println("passei por aqui validação do ano.");
				errors.add("campoAno", new ActionMessage("error.campoAno.range"));
			}

		}
		// Inclusão dos parametros de pesquisa em memória.
		Divida dividaFiltroReversao = new Divida();
		Pedido pedidoFiltroReversao = new Pedido();

		if (!this.ano.isEmpty())
			pedidoFiltroReversao.setAno(Integer.parseInt(this.ano));

		if (!this.codReversao.isEmpty())
			pedidoFiltroReversao.setCodigo(this.codReversao);

		Pessoa pessoaFiltroReversao = new Pessoa();

		if (!this.codBanco.isEmpty())
			pedidoFiltroReversao.setBanco(Integer.parseInt(codBanco));
		if (!this.tipoDivida.isEmpty())
			dividaFiltroReversao.setTipo(this.tipoDivida);

		dividaFiltroReversao.setPedido(pedidoFiltroReversao);
		dividaFiltroReversao.setPessoa(pessoaFiltroReversao);

		request.getSession().setAttribute("reversaoFiltro", dividaFiltroReversao);

		return errors;
	}

}// fim da classe
