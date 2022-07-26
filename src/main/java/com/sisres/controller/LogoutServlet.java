package com.sisres.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sisres.sec.SecUtils;

/**
 * Servlet implementation class LogoutServlet
 */

public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -686031138289475457L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext context = getServletContext();

		SecUtils.closeSession(request, response);

		response.sendRedirect(context.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
