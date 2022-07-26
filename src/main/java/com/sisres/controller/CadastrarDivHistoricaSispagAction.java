package com.sisres.controller;

import java.sql.Date;

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

import com.sisres.model.CadDivHistoricaSispagSvc;
import com.sisres.model.Divida;
import com.sisres.model.Lancamento;
import com.sisres.model.OC;
import com.sisres.model.Pessoa;
import com.sisres.model.SispagException;
import com.sisres.view.CadastrarDivHistoricaSispagForm;

public class CadastrarDivHistoricaSispagAction extends Action {

	private static final String SISTEMA_ORIGEM_SISPAG = "S";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		// System.out.println("Action Cadastrar Divida Historica");

		try {

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			CadastrarDivHistoricaSispagForm myForm = (CadastrarDivHistoricaSispagForm) form;

			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");

			// Business logic
			CadDivHistoricaSispagSvc sispagSvc = new CadDivHistoricaSispagSvc(usuario, senhaCripto);
			String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

			String estado = null;

			// if (request.getAttribute("role").equals("ADMINISTRADOR")||
			// request.getAttribute("role").equals("PAPEM-23") ){
			if (request.getSession().getAttribute("role").equals("ADMINISTRADOR")
					|| request.getSession().getAttribute("role").equals("PAPEM-23")) {

				estado = "CONCLUIDO";
			} else {

				estado = "EM ESPERA";
			}

			// System.out.println((String)session.getAttribute("matFin"));
			// System.out.println(myForm.getNome());

			Divida dividaFormulario = new Divida();
			Pessoa pessoaFormulario = new Pessoa();
			Lancamento lancamentoFormulario = new Lancamento();
			OC ocFormulario = new OC();

			/**
			 * DADOS DA DIVIDA
			 */
			dividaFormulario.setMesProcessoGeracao(myForm.getMesIni());
			dividaFormulario.setAnoProcessoGeracao(myForm.getAnoIni());
			dividaFormulario.setMesTermino(myForm.getMesTerm());
			dividaFormulario.setAnoTermino(myForm.getAnoTerm());
			dividaFormulario.setValor(myForm.getValorDivida());
			dividaFormulario.setDocEnvio(myForm.getTipoDocEnvio());
			dividaFormulario.setNumeroDocEnvio(myForm.getNumDocEnvio());
			dividaFormulario.setDataDocEnvio(Date.valueOf(myForm.getDataDocEnvio()));
			dividaFormulario.setDataMotivo(Date.valueOf(myForm.getDataMotivo()));
			dividaFormulario.setMotivo(myForm.getMotivo());
			dividaFormulario.setCgs(SISTEMA_ORIGEM_SISPAG);
			/**
			 * DADOS DOMICILIO BANCARIO
			 */
			dividaFormulario.setAgencia(myForm.getAgencia());
			dividaFormulario.setContaCorrente(myForm.getContaCorrente());
			dividaFormulario.setBanco(myForm.getBanco());
			/**
			 * DADOS OC
			 */
			ocFormulario.setOc(myForm.getOcCodigo());
			ocFormulario.setNome(myForm.getOcNome());
			/**
			 * DADOS PESSOAIS
			 */
			// pessoaFormulario.setMatFin(myForm.getMatFin());
			pessoaFormulario.setMatFin((String) session.getAttribute("matFin"));
			pessoaFormulario.setCpf(myForm.getCpf());
			pessoaFormulario.setNome(myForm.getNome());
			pessoaFormulario.setPosto(myForm.getPosto());
			pessoaFormulario.setSituacao(myForm.getSituacao());
			/**
			 * DADOS LANÇAMENTO
			 */
			lancamentoFormulario.setOrigemDocAutorizacao(myForm.getOrigemDocAut());
			lancamentoFormulario.setTipoDocAutorizacao(myForm.getTipoDocAut());
			lancamentoFormulario.setNumeroDocAutorizacao(myForm.getNumeroDocAut());
			lancamentoFormulario.setDataDocAutorizacao(Date.valueOf(myForm.getDataDocAut()));
			lancamentoFormulario.setObservacao(myForm.getObservacao());
			/**
			 * INSERINDO EM DIVIDA OS DADOS CAPTURADOS NO FORMULARIO
			 */
			dividaFormulario.setPessoa(pessoaFormulario);
			dividaFormulario.setLancamento(lancamentoFormulario);
			dividaFormulario.setOc(ocFormulario);

			try {
				Divida dividaItem = new Divida();
				dividaItem = sispagSvc.confirmaDividaHistorica(dividaFormulario, codUsuario, estado);

				// Store the divida on the request-scope
				session.setAttribute("dividaItem", dividaItem);

				// Store the matFin on the session-scope

				session.setAttribute("matFin", dividaItem.getPessoa().getMatFin());
				// System.out.println((String)session.getAttribute("matFin") + " Session");
				// System.out.println(dividaItem.getPessoa().getMatFin() +
				// "dividaItem.getPessoa().getMatFin()");

				messages.add("mensagemSucesso", new ActionMessage("message.cadastroRealizado"));
				saveMessages(request, messages);

				// System.out.println("Antes do reset");

				// Limpo o form depois que cadastrei
				request.getSession().removeAttribute("dividaItem");
				request.getSession().removeAttribute("matFin");
				request.getSession().removeAttribute("DataIni");
				request.getSession().removeAttribute("DataFim");
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
				return mapping.findForward("erro");

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
			return mapping.findForward("erro");
		}

	}

}
