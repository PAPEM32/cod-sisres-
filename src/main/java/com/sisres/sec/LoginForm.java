package com.sisres.sec;

import org.apache.struts.action.ActionForm;

public class LoginForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7779755228054966822L;
	String usuario;
	String senha;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
