package com.sisres.controller;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sisres.model.Divida;
import com.sisres.model.Lancamento;

public class ConsultarDividaDetalheAction extends org.apache.struts.action.Action {

	// Global Forwards
	public static final String GLOBAL_FORWARD_welcome = "welcome";

	// Local Forwards

	public ConsultarDividaDetalheAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO: Write method body
		try {
			ServletContext context = getServlet().getServletContext();
			HttpSession session = request.getSession();
			ArrayList<Divida> listDivida = (ArrayList<Divida>) session.getAttribute("listDivida");
			// context.log("peguei o ArrayList de Dívida");

			int index = Integer.parseInt(request.getParameter("index"));
			// context.log("converti o index de string pra int." + index);
			Divida divida = new Divida(); // = (Divida)listDivida.get(index);

			for (int i = 0; i < listDivida.size(); i++) {
				if (listDivida.get(i).getId() == index) {
					divida = (Divida) listDivida.get(i);
					break;
				}
			}

			Lancamento lancamento = divida.getLancamentoInicial();
			// context.log("peguei a dívida index");
			// context.log("seu estado é" + divida.getEstado());
			session.setAttribute("divida", divida);
			session.setAttribute("lancamento", lancamento);
			session.removeAttribute("listLancamentos");
			// System.out.println("Código da Dívida "+ divida.getCodigo());
			// System.out.println("Valor da Dívida "+divida.getValor());
			// System.out.println("ID da Dívida "+ divida.getId());
			// System.out.println("Doc Envio "+ divida.getDocEnvio());
			// System.out.println("tipo divida "+ divida.getTipo());

			return mapping.findForward("detalhe");
		} catch (Exception e) {

			// System.out.println("deu pau!!!!");
			return mapping.findForward("error");
		}
	}

}