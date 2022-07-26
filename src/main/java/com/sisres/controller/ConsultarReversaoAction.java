package com.sisres.controller;

//Front Controller
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.sisres.model.ConsultaReversaoSvc;
import com.sisres.model.Divida;
import com.sisres.model.Pedido;
import com.sisres.model.SispagException;
import com.sisres.utilitaria.Pager;
import com.sisres.view.ConsultarReversaoForm;

public class ConsultarReversaoAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();

		ActionErrors listaErros = new ActionErrors();

		try {

			ConsultaReversaoSvc consultaReversaoSvc = new ConsultaReversaoSvc();
			ConsultarReversaoForm consulta = (ConsultarReversaoForm) form;
			Divida divida = new Divida();
			DateFormat dfS = new SimpleDateFormat("dd/MM/yyyy");
			Pedido res = new Pedido();

			String[] queryParameters = new String[21];

			// popular o Array
			queryParameters[0] = consulta.getCodReversao();
			if (!consulta.getCodReversao().equals("")) {
				res.setCodigo(consulta.getCodReversao());
			}

			queryParameters[1] = consulta.getAno();

			if (!consulta.getAno().equals("")) {
				res.setAno(Integer.parseInt(consulta.getAno()));
			}

			queryParameters[2] = consulta.getdataReversao();
			if (!consulta.getdataReversao().equals("")) {
				try {
					// incluia a data no formato date.SQL

					res.setRespData(dfS.parse(consulta.getdataReversao()));
				} catch (ParseException edf) {
					edf.printStackTrace();
				}
			}

			queryParameters[3] = consulta.getCodBanco();
			divida.setBanco(consulta.getCodBanco());

			queryParameters[4] = consulta.getTipoDivida();
			divida.setTipo(consulta.getTipoDivida());

			if (!(request.getSession().getAttribute("role").equals("ADMINISTRADOR")
					|| request.getSession().getAttribute("role").equals("PAPEM-23"))) {
				// Recuperando o código da OC do usuário logado.
				queryParameters[18] = (String) request.getSession().getAttribute("codOc");
				// System.out.println("parametro 18:"+ queryParameters[18]);
			} else {
				// Caso o perfil do usuário seja 'ADMINISTRADOR' OU 'PAPEM-23'
				queryParameters[18] = "";
			}

			int totalRegistros = consultaReversaoSvc.getQtdTotalReversao(queryParameters);

//			session.setAttribute("dividaFiltro", divida);

			Pager pager = null;
			Object objectPager = session.getAttribute("pagerConsultaReversao");
			if (objectPager == null) {
				pager = new Pager();
			} else {
				pager = (Pager) objectPager;
			}

			pager.setTotalRegistros(totalRegistros);

			/*
			 * if (!"".equals(consulta.getPagina())) {
			 * pager.setPagSelecionada(Integer.valueOf( consulta.getPagina() )); }
			 */
			/*
			 * session.setAttribute("pager", pager );
			 * 
			 * pager.setQtdPaginas(Integer.valueOf(pager.getQtdPaginas()));
			 */

			//// System.out.println("Consulta registros por pagina :" +
			//// consulta.getRegistrosPorPagina());

//			pager.setRegistrosPorPagina(Integer.valueOf(consulta.getRegistrosPorPagina())); 

			queryParameters[19] = String.valueOf(pager.getLinhaInicio());
			queryParameters[20] = String.valueOf(pager.getLinhaFim());

//			for (int i = 0; i < queryParameters.length; i++) {
//				System.out.print(i + " " + queryParameters[i].toString().trim()); 
//			}

			ArrayList<Divida> listReversao = consultaReversaoSvc.getListReversao(queryParameters);

			request.getSession().setAttribute("listReversao", listReversao);

			// return mapping.findForward("success");
			return mapping.findForward("consultaReversao.listarReversao");

		} catch (SispagException e) {
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("error.filtro");
		} catch (RuntimeException e) {
			context.log("Erro Inesperado: ", e);

			if (listaErros.size() > 0) {

				request.getSession().removeAttribute("listReversao");
				saveErrors(request, listaErros);
			}
			return mapping.findForward("error.filtro");
		}

	}// fim do metodo

} // fim da classe
