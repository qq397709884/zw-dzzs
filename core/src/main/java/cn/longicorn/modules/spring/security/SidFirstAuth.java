package cn.longicorn.modules.spring.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.filter.GenericFilterBean;

public class SidFirstAuth extends GenericFilterBean {

	private String loginUrl;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String sid = request.getParameter("sid");
		SecurityContext securityContext = (SecurityContext) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		if (securityContext == null && checkSidFormat(sid)) {
			StringBuffer orgiUrl = request.getRequestURL();
			String queryString = removeSidParameter(request.getQueryString());

			String serviceUrl = orgiUrl.toString();
			if (!queryString.isEmpty()) {
				if (orgiUrl.charAt(orgiUrl.length() - 1) == '/') {
					orgiUrl.deleteCharAt(orgiUrl.length() - 1);
				}
				serviceUrl = orgiUrl + "?" + queryString;
			}

			serviceUrl = response.encodeURL(serviceUrl);
			response.sendRedirect(loginUrl + "?sid=" + sid + "&service=" + serviceUrl);
		} else {
			chain.doFilter(req, res);
		}
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	private String removeSidParameter(String queryString) {
		String[] args = queryString.split("&");
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String arg : args) {
			if (arg.contains("sid="))
				continue;
			if (!isFirst)
				sb.append("&");
			else
				isFirst = false;
			sb.append(arg);
		}
		return sb.toString();
	}

	/*
	 * Check the sid has a valid sid formate
	 * @return true valid / false invalid
	 */
	private boolean checkSidFormat(String sid) {
		return StringUtils.isNotBlank(sid) && sid.length() >= 32;
	}
}
