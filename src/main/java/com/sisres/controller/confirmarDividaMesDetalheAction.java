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

public class confirmarDividaMesDetalheAction extends org.apache.struts.action.Action {

	public confirmarDividaMesDetalheAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO: Write method body
		try {
			ServletContext context = getServlet().getServletContext();
			HttpSession session = request.getSession();
			ArrayList<Divida> listDivida = (ArrayList<Divida>) session.getAttribute("listaDividaMes");
			// context.log("peguei o ArrayList de Dívida");

			int index = Integer.parseInt(request.getParameter("index"));
			// context.log("converti o index de string pra int." + index);
			Divida divida = (Divida) listDivida.get(index);

			// context.log("peguei a dívida index");
			session.setAttribute("divida", divida);

			return mapping.findForward("detalhe");
		} catch (Exception e) {

			// System.out.println("deu pau!!!!");
			return mapping.findForward("error");
		}

	}
}
