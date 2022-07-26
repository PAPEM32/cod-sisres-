package com.sisres.controller;

//import java.util.LinkedList;
//import java.util.List;

//import model.;

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

import com.sisres.model.CadDivMesSispagSvc;
import com.sisres.model.Divida;
import com.sisres.model.SispagException;
//Classes do Projeto
import com.sisres.view.FiltrarMatFinSispagForm;

public class FiltrarMatFinSispagAction extends Action {

	/* forward name="success" path="" */
	// private final static String SUCCESS = "success";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();

		// Store this set in the request scope, in case we need to
		// send the ErrorPage view.
		// saveMessages(request, errors);

		try {

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			FiltrarMatFinSispagForm myForm = (FiltrarMatFinSispagForm) form;

			// Business logic
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			CadDivMesSispagSvc sispagSvc = new CadDivMesSispagSvc(usuario, senhaCripto);

			Divida dividaItem = null;

			dividaItem = sispagSvc.getDivMesSISPAG(myForm.getMatFin());
			// flag utilizado no alterarDivida para modificar o conteúdo dos dados contidos
			// na tela
			session.setAttribute("filtro", "true");
			// Store the divida on the request-scope
			// request.setAttribute("dividaItem", dividaItem);
			session.setAttribute("dividaItem", dividaItem);

			// Store the matFin on the session-scope
			session.setAttribute("matFin", myForm.getMatFin());

			// Store the valorDivida on the session-scope
			session.setAttribute("valorDividaSispag", dividaItem.getValor());

			// Dispatch to Success view
			return mapping.findForward("success");

			// Handle any unexpected expections
		} catch (SispagException se) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("error");

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
