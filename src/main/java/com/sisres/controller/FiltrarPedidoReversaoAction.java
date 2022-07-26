package com.sisres.controller;

import java.util.ArrayList;

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

import com.sisres.model.Divida;
import com.sisres.model.FiltrarPedidoReversaoSvc;
import com.sisres.model.SispagException;
import com.sisres.view.FiltrarPedidoReversaoForm;

public class FiltrarPedidoReversaoAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		context.log("Entrei no filtro da reversão");

		ActionErrors errors = new ActionErrors();

		// pego os dados do form
		FiltrarPedidoReversaoForm myForm = (FiltrarPedidoReversaoForm) form;

		FiltrarPedidoReversaoSvc FiltrarPedidoReversaoSvc = new FiltrarPedidoReversaoSvc();

		ArrayList<Divida> listDivida;

		try {

			// com.sisres.controller.FiltrarPedidoReversaoAction
			listDivida = FiltrarPedidoReversaoSvc.getDividaReversaoList(myForm);
			request.getSession().setAttribute("listDividaReversao", listDivida);
			request.getSession().setAttribute("banco", myForm.getBanco());
			request.getSession().setAttribute("codTipo", myForm.getTipoDivida());

			return mapping.findForward("success");

		} catch (SispagException e) {

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			// System.out.println("ERRO na Action: FiltrarPedidoReversaoAction");

			// P32 alterado de erro para erro.filtro
			// return mapping.findForward("error.filtro");
			return mapping.findForward("erro");

		} catch (RuntimeException e) {
			context.log("Erro Inesperado: ", e);
			if (errors.size() > 0) {

				request.getSession().removeAttribute("listDividaReversao");
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}

	}// fim do metodo

} // fim da classe
