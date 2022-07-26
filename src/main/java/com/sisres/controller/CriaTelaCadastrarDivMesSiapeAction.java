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
import com.sisres.model.CadDivMesSiapeSvc;
import com.sisres.model.Divida;
import com.sisres.model.Motivo;
import com.sisres.model.OC;
import com.sisres.model.SispagException;
import com.sisres.model.Situacao;

public class CriaTelaCadastrarDivMesSiapeAction extends Action {

	private final String REDIRECIONAR_TELA_CADASTRO = "cadastrarDividaMensalSiape.cadastrar";

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
			CadDivMesSiapeSvc cadastrarDividaMesSiapeSVC = new CadDivMesSiapeSvc(usuario, senhaCripto);

			// Divida dividaItem = new Divida();

			//// System.out.println(dividaItem.getAnoProcessoGeracao()+"/"+dividaItem.getMesProcessoGeracao());

			if (session.getAttribute("processoDataStr") == null) {
				Divida divida = cadastrarDividaMesSiapeSVC.obterProcesso();
				String mes = divida.getMesProcessoGeracao();
				String ano = divida.getAnoProcessoGeracao();
				if (mes.length() == 1) {
					mes = "0" + mes;
				}
				String data = mes + "/" + ano;
				session.setAttribute("processoDataStr", data);
			}

			if (session.getAttribute("listaOc") == null) {
				listaOc = (ArrayList<OC>) cadastrarDividaMesSiapeSVC.obterOc();
				session.setAttribute("listaOc", listaOc);
			}

			if (session.getAttribute("listaMotivo") == null) {
				listaMotivo = (ArrayList<Motivo>) cadastrarDividaMesSiapeSVC.obterMotivos();
				session.setAttribute("listaMotivo", listaMotivo);
			}

			if (session.getAttribute("listaBancos") == null) {
				listaBancos = cadastrarDividaMesSiapeSVC.obterListaBancos();
				session.setAttribute("listaBancos", listaBancos);
			}

			if (session.getAttribute("listaSituacoes") == null) {
				listaSituacoes = cadastrarDividaMesSiapeSVC.obterSituacoes();
				session.setAttribute("listaSituacoes", listaSituacoes);
			}

			return mapping.findForward(REDIRECIONAR_TELA_CADASTRO);
		} catch (SispagException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			/* errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ")); */
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error

			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			/* errors.add("error.genericError",new ActionError("ERRO INESPERADO QQ")); */
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}
			return mapping.findForward("erro");
		}

	}
}
