package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.web.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现sid认证的Spring MVC拦截器
 * 认证信息暂时只使用Redis作为认证信息的保存容器。
 */
public class SidAuthenticationInterceptor implements HandlerInterceptor {

	private ISessionManager sessionManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Session session = retriveSession(request);
		if (session != null) {
			bind2Thread(session);
			return true;
		}
		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		return false;
	}

	private Session retriveSession(HttpServletRequest request) {
		String sid = request.getParameter("sid");
		return sid == null ? null : sessionManager.loadSession(sid);
	}

	private void bind2Thread(Session s) {
		RestAuthUtils.setSession(s);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		RestAuthUtils.cleanThreadLocalSession();
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}