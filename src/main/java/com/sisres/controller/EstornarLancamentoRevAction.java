package com.sisres.controller;

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

import com.sisres.model.Divida;
import com.sisres.model.EstornarLancamentoRevDividaSvc;
import com.sisres.model.Lancamento;
import com.sisres.view.EstonarLancamentoRevForm;

public class EstornarLancamentoRevAction extends Action {

	Divida divida;

	@SuppressWarnings("deprecation")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// System.out.println("Entrei no estorno");
		ServletContext context = getServlet().getServletContext();

		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {

			HttpSession session = request.getSession();
			EstonarLancamentoRevForm myForm = (EstonarLancamentoRevForm) form;
			String motivo = myForm.getMotivo();
			Lancamento lancamento;
			EstornarLancamentoRevDividaSvc estornoRevSvc = new EstornarLancamentoRevDividaSvc();
			String resultado = "";
			lancamento = (Lancamento) session.getAttribute("lancamento");
			Lancamento lancamento2 = (Lancamento) session.getAttribute("lancamento");
			Divida divida = (Divida) session.getAttribute("divida");

			lancamento.setDividaId(divida.getId());
			lancamento.setResponsavel((String) request.getSession().getAttribute("usuarioSispag"));
			lancamento.setObservacao(motivo);

			// System.out.println("tipo: " + lancamento.getTipo());
			// System.out.println("codigo: " + lancamento.getCodigo());
			// System.out.println("Lançamento: " + lancamento.getIdLancamento());
			// System.out.println("Lançamento: " + lancamento2.getIdLancamento());

			if (lancamento.getTipo().equals("1")) {
				// errors.add("title", new ActionMessage("error.lancamentoInicial.range"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.lancamentoInicial.range"));
				saveErrors(request, errors);// (request, errors);
			} else {

				if (lancamento.getTipo().equals("5")) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.lancamentoEstorno.range"));
					saveErrors(request, errors);// (request, errors);
				} else {

					resultado = estornoRevSvc.estornarLancamentoReversao(lancamento);
					if (resultado == "true") {
						messages.add("mensagemSucesso", new ActionMessage("message.cadastroEstorno"));
						saveMessages(request, messages);
					} else {
						// errors.add("mensagemSucesso", new ActionMessage("error.dividaDuplicada"));
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.estornoexistente.range"));
						saveErrors(request, errors);// (request, errors);
					}
				}
			}

			myForm.reset(mapping, request);

			return mapping.findForward("relatorio");

			// Handle any unexpected expections
		} catch (RuntimeException e) {
			// Log stack trace
			context.log("Erro Inesperado: ", e);
			// Record the error
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("erro.erroInesperado", e.getMessage()));
			// and forward to the error handling page (the form itself)
			return mapping.findForward("erro");
		}

	}

}
