package com.sisres.controller;

import java.util.ArrayList;

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

import com.sisres.model.ConfirmarDividaHistoricaSvc;
import com.sisres.model.Divida;
import com.sisres.model.SispagException;

public class CriaTelaRelacaoDividaHistoricaAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO: Write method body
		ServletContext context = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		// saveMessages(request,errors);
		ArrayList<Divida> listaDividaHistorica = new ArrayList<Divida>();

		try {

			HttpSession session = request.getSession();
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");

			ConfirmarDividaHistoricaSvc confirmarDividaHistoricaSvc = new ConfirmarDividaHistoricaSvc(usuario,
					senhaCripto);
			// ConfirmarDividaMesSvc confirmarDividaMesSvc = new
			// ConfirmarDividaMesSvc(usuario,senhaCripto);
			listaDividaHistorica = (ArrayList<Divida>) confirmarDividaHistoricaSvc.getAllDividaHistoricaEmEspera();
			session.setAttribute("listDividaHistorica", listaDividaHistorica);

			// Envia uma mensagem para o usuário informando que não há dívidas em estado de
			// espera
			// System.out.println(" Verifica lista de dívida Historica"+
			// listaDividaHistorica.size());

			if (listaDividaHistorica.size() == 0) {
				// System.out.println(" não tem divida");
				messages.add("mensagemSucesso", new ActionMessage("message.dividaNConfirmada"));
				saveMessages(request, messages);
			}

			// limpa os dados da sessao referente a pesquisa
			request.getSession().removeAttribute("dividaItem");
			request.getSession().removeAttribute("matFin");

			return mapping.findForward("confirma");

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
