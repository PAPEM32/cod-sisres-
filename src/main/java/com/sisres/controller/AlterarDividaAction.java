package com.sisres.controller;

import java.util.Date;

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

import com.sisres.model.AltDivMesSispagSvc;
import com.sisres.model.Divida;
import com.sisres.model.Lancamento;
import com.sisres.utilitaria.Utilitaria;
import com.sisres.view.AlterarDividaMesSispagForm;

public class AlterarDividaAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionErrors errors = new ActionErrors();
		ActionMessages messages = new ActionMessages();

		try {

			HttpSession session = request.getSession();
			Divida divida = (Divida) session.getAttribute("divida");
			Lancamento lancamento = divida.getLancamentoInicial();

			AlterarDividaMesSispagForm altform = (AlterarDividaMesSispagForm) form;

			// Pega os dados manipulados e atualiza a dívida

			if (divida.getTipo().equals("MÊS ")) { // para divida do mes

//		lancamento.setNumeroDocAutorizacao(Integer.parseInt(altform.getNumeroDoc()));
				lancamento.setNumeroDocAutorizacao(altform.getNumeroDoc());
				lancamento.setTipoDocAutorizacao(altform.getTipoDoc());
				lancamento.setDataDocAutorizacao(new Date(altform.getDataDoc()));
				lancamento.setOrigemDocAutorizacao(altform.getOrigemDoc());
				lancamento.setObservacao(altform.getObservacao());
				divida.setMotivo(altform.getMotivo().trim());
				// System.out.println("Motivo: "+divida.getMotivo());
				// System.out.println("antes Action data Motivo: ");
				divida.setDataMotivo(
						java.sql.Date.valueOf(Utilitaria.formatarData(altform.getDataMotivo(), "yyyy-MM-dd")));
				// System.out.println("Action data: " +divida.getDataMotivo().toString());
				// System.out.println("Matricula: " + divida.getPessoa().getMatFin());
				// System.out.println("Motivo: "+altform.getMotivo());
				// System.out.println("Action data: " +lancamento.getObservacao());
				// System.out.println("Observação no form: "+altform.getObservacao());

			} else if (divida.getTipo().equals("HISTÓRICA ")) { // para divida historica

				lancamento.setOrigemDocAutorizacao(altform.getOrigemDoc());
				lancamento.setTipoDocAutorizacao(altform.getTipoDoc());
//	    lancamento.setNumeroDocAutorizacao(Integer.parseInt(altform.getNumeroDoc()));
				lancamento.setNumeroDocAutorizacao(altform.getNumeroDoc());
				lancamento.setDataDocAutorizacao(new Date(altform.getDataDoc()));
				lancamento.setObservacao(altform.getObservacao());
				divida.setDocEnvio(altform.getTipoDocEnvio());
//		divida.setNumeroDocEnvio(Integer.parseInt(altform.getNumDocEnvio()));
				divida.setNumeroDocEnvio(altform.getNumDocEnvio());
				divida.setDataDocEnvio(
						java.sql.Date.valueOf(Utilitaria.formatarData(altform.getDataDocEnvio(), "yyyy-MM-dd")));
				divida.setDataMotivo(
						java.sql.Date.valueOf(Utilitaria.formatarData(altform.getDataMotivo(), "yyyy-MM-dd")));
				divida.setMotivo(altform.getMotivo().trim());
//		divida.setProcessoInicioStr(altform.getDtInicio());
//		divida.setProcessoTerminoStr(altform.getDtFim());
				// System.out.println("Motivo: "+divida.getMotivo());
				// System.out.println("antes Action data Motivo: ");
				// System.out.println("Action data: " +divida.getDataMotivo().toString());
				// System.out.println("Matricula: " + divida.getPessoa().getMatFin());
				// System.out.println("Action data: " +lancamento.getObservacao());
				// System.out.println("Observação no form: "+altform.getObservacao());
				// System.out.println("Tipo Divida no form: "+altform.getTipoDivida());

			}

			String usuario = (String) request.getSession().getAttribute("usuarioSispag");
			// System.out.println("Usuário logado: "+usuario);

			AltDivMesSispagSvc altDivSvc = new AltDivMesSispagSvc();

			if (!altDivSvc.setDivida(divida, usuario)) {
				// System.out.println("Não foi possível realizar a operação");
				// errors.add("erroBD" +"", new ActionMessage("error.baseDados.inacessivel"));
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.baseDados.inacessivel"));
				saveErrors(request, errors);
				return mapping.findForward("erro");
			} else {
				// System.out.println("alterar dívida sucesso");
				messages.add("mensagemSucesso", new ActionMessage("message.alterarDividaRealizado"));
				saveMessages(request, messages);
				return mapping.findForward("sucesso");
			}

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (errors.size() > 0) {
				saveErrors(request, errors);
			}

			return mapping.findForward("erro");
		}

	}

}
