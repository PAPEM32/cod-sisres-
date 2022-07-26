package com.sisres.controller;

import java.sql.SQLException;

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

import com.sisres.model.ReemitirPedidoReversaoSvc;
import com.sisres.model.SispagException;
import com.sisres.utilitaria.GeradorPedidoReversaoPDF;
import com.sisres.view.ReemitirPedidoReversaoForm;

public class ReemitirPedidoReversaoAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();

		ActionErrors listaErros = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		Integer idCodigoPedidoReversao = null;
		String[][] retornoListaDividasPedidoReversao = null;
		ReemitirPedidoReversaoForm reemitirPedidoReversaoForm = (ReemitirPedidoReversaoForm) form;
		try {
			if (reemitirPedidoReversaoForm.getAcao().equals("filtrarPedidoReversao")) {
				ReemitirPedidoReversaoSvc reemitirPedidoReversaoSVC = new ReemitirPedidoReversaoSvc();
				idCodigoPedidoReversao = reemitirPedidoReversaoSVC.obterIdPedidoReversao(
						Integer.parseInt(reemitirPedidoReversaoForm.getCodigoPedidoReversao()),
						Integer.parseInt(reemitirPedidoReversaoForm.getAnoReversao()));

				retornoListaDividasPedidoReversao = reemitirPedidoReversaoSVC.recuperarListaDividasPedidoReversao(
						idCodigoPedidoReversao, Integer.parseInt(reemitirPedidoReversaoForm.getAnoReversao()));

				request.getSession().setAttribute("listaDividasPedidoReversao", retornoListaDividasPedidoReversao);
			}
			if (reemitirPedidoReversaoForm.getAcao().equals("reemitirPedidoReversao")) {
				String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

				GeradorPedidoReversaoPDF geradorPedidoReversaoPDF = new GeradorPedidoReversaoPDF();

				String[] listaResponsaveis = new String[6];
				listaResponsaveis[0] = reemitirPedidoReversaoForm.getPtOrdenador();
				listaResponsaveis[1] = reemitirPedidoReversaoForm.getOrdenador();
				listaResponsaveis[2] = reemitirPedidoReversaoForm.getPtAgente();
				listaResponsaveis[3] = reemitirPedidoReversaoForm.getAgente();
				listaResponsaveis[4] = reemitirPedidoReversaoForm.getFcOrdenador();
				listaResponsaveis[5] = reemitirPedidoReversaoForm.getFcAgente();

				geradorPedidoReversaoPDF.montarArquivoPDF(
						(String[][]) request.getSession().getAttribute("listaDividasPedidoReversao"), codUsuario,
						context, request, response, listaResponsaveis);
				session.removeAttribute("listaDividasPedidoReversao");

				messages.add("mensagemSucesso", new ActionMessage("sucesso.relatorioPedidoReversao"));
				saveMessages(request, messages);

				// return null;
			}
			return mapping.findForward("reemitirPedidoReversao.listarReversao");
		} catch (SispagException e) {
			System.out.println(e.getMessage());
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("reemitirPedidoReversao.error");
		} catch (SQLException e) {
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("reemitirPedidoReversao.error");
		}

	}
}
