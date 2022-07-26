package com.sisres.controller;

import javax.servlet.ServletContext;
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

import com.sisres.model.CadDivHistoricaSispagSvc;
import com.sisres.model.Divida;
import com.sisres.model.SispagException;
import com.sisres.view.FiltrarDividaHistForm;

public class filtrarDividaHistAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// System.out.println("Entrei aqui no action!!!");

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();

		try {

			// context.log("filtrarMatfin action");

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();
			// HttpServletRequest req = request.getRequest

			// Cast the action form to an application-specific form
			FiltrarDividaHistForm myForm = (FiltrarDividaHistForm) form;

			// Business logic
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			CadDivHistoricaSispagSvc sispagSvc = new CadDivHistoricaSispagSvc(usuario, senhaCripto);

			Divida dividaItem = null;
			dividaItem = sispagSvc.getDadosDivHistoricaSISPAG(myForm.getMatFin(), myForm.getDataIni(),
					myForm.getDataFim());

			// Store the divida on the request-scope
			// request.setAttribute("dividaItem", dividaItem);
			session.setAttribute("dividaItem", dividaItem);

			String dataIni = myForm.getDataIni().substring(4, 6) + "/" + myForm.getDataIni().substring(0, 4);

			String dataFim = myForm.getDataFim().substring(4, 6) + "/" + myForm.getDataFim().substring(0, 4);

			// Store the matFin on the session-scope
			session.setAttribute("matFin", myForm.getMatFin());
			session.setAttribute("DataIni", dataIni);
			session.setAttribute("DataFim", dataFim);

			// Store the valorDivida on the session-scope

			session.setAttribute("valorDividaSispag", dividaItem.getValor());

			// System.out.println("retornou o success...");
			// Dispatch to Success view
			return mapping.findForward("success");

		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		} catch (SispagException se) {
			// Log stack trace
			context.log("Erro Inesperado: ", se);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		}
	}

}
