/**
 * 
 */
package com.sisres.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.Divida;
import com.sisres.model.IncluirDevolucaoSvc;
import com.sisres.model.Lancamento;
import com.sisres.model.SispagException;
import com.sisres.view.IncluirDevolucaoForm;

/**
 * @author usuario
 *
 */
public class IncluirDevolucaoAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		// Pega o lancamento da session
		// System.out.println("Entrou no action");
		HttpSession session = request.getSession();
		// System.out.println("Instanciou a session");
		Divida divida = (Divida) session.getAttribute("divida");
		Lancamento lancamento = new Lancamento();
		lancamento.setDividaId(divida.getId());

		// System.out.println("Divida id: " + divida.getId() );
		IncluirDevolucaoForm myForm = (IncluirDevolucaoForm) form;

		// pega os dados validados no Form
		lancamento.setTipoDocAutorizacao(myForm.getTipoDocAutorizacao());
		lancamento.setDataDocAutorizacao(new Date(myForm.getDataDocAutorizacao()));
		lancamento.setNumeroOB(myForm.getNumeroOB());
		lancamento.setDataOB(new Date(myForm.getDataOB()));
		lancamento.setValor(Double.valueOf(myForm.getValor()));
		lancamento.setObservacao(myForm.getObservacao());
		lancamento.setResponsavel((String) request.getSession().getAttribute("usuarioSispag"));
		lancamento.setOrigemDocAutorizacao(myForm.getOrigemDocAutorizacao());
		// Informação padrão para identificar que o lancamento é uma devolução
		lancamento.setTipo("3");
		lancamento.setOperador("D");

		IncluirDevolucaoSvc inclDevolucaoSvc = new IncluirDevolucaoSvc();
		try {
			Double saldo = inclDevolucaoSvc.getSaldoRegularizar(lancamento.getDividaId());
			// System.out.println("Saldo: "+ saldo);
			// Verifica se a Divida tem saldo a regularizar
			if (saldo >= 0) {
				messages.add("saldoRegularizar", new ActionMessage("message.saldoRegularizar"));
				// System.out.println("A Dívida possui Saldo a Regularizar e não pode haver
				// devolução");
				saveErrors(request, messages);
				return mapping.findForward("erro");
			}
			// Verifica se o valor da devolução é válido para a Dívida
			// System.out.println("Verifica se o valor da devolução é válido para a
			// Dívida");
			if ((saldo * -1) < lancamento.getValor()) {
				errors.add("saldoInvalido", new ActionMessage("erro.saldoInvalido"));
				// System.out.println("Valor do Lançamento de Devolução maior do que o Saldo a
				// Regularizar da Dívida");
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}
			// Inseri lancamento de Devolução
			// System.out.println("Inseri lancamento de Devolução");
			if (inclDevolucaoSvc.setLancamentoDevolucao(lancamento)) {
				divida.setValor(inclDevolucaoSvc.getSaldoRegularizar(lancamento.getDividaId()));
				session.setAttribute("divida", divida);
				messages.add("sucesso", new ActionMessage("message.genericMessage"));
				saveMessages(request, messages);
				return mapping.findForward("sucesso");
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.baseDados.inacessivel"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}
		} catch (SispagException se) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.baseDados.inacessivel"));
			saveErrors(request, errors);
			return mapping.findForward("erro");
		}

	}

}
