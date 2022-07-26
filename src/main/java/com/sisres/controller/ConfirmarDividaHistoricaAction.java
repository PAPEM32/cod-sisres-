package com.sisres.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//Struts classes
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.ConfirmarDividaHistoricaSvc;
import com.sisres.model.DAODivida;
import com.sisres.model.Divida;
import com.sisres.view.ConfirmarDividaHistoricaForm;

public class ConfirmarDividaHistoricaAction extends Action {

	/* forward name="success" path="" */
	// private final static String SUCCESS = "success";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		// Store this set in the request scope, in case we need to
		// send the ErrorPage view.
		// saveMessages(request, errors);

		try {

			HttpSession session = request.getSession();

			ConfirmarDividaHistoricaForm myForm = (ConfirmarDividaHistoricaForm) form;
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			// Business logic
			ConfirmarDividaHistoricaSvc ConfDivSvc = new ConfirmarDividaHistoricaSvc(usuario, senhaCripto);
			String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

			Divida divida = (Divida) session.getAttribute("divida");

			ConfDivSvc.confirmarDividaHistorica(divida, myForm, codUsuario);

			request.setAttribute("divida", divida);

			messages.add("mensagemSucesso", new ActionMessage("message.dividaConfirmada"));
			saveMessages(request, messages);

			return mapping.findForward("success");
			// Handle any unexpected expections
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("error");
		}

	}
}