package com.sisres.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//Struts classes
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.CadDivMesSiapeSvc;
import com.sisres.model.Divida;
import com.sisres.model.SispagException;
//Classes do Projeto
import com.sisres.view.CadastrarDivMesSiapeForm;

public class CadastrarDividaMesSiapeAction extends Action {

	private static final String SISTEMA_ORIGEM_SIAPE = "N";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		// System.out.println("Action Cadastrar Divida Mes");

		try {

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			CadastrarDivMesSiapeForm myForm = (CadastrarDivMesSiapeForm) form;
			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");

			// Business logic
			// CadDivMesSispagSvc sispagSvc = new CadDivMesSispagSvc(usuario,senhaCripto);
			CadDivMesSiapeSvc cadastrarDividaSiapeSVC = new CadDivMesSiapeSvc(usuario, senhaCripto);

			String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

			// System.out.println((String)session.getAttribute("matFin"));
			// System.out.println(myForm.getMes());
			// System.out.println(myForm.getAno());
			// System.out.println(myForm.getNome());

			Divida dividaItem = new Divida();

			try {

				// System.out.println("vai testar...");

				String ocSessao = (String) session.getAttribute("codOc");
				// System.out.println("OC da sessão :" + ocSessao);
				String ocDivida = myForm.getOcCodigo();
				// System.out.println("OC do form: " + ocDivida);
				String perfil = (String) session.getAttribute("perfilUsuario");
				// System.out.println("Perfil" + perfil);

				if (!ocSessao.equals(ocDivida)) {
					// System.out.println("OC da divida é diferente do OC do usuário. \n Testando
					// Perfil...");
					if (!(perfil.equals("ADMINISTRADOR")) && !(perfil.equals("PAPEM-23"))) {
						// System.out.println("Não é nem ADMINISTRADOR nem PAPEM-23. Erro --> não
						// cadastrará a dívida.");
						errors.add("matFin", new ActionMessage("error.dividaPertencenteOutraOC"));
						saveErrors(request, errors);
						return mapping.findForward("cadastrardDividaMesSiape.erro");
					}
				}

				// System.out.println("entrou no corfirmar divida.....");

				dividaItem = cadastrarDividaSiapeSVC.confirmaDivMes(SISTEMA_ORIGEM_SIAPE, perfil, myForm.getMatFin(),
						myForm.getMes(), myForm.getAno(), myForm.getNome(), myForm.getCpf(), myForm.getPosto(),
						myForm.getSituacao(), myForm.getOcCodigo(), myForm.getOcNome(), myForm.getBanco(),
						myForm.getAgencia(), myForm.getContaCorrente(), myForm.getValorDivida(),
						myForm.getOrigemDocAut(), myForm.getTipoDocAut(), myForm.getNumeroDocAut(),
						myForm.getDataDocAut(), myForm.getDataMotivo(), myForm.getMotivo(), myForm.getObservacao(),
						codUsuario);

				// Store the divida on the request-scope
				session.setAttribute("dividaItem", dividaItem);

				// Store the matFin on the session-scope

				session.setAttribute("matFin", dividaItem.getPessoa().getMatFin());
				// System.out.println((String)session.getAttribute("matFin") + " Session");
				// System.out.println(dividaItem.getPessoa().getMatFin() +
				// "dividaItem.getPessoa().getMatFin()");
				// System.out.println("Processo
				// teste"+dividaItem.getAnoProcessoGeracao()+"/"+dividaItem.getMesProcessoGeracao());

				messages.add("mensagemSucesso", new ActionMessage("message.cadastroRealizado"));
				saveMessages(request, messages);

				// System.out.println("Antes do reset");

				// Limpo o form depois que cadastrei
				request.getSession().removeAttribute("dividaItem");
				request.getSession().removeAttribute("matFin");
				myForm.reset(mapping, request);

				// System.out.println("DEPOIS do reset");

				// Dispatch to Success view
				return mapping.findForward("success");

			} catch (SispagException se) {
				// TODO: handle exception
				/*
				 * errors.add(ActionMessages.GLOBAL_MESSAGE, new
				 * ActionMessage("error.dividaDuplicada", e.getMessage()));
				 */
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
				if (errors.size() > 0) {
					saveErrors(request, errors);
				}

				myForm.reset(mapping, request);
				// Dispatch to Success view
				return mapping.findForward("cadastrardDividaMesSiape.erro");

			}
			// Handle any unexpected expections
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			// and forward to the error handling page (the form itself)
			return mapping.findForward("cadastrardDividaMesSiape.erro");
		}

	}
}
