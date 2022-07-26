package com.sisres.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.sisres.model.CadDivHistoricaSiapeSvc;
import com.sisres.model.Motivo;
import com.sisres.model.OC;
import com.sisres.model.Situacao;

public class CriaTelaCadastrarDivHistoricaSiapeAction extends Action {

	private final String REDIRECIONAR_TELA_CADASTRO = "cadastrarDividaHistoricaSiape.cadastrar";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();

		ArrayList<Motivo> listaMotivo = new ArrayList<Motivo>();
		ArrayList<OC> listaOc = new ArrayList<OC>();
		List<Banco> listaBancos = new ArrayList<Banco>();
		List<Situacao> listaSituacoes = new ArrayList<Situacao>();

		try {
			HttpSession session = request.getSession();
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");
			CadDivHistoricaSiapeSvc cadastrarDividaHistoricaSiapeSVC = new CadDivHistoricaSiapeSvc(usuario,
					senhaCripto);

			if (session.getAttribute("listaOc") == null) {
				listaOc = (ArrayList<OC>) cadastrarDividaHistoricaSiapeSVC.obterOc();
				session.setAttribute("listaOc", listaOc);
			}

			if (session.getAttribute("listaMotivo") == null) {
				listaMotivo = (ArrayList<Motivo>) cadastrarDividaHistoricaSiapeSVC.obterMotivos();
				session.setAttribute("listaMotivo", listaMotivo);
			}

			if (session.getAttribute("listaBancos") == null) {
				listaBancos = cadastrarDividaHistoricaSiapeSVC.obterListaBancos();
				session.setAttribute("listaBancos", listaBancos);
			}

			if (session.getAttribute("listaSituacoes") == null) {
				listaSituacoes = cadastrarDividaHistoricaSiapeSVC.obterSituacoes();
				session.setAttribute("listaSituacoes", listaSituacoes);
			}

			return mapping.findForward(REDIRECIONAR_TELA_CADASTRO);

		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			// System.out.println("erro2 -----------------------------------");

			return mapping.findForward("erro");
		}

	}

}
