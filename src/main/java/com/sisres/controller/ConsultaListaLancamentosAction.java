package com.sisres.controller;

//Front Controller
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

import com.sisres.model.ConsultaLancamentoSvc;
import com.sisres.model.Lancamento;
import com.sisres.model.SispagException;
import com.sisres.view.ConsultarListaLancamentosForm;

public class ConsultaListaLancamentosAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		// context.log("action passei por aqui 1");

		ActionErrors listaErros = new ActionErrors();

		try {

			// String codDivida = (String)request.getParameter("codDivida");
			ConsultarListaLancamentosForm consulta = (ConsultarListaLancamentosForm) form;
			// context.log("action passei por aqui 2");
			// System.out.println("Action "+ consulta.getDividaId());
			ConsultaLancamentoSvc consultaLancamentoSvc = new ConsultaLancamentoSvc();
			// context.log("action passei por aqui 3");
			ArrayList<Lancamento> listLancamentos = consultaLancamentoSvc
					.readLancamentosporDivida(consulta.getDividaId());
			// context.log("Consegui sair do Loop.");

			// seta o atributo que sera lido pelo browser

			request.getSession().setAttribute("listLancamentos", listLancamentos);

			return mapping.findForward("lancamentos");

		} catch (SispagException e) {
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("error");
		} catch (RuntimeException e) {
			context.log("Erro Inesperado: ", e);
			if (listaErros.size() > 0) {

				request.getSession().removeAttribute("listLancamentos");
				saveErrors(request, listaErros);
			}
			return mapping.findForward("error");
		}

	}// fim do metodo

} // fim da classe
