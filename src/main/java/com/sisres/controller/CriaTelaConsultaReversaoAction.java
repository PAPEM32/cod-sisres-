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

import com.sisres.model.Banco;
import com.sisres.model.GerarPedidoReversaoSvc;
import com.sisres.model.SispagException;

public class CriaTelaConsultaReversaoAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// System.out.println("Glaucia entrou aqui");

		// TODO: Write method body
		ServletContext context = getServlet().getServletContext();
		ActionErrors errors = new ActionErrors();
		// saveMessages(request,errors);
		// ArrayList<OC> listaOc = new ArrayList<OC>();
		// ArrayList<Motivo> listaMotivo = new ArrayList<Motivo>();
		ArrayList<Banco> listaBanco = new ArrayList<Banco>();

		try {

			HttpSession session = request.getSession();
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			// ConsultaDividaSvc consultarSvc = new ConsultaDividaSvc(usuario,senhaCripto);

			/*
			 * listaOc = (ArrayList<OC>)consultarSvc.getAllOc();
			 * session.setAttribute("listaOc", listaOc);
			 * 
			 * listaMotivo = (ArrayList<Motivo>)consultarSvc.getAllMotivo();
			 * session.setAttribute("listaMotivo", listaMotivo);
			 */

			// GerarPedidoReversaoSvc gerarPedidoSvc = new GerarPedidoReversaoSvc
			// (usuario,senhaCripto);
			GerarPedidoReversaoSvc gerarPedidoSvc = new GerarPedidoReversaoSvc();
			listaBanco = (ArrayList<Banco>) gerarPedidoSvc.getAllBanco();

			session.setAttribute("listaBanco", listaBanco);
			request.getSession().removeAttribute("banco");

			/*
			 * Neste momento estamos entrando na funcionalidade de consulta de reversão pelo
			 * menu do sistema. Ao entrarmos na tela de consulta, o resultado da pesquisa
			 * anterior deve ser apagado. Isto será feito verificando se existe em memória
			 * (sessão) a lista de resultados. Caso positivo, removeremos o atributo da
			 * sessão.
			 */
			if (session.getAttribute("listReversao") != null) {
				session.removeAttribute("listReversao");
				session.removeAttribute("reversaoFiltro");
			}
			// return mapping.findForward("cadastro");
			return mapping.findForward("consultaReversao.telaConsulta");

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