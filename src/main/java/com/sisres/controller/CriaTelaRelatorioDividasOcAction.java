package com.sisres.controller;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.OC;
import com.sisres.model.RelatorioSisresSvc;
import com.sisres.model.SispagException;

public class CriaTelaRelatorioDividasOcAction extends org.apache.struts.action.Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO: Write method body
		ServletContext context = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();
		// saveMessages(request,errors);
		ArrayList<OC> listadeoc = new ArrayList<OC>();

		try {
			HttpSession sessao = request.getSession();

			String codOc = (String) sessao.getAttribute("codOc");
			String perfil = (String) sessao.getAttribute("perfilUsuario");
			String usuario = (String) sessao.getAttribute("usuarioSisres");
			String senha = (String) sessao.getAttribute("passwordSisres");
			// TODO: Migrado P32 - OK
			String role = (String) sessao.getAttribute("role");

			RelatorioSisresSvc relatorioSvc = new RelatorioSisresSvc();

			// TODO: Migrado P32 - OK
			/*
			 * if (request.isUserInRole("OC")||request.isUserInRole("SIPM")) { OC oc =
			 * relatorioSvc.getOC(codOc); listadeoc.add(oc); }else if
			 * (request.getSession().getAttribute("role").equals("PAPEM-23")||
			 * request.getSession().getAttribute("role").equals("ADMINISTRADOR")){ listadeoc
			 * = (ArrayList<OC>)relatorioSvc.getAllOC(); }
			 */

			if (role.equals("OC") || role.equals("SIPM")) {
				OC oc = relatorioSvc.getOC(codOc);
				listadeoc.add(oc);
			} else if (role.equals("PAPEM-23") || request.getSession().getAttribute("role").equals("ADMINISTRADOR")) {
				listadeoc = (ArrayList<OC>) relatorioSvc.getAllOC();
			}

			request.setAttribute("listadeoc", listadeoc);
			// Verificar com o Fábio solução para linha de baixo
			sessao.setAttribute("listadeoc", listadeoc);
			return mapping.findForward("relatorio");
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
