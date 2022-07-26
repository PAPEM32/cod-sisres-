package com.sisres.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.IncluirResparegSvc;
import com.sisres.model.SispagException;

public class IncluirResparegAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		IncluirResparegSvc incluirResparegSvc = new IncluirResparegSvc();

		// Verifica estágio CGS
		try {
			if (incluirResparegSvc.verificaEstagioCGS() != 3) {
				errors.add("erroCGS", new ActionMessage("error.estagioCGS"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}

			// Verifica se há dívida do tipo RESPAREG/REGARESP para este mês
			if (!incluirResparegSvc.verificaExisteResparegSISPAG()) {
				errors.add("errorresparegInexistente", new ActionMessage("error.resparegInexistente"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}

			// Verifica se já foi realizada a operação neste mês
			if (incluirResparegSvc.verificaExisteResparegSISRES()) {
				errors.add("errorExisteRespareg", new ActionMessage("error.existeRespareg"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}

			// Inclui RESPAREG e REGARESP
			if (!incluirResparegSvc.insereRespareg()) {
				errors.add("erroBD", new ActionMessage("error.baseDados.inacessivel"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			}
			// System.out.println("Operação respareg realizada");

			messages.add("mensagemSucesso", new ActionMessage("message.resparegIncluido"));
			saveMessages(request, messages);
			return mapping.findForward("erro");
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			/* errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ")); */
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}

	}

}
