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
import com.sisres.view.CadastrarDividaHistoricaSiapeForm;

public class CadastrarDividaHistoricaSiapeAction extends Action {

	private static final String SISTEMA_ORIGEM_SIAPE = "N";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ServletContext context = getServlet().getServletContext();

		// Use Struts actions to record business processing errors.
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {

			// retrieve the "library" attribute from the session-scope
			HttpSession session = request.getSession();

			// Cast the action form to an application-specific form
			CadastrarDividaHistoricaSiapeForm cadastrarDividaHistoricaSiapeForm = (CadastrarDividaHistoricaSiapeForm) form;

			String usuario = (String) session.getAttribute("usuarioSispag");
			String senhaCripto = (String) session.getAttribute("passwordSispag");

			// Business logic
			CadDivHistoricaSispagSvc sispagSvc = new CadDivHistoricaSispagSvc(usuario, senhaCripto);
			String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

			String estado = null;

			if (request.getSession().getAttribute("role").equals("ADMINISTRADOR")
					|| request.getSession().getAttribute("role").equals("PAPEM-23")) {

				estado = "CONCLUIDO";
			} else {

				estado = "EM ESPERA";
			}

			// System.out.println((String)session.getAttribute("matFin"));
			// System.out.println(cadastrarDividaHistoricaSiapeForm.getNome());

			Divida dividaFormulario = new Divida();
			Pessoa pessoaFormulario = new Pessoa();
			Lancamento lancamentoFormulario = new Lancamento();
			OC ocFormulario = new OC();

			/**
			 * DADOS DA DIVIDA
			 */
			dividaFormulario.setMesProcessoGeracao(cadastrarDividaHistoricaSiapeForm.getMesIni());
			dividaFormulario.setAnoProcessoGeracao(cadastrarDividaHistoricaSiapeForm.getAnoIni());
			dividaFormulario.setMesTermino(cadastrarDividaHistoricaSiapeForm.getMesTerm());
			dividaFormulario.setAnoTermino(cadastrarDividaHistoricaSiapeForm.getAnoTerm());
			dividaFormulario.setValor(cadastrarDividaHistoricaSiapeForm.getValorDivida());
			dividaFormulario.setDocEnvio(cadastrarDividaHistoricaSiapeForm.getTipoDocEnvio());
			dividaFormulario.setNumeroDocEnvio(cadastrarDividaHistoricaSiapeForm.getNumDocEnvio());
			dividaFormulario.setDataDocEnvio(Date.valueOf(cadastrarDividaHistoricaSiapeForm.getDataDocEnvio()));
			dividaFormulario.setDataMotivo(Date.valueOf(cadastrarDividaHistoricaSiapeForm.getDataMotivo()));
			dividaFormulario.setMotivo(cadastrarDividaHistoricaSiapeForm.getMotivo());
			dividaFormulario.setCgs(SISTEMA_ORIGEM_SIAPE);
			/**
			 * DADOS DOMICILIO BANCARIO
			 */
			dividaFormulario.setAgencia(cadastrarDividaHistoricaSiapeForm.getAgencia());
			dividaFormulario.setContaCorrente(cadastrarDividaHistoricaSiapeForm.getContaCorrente());
			dividaFormulario.setBanco(cadastrarDividaHistoricaSiapeForm.getBanco());
			/**
			 * DADOS OC
			 */
			ocFormulario.setOc(cadastrarDividaHistoricaSiapeForm.getOcCodigo());
			ocFormulario.setNome(cadastrarDividaHistoricaSiapeForm.getOcNome());
			/**
			 * DADOS PESSOAIS
			 */
			pessoaFormulario.setMatSIAPE(cadastrarDividaHistoricaSiapeForm.getMatFin());
			// pessoaFormulario.setMatFin((String)session.getAttribute("matFin"));
			pessoaFormulario.setCpf(cadastrarDividaHistoricaSiapeForm.getCpf());
			pessoaFormulario.setNome(cadastrarDividaHistoricaSiapeForm.getNome());
			pessoaFormulario.setPosto(cadastrarDividaHistoricaSiapeForm.getPosto());
			pessoaFormulario.setSituacao(cadastrarDividaHistoricaSiapeForm.getSituacao());
			/**
			 * DADOS LANÇAMENTO
			 */
			lancamentoFormulario.setOrigemDocAutorizacao(cadastrarDividaHistoricaSiapeForm.getOrigemDocAut());
			lancamentoFormulario.setTipoDocAutorizacao(cadastrarDividaHistoricaSiapeForm.getTipoDocAut());
			lancamentoFormulario.setNumeroDocAutorizacao(cadastrarDividaHistoricaSiapeForm.getNumeroDocAut());
			lancamentoFormulario.setDataDocAutorizacao(Date.valueOf(cadastrarDividaHistoricaSiapeForm.getDataDocAut()));
			lancamentoFormulario.setObservacao(cadastrarDividaHistoricaSiapeForm.getObservacao());
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

				session.setAttribute("matFin", dividaItem.getPessoa().getMatSIAPE());

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
				cadastrarDividaHistoricaSiapeForm.reset(mapping, request);

				// System.out.println("DEPOIS do reset");

				// Dispatch to Success view
				return mapping.findForward("cadastrarDividaHistoricaSiape.confirmarDivida");

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

				cadastrarDividaHistoricaSiapeForm.reset(mapping, request);
				// Dispatch to Success view
				return mapping.findForward("cadastrarDividaHistoricaSiape.erro");

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
			return mapping.findForward("cadastrarDividaHistoricaSiape.erro");
		}

	}
}
