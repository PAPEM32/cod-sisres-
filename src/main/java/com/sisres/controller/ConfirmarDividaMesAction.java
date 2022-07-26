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

import com.sisres.model.ConfirmarDividaMesSvc;
import com.sisres.model.DAODivida;
import com.sisres.model.Divida;
import com.sisres.view.ConfirmarDividaMesForm;

public class ConfirmarDividaMesAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			ConfirmarDividaMesForm myForm = (ConfirmarDividaMesForm) form;
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			// Business logic
			ConfirmarDividaMesSvc ConfDivMesSvc = new ConfirmarDividaMesSvc(usuario, senhaCripto);

			String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

			Divida divida = (Divida) session.getAttribute("divida");

			ConfDivMesSvc.confirmarDividaMes(divida, myForm, codUsuario);

			request.setAttribute("divida", divida);

			messages.add("mensagemSucesso", new ActionMessage("message.dividaConfirmada"));
			saveMessages(request, messages);

			return mapping.findForward("success");

		} catch (RuntimeException e) {
			context.log("Erro Inesperado: ", e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			return mapping.findForward("error");
		}

	}
}