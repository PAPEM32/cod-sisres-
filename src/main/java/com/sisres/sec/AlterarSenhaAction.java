package com.sisres.sec;

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

import papem32.UsuarioService;

public class AlterarSenhaAction extends Action {
	UsuarioService us = new UsuarioService();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages msgs = new ActionMessages();
		ActionMessages errors = new ActionMessages();

		try {
			AlterarSenhaForm formBean = (AlterarSenhaForm) form;

			HttpSession sessao = request.getSession(false);
			String usu = (String) sessao.getAttribute("usuario");

			us.alteraSenha(usu, formBean.getNovaSenha());
			msgs.add("mensagemSucesso", new ActionMessage("message.senha_alterada"));

			saveMessages(request, msgs);
			return mapping.findForward("sucesso");

		} catch (Exception e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			saveErrors(request, errors);
			// return mapping.getInputForward();
			return mapping.findForward("erro");

		}
	}

}
