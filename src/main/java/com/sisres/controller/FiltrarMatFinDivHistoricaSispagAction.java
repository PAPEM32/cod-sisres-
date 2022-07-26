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

import com.sisres.model.CadDivHistoricaSispagSvc;
import com.sisres.model.SispagException;
//Classes do Projeto
import com.sisres.view.FiltrarMatFinDivHistoricaSispagForm;

public class FiltrarMatFinDivHistoricaSispagAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();

		try {

			// context.log("filtrarMatfin action");

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			FiltrarMatFinDivHistoricaSispagForm myForm = (FiltrarMatFinDivHistoricaSispagForm) form;

			// Business logic
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			CadDivHistoricaSispagSvc sispagSvc = new CadDivHistoricaSispagSvc(usuario, senhaCripto);

			/**
			 * Minha divida virá em um array de 3 dimensões OC / Conta Conrrente / Dados da
			 * Divida
			 */
			String[][][] div_OC_CC = null;

			div_OC_CC = sispagSvc.getDivHistoricaSISPAG(myForm.getMatFin(), myForm.getProcessoInicioStr(),
					myForm.getProcessoTerminoStr());

			if (div_OC_CC == null) {
				errors.add("erroBD", new ActionMessage("error.matfinInexistente.range"));
				saveErrors(request, errors);
				return mapping.findForward("erro");

			}
			// System.out.println("Vou verificar se já existe");

			div_OC_CC = sispagSvc.verificaDivCadastrada(div_OC_CC, myForm.getMatFin(), myForm.getProcessoInicioStr(),
					myForm.getProcessoTerminoStr());

			session.setAttribute("div_OC_CC", div_OC_CC);

			String oc = (String) session.getAttribute("codOc");

			// System.out.println("Valida Login codigo da oc da sessao: " + oc);

			// System.out.println("OC do meu array: " + div_OC_CC[0][0][0]);

			session.setAttribute("matFin", myForm.getMatFin());
			// session.setAttribute("processoInicioStr", myForm.getProcessoInicioStr());
			// session.setAttribute("processoTerminoStr", myForm.getProcessoTerminoStr());

			return mapping.findForward("success");

			// Handle any unexpected expections
		} catch (SispagException se) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");

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
		}

	}
}
