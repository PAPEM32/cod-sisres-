package com.sisres.controller;

import java.util.ArrayList;
import java.util.Enumeration;

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
import com.sisres.model.Motivo;
import com.sisres.model.OC;
import com.sisres.model.SispagException;

public class CriaTelaCadastrarDivHistoricaSispagAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// TODO: Write method body
		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();
		// saveMessages(request,errors);
		ArrayList<Motivo> listaMotivo = new ArrayList<Motivo>();
		ArrayList<OC> listaOc = new ArrayList<OC>();

		try {
			HttpSession session = request.getSession();

			Enumeration<String> en = session.getAttributeNames();
			while (en.hasMoreElements()) {
				String name = en.nextElement();
				if ((name == "dividaItem") || (name == "matFin") || (name == "DataIni") || (name == "DataFim")) {
					session.removeAttribute(name);
				}
			}

			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			CadDivHistoricaSispagSvc cadastrarSvc = new CadDivHistoricaSispagSvc(usuario, senhaCripto);

			listaMotivo = (ArrayList<Motivo>) cadastrarSvc.getAllMotivo();
			listaOc = (ArrayList<OC>) cadastrarSvc.getAllOc();

			session.setAttribute("listaMotivo", listaMotivo);
			session.setAttribute("listaOc", listaOc);

			return mapping.findForward("cadastro");
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			/* errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ")); */
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			/* errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ")); */
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}

	}

}
