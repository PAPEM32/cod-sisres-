package com.sisres.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutSisresAction extends Action {
	public static final String SISRES_LOGOUT = "logout.sisres";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession sessao = request.getSession();
		sessao.removeAttribute("autenticado");
		Enumeration<String> atrs = sessao.getAttributeNames();
		while (atrs.hasMoreElements())
			sessao.removeAttribute(atrs.nextElement());

		sessao.invalidate();
		return mapping.findForward(SISRES_LOGOUT);

	}

}
