package com.sisres.controller;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sisres.model.Lancamento;

public class ConsultarLancamentoDetalheAction extends org.apache.struts.action.Action {

	// Global Forwards
	public static final String GLOBAL_FORWARD_welcome = "welcome";

	// Local Forwards

	public ConsultarLancamentoDetalheAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO: Write method body
		try {
			ServletContext context = getServlet().getServletContext();
			HttpSession session = request.getSession();
			ArrayList<Lancamento> listLancamentos = (ArrayList<Lancamento>) session.getAttribute("listLancamentos");
			// context.log("peguei o ArrayList de Lancamentos");

			int index2 = Integer.parseInt(request.getParameter("index2"));
			// context.log("converti o index2 de string pra int." + index2);
			Lancamento lancamento = (Lancamento) listLancamentos.get(index2);

			// context.log("peguei o lancamento index2");
			session.setAttribute("lancamento", lancamento);
			session.removeAttribute("listLancamentos");
			// System.out.println("Código do Lancamento "+ lancamento.getCodigo());
			// System.out.println("Valor da Dívida "+lancamento.getValor());

			return mapping.findForward("detLancamento");
		} catch (Exception e) {

			// System.out.println("deu pau!!!");
			return mapping.findForward("error");
		}
	}

}