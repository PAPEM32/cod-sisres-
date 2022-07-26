package com.sisres.controller;

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
import com.sisres.model.ExcDivMesSispagSvc;
import com.sisres.model.SispagException;

public class ExcluirDividaAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors listaErros = new ActionErrors();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		Divida divida = (Divida) session.getAttribute("divida");

		ExcDivMesSispagSvc excDivSvc = new ExcDivMesSispagSvc();

		try {
			if (excDivSvc.excluirDivida(divida)) {
				messages.add("mensagemSucesso", new ActionMessage("message.exclusaoRealizada"));
				saveMessages(request, messages);
				// System.out.println("Dívida apagada com sucesso");
				return mapping.findForward("sucesso");
			} else {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError"));
				// System.out.println("Não foi possível realizar a operação");
				return mapping.findForward("erro");
			}
		} catch (SispagException e) {
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("erro");
		}
	}
}
