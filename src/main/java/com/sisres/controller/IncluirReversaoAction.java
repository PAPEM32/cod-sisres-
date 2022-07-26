package com.sisres.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.Divida;
import com.sisres.model.IncluirReversaoSvc;
import com.sisres.model.Lancamento;
import com.sisres.model.SispagException;
import com.sisres.view.IncluirReversaoForm;

public class IncluirReversaoAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {
			ServletContext context = getServlet().getServletContext();
			// System.out.println("action-criei o context");

			try {

				IncluirReversaoForm incRevForm = (IncluirReversaoForm) form;
				IncluirReversaoSvc incRevSvc = new IncluirReversaoSvc();
				// System.out.println("criei o form e o svc");

				HttpSession session = request.getSession();

				// //System.out.println(incRevForm.getMes());
				// //System.out.println(incRevForm.getAno());

				Divida divida = (Divida) session.getAttribute("divida");
				int divId = divida.getId();
				// System.out.println(divId);

				Lancamento lanc = (Lancamento) session.getAttribute("lancamento2");
				// int lancId = lanc.getCodigo();
				//// System.out.println(lancId);

				String codUsuario = (String) request.getSession().getAttribute("usuarioSispag");

				// Lancamento reversao = new Lancamento();
				int tipoLanc = 2; // Reversão sempre será 2!
				String operador = "C"; // Reversão sempre será Crédito!

				boolean reversao = false;

				/*
				 * reversao = incRevSvc.incReversao(incRevForm.getNumeroDocAutorizacao(),
				 * incRevForm.getTipoDocAutorizacao(), incRevForm .getDataDocAutorizacao(),
				 * incRevForm.getOrigemDocAutorizacao(), operador, incRevForm.getValor(),
				 * incRevForm.getObservacao(), divId, tipoLanc, //lancId, codUsuario);
				 */

				reversao = incRevSvc.incReversao(incRevForm.getNumeroDocAutorizacaoStr(),
						incRevForm.getTipoDocAutorizacao(), incRevForm.getDataDocAutorizacao(),
						incRevForm.getOrigemDocAutorizacao(), operador, incRevForm.getValor(),
						incRevForm.getObservacao(), divId, tipoLanc,
						// lancId,
						codUsuario);

				//// System.out.println(divida.getLancamentos().listIterator());
				// System.out.println("Valor antes de atualizar:" + divida.getValor());
				if (reversao) {
					divida = incRevSvc.atualizaValorDivida(divida);
					// System.out.println("Valor DEPOIS de atualizar:" + divida.getValor());

					session.setAttribute("divida", divida);
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.lancamentoRealizado"));
					saveMessages(request, messages);

					// System.out.println("Sucesso na Action: IncluirReversaoAction");

					return mapping.findForward("sucesso");
				}
			} catch (SispagException se) {

				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", se.getMessage()));
				if (errors.size() > 0) {
					saveErrors(request, errors);
				}
				// System.out.println("ERRO na Action: IncluirReversaoAction Sispag");

				return mapping.findForward("erro");

			}
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.unexpectedError", e.getMessage()));

			// System.out.println("ERRO na Action: IncluirReversaoAction");

			return mapping.findForward("erro");

		}

		return mapping.findForward("erro");

	}
}
