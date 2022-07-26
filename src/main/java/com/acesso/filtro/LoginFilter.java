package com.acesso.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */

public class LoginFilter implements Filter {

	private FilterConfig config;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		ServletContext context = this.config.getServletContext();
		String baseUrl = httpRequest.getRequestURL().substring(0,
				httpRequest.getRequestURL().length() - httpRequest.getRequestURI().length())
				+ httpRequest.getContextPath();

		String url = httpRequest.getRequestURL().toString();
		url = (url == null) ? "/" : url;

		if (url.startsWith(baseUrl + "/novoLogin.jsp") || url.equals(baseUrl + "/") || url.equals(baseUrl + "/login.do")
				|| url.startsWith(baseUrl + "/logout.do") || url.startsWith(baseUrl + "/default/")
				|| url.startsWith(baseUrl + "/library/") || url.startsWith(baseUrl + "/imagens/")) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession sessao = httpRequest.getSession(false);

		if (sessao == null) {
			httpResponse.setStatus(401);
			return;
		}

		String usuario = (String) sessao.getAttribute("usuario");
		String role = (String) sessao.getAttribute("role");
		;

		if (usuario == null) {

			httpResponse.setStatus(401);
			return;
		}

		if (url.startsWith(baseUrl + "/administrador/") && role.equals("ADMINISTRADOR")) {
			chain.doFilter(request, response);
			return;
		}

		if (url.startsWith(baseUrl + "/papem23/")
				&& (role.equals("PAPEM-23") || role.equals("SIPM") || role.equals("ADMINISTRADOR"))) {
			chain.doFilter(request, response);
			return;
		}

		if (url.startsWith(baseUrl + "/sipm/")
				&& (role.equals("PAPEM-23") || role.equals("SIPM") || role.equals("ADMINISTRADOR"))) {
			chain.doFilter(request, response);
			return;
		}

		if (url.startsWith(baseUrl + "/oc/") && (role.equals("PAPEM-23") || role.equals("SIPM")
				|| role.equals("ADMINISTRADOR") || role.equals("OC"))) {
			chain.doFilter(request, response);
			return;
		}

		httpResponse.setStatus(401);

	}

	public void init(FilterConfig config) throws ServletException {

		this.config = config;

	}

	// usado para decodificar a URL


	public void destroy() {
		// TODO Auto-generated method stub

	}

}
