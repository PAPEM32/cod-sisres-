package com.sisres.sec;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SecUtils {
	public static void closeSession(HttpServletRequest request, HttpServletResponse response) {
		/* Removendo atributos de sessao */
		Enumeration<String> s = (Enumeration<String>) request.getSession().getAttributeNames();
		while (s.hasMoreElements()) {
			String a = s.nextElement();
			request.removeAttribute(a);
		}

		/* Loging out usuario no Apache Shiro */
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();

		/*
		 * Procedimentos recomendados pelo owasp. Fonte:
		 * https://www.owasp.org/index.php/Logout
		 * 
		 * OWASP First step : Invalidate user session
		 * 
		 */
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		/*
		 * Second step : Invalidate all cookies by, for each cookie received,
		 * overwriting value and instructing browser to deletes it
		 */
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				cookie.setValue("-");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}
}
