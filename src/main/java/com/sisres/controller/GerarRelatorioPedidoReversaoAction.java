package com.sisres.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.GerarPedidoReversaoSvc;
import com.sisres.model.SispagException;
import com.sisres.utilitaria.GeradorPedidoReversaoPDF;
import com.sisres.view.GerarPedidoReversaoForm;

public class GerarRelatorioPedidoReversaoAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		// context.log("entrei pelo get e vou começar agora....");

		GerarPedidoReversaoForm myForm = (GerarPedidoReversaoForm) request.getSession().getAttribute("myForm");
		GerarPedidoReversaoSvc GerarPedidoReversaoSvc = new GerarPedidoReversaoSvc();
		String[][] listPedidoReversao;
		try {
			listPedidoReversao = GerarPedidoReversaoSvc.getGerarRelPedidoReversaoList();
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}

		String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

		GeradorPedidoReversaoPDF geradorPedidoReversaoPDF = new GeradorPedidoReversaoPDF();

		String[] listaResponsaveis = new String[6];
		listaResponsaveis[0] = myForm.getPtOrdenador();
		listaResponsaveis[1] = myForm.getOrdenador();
		listaResponsaveis[2] = myForm.getPtAgente();
		listaResponsaveis[3] = myForm.getAgente();
		listaResponsaveis[4] = myForm.getFcOrdenador();
		listaResponsaveis[5] = myForm.getFcAgente();

		geradorPedidoReversaoPDF.montarArquivoPDF(listPedidoReversao, codUsuario, context, request, response,
				listaResponsaveis);

		messages.add("mensagemSucesso", new ActionMessage("sucesso.relatorioPedidoReversao"));
		saveMessages(request, messages);

		return null;
	}
}
