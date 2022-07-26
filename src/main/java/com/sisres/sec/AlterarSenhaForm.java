package com.sisres.sec;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import papem32.UsuarioService;

public class AlterarSenhaForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5067439246073211419L;
	private String senhaAtual;
	private String novaSenha;
	private String confirmaSenha;

	UsuarioService usvc =new UsuarioService();
	
	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha != null ? confirmaSenha.trim() : "";
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha != null ? novaSenha.trim() : "";
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual != null ? senhaAtual.trim() : "";
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (getSenhaAtual().equals("") || getNovaSenha().equals("") || getConfirmaSenha().equals(""))
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.senhas_branco"));

		String us = (String) SecurityUtils.getSubject().getPrincipal();
		//AuthenticationToken token = new UsernamePasswordToken(us, new AdmSegPwHash().codificaSenha(getSenhaAtual()));
		//securityUtils.getSecurityManager().authenticate(token);
		if(!usvc.verificaSenha(us, getSenhaAtual())){		
			 errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.senha_atual_errada"));		
		}

		if (!getNovaSenha().equals(getConfirmaSenha()))
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.nova_senha_dif"));

		return errors;
	}
}
