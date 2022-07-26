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
import com.sisres.model.IncluirPerdaoSvc;
import com.sisres.model.Lancamento;
import com.sisres.model.SispagException;
import com.sisres.view.IncluirPerdaoForm;

public class IncluirPerdaoAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//Pega os dados da Session para passar para o SVC
		HttpSession session = request.getSession();

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		Lancamento lancamento = new Lancamento();
		Divida divida = (Divida) session.getAttribute("divida");
		IncluirPerdaoForm myForm = (IncluirPerdaoForm) form;

// Pega os dados do Form para passar para o objeto LANCAMENTO
//System.out.println("Divida:" +divida.getId());
		lancamento.setDividaId(divida.getId());
//System.out.println("lancamento div id: "+lancamento.getDividaId());
//System.out.println("lancamento div id: "+myForm.getValor());
// Somente a papem inseri o lancamento de perdao de dívida, por isso foi incluso automaticamente origem como 004
		lancamento.setOrigemDocAutorizacao("004");
		lancamento.setTipoDocAutorizacao(myForm.getTipoDocAutorizacao());
		lancamento.setNumeroDocAutorizacao(myForm.getNumeroDoc());
		lancamento.setDataDocAutorizacao(new Date(myForm.getDataDocAutorizacao()));
		lancamento.setValor(Double.valueOf(myForm.getValor()));
		lancamento.setObservacao(myForm.getObservacao());
		lancamento.setResponsavel((String) request.getSession().getAttribute("usuarioSispag"));

//Informação padrão para identificar que o lancamento é uma devolução
		lancamento.setTipo("4");
		lancamento.setOperador("C");
// pega o usuário logado
		lancamento.setResponsavel((String) request.getSession().getAttribute("usuarioSispag"));

		IncluirPerdaoSvc inclPerdaoSvc = new IncluirPerdaoSvc();
		try {
			Double saldo = inclPerdaoSvc.getSaldoRegularizar(lancamento.getDividaId());
//System.out.println("Saldo: "+saldo);
//	Verifica se a Divida tem saldo a regularizar
			if (saldo < 0) {
				messages.add("saldoRegularizar", new ActionMessage("message.saldoRegularizarPerdao"));
				errors.add("saldoInvalido", new ActionMessage("erro.saldoInvalidoPerdao"));
				// System.out.println("A Dívida possui Saldo a Regularizar e não pode haver
				// devolução");
				saveErrors(request, messages);
				return mapping.findForward("erro");
			}
//	Verifica se o valor da devolução é válido para a Dívida
			if (saldo < lancamento.getValor()) {
				errors.add("saldoInvalido", new ActionMessage("erro.saldoInvalidoPerdao"));
				// System.out.println("Valor do Lançamento de Perdão maior do que o Saldo a
				// Regularizar da Dívida");
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}
// Inseri lancamento de Devolução

			if (inclPerdaoSvc.setLancamentoPerdao(lancamento)) {
				// System.out.println("Voltou do DAO e do svc");
				divida.setValor(inclPerdaoSvc.getSaldoRegularizar(lancamento.getDividaId()));
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
