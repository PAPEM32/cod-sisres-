package com.sisres.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sisres.model.DAODivida;
import com.sisres.model.GerarPedidoReversaoSvc;
import com.sisres.model.SispagException;
import com.sisres.view.GerarPedidoReversaoForm;

public class GerarPedidoReversaoAction extends Action {
	
	private Logger logger = LoggerFactory.getLogger(GerarPedidoReversaoAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();

		ActionErrors listaErros = new ActionErrors();

		String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

		// pego os dados do form
		GerarPedidoReversaoForm myForm = (GerarPedidoReversaoForm) form;

		GerarPedidoReversaoSvc GerarPedidoReversaoSvc = new GerarPedidoReversaoSvc();

		request.getSession().setAttribute("myForm", myForm);

		// System.out.println("Gerar Pedido Reversão Action");

		try {

			Boolean gerarPedidoReversao = GerarPedidoReversaoSvc.gerarPedidoReversao(myForm, codUsuario);

			if (gerarPedidoReversao) {
				// System.out.println("retornou success");
				return mapping.findForward("success");

			} else {

				if (listaErros.size() > 0) {

					saveErrors(request, listaErros);
				}
				// System.out.println("retornou o error");

				return mapping.findForward("error");

			}

		} catch (SispagException e1) {
			logger.error(e1.getMessage(),e1);
		} catch (RuntimeException e) {
			context.log("Erro Inesperado: ", e);
			if (listaErros.size() > 0) {

				saveErrors(request, listaErros);
			}
			return mapping.findForward("error");
		}
		return null;

	}// fim do metodo

} // fim da classe
