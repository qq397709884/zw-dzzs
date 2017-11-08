package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;
import cn.longicorn.modules.web.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现Basic认证的Spring MVC拦截器
 * 认证信息暂时只使用Redis作为认证信息的保存容器。
 */
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

	private IAuthenticationManager authenticationManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			//支持每次认证(REST风格应采用每次认证，但在无法获知用户名密码的第三方应用中只能够使用sid，sid由平台传递给第三方)
			String[] auths = RestAuthUtils.getAuthorization(request);
			IUser user = authenticationManager.login(auths[0], auths[1]);
			if (user != null) {
				Session s = new Session(user);
				bind2Thread(s);
				return true;
			}
		} catch (RestAuthticationException e) {
			// Run the return false branch
		}
		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		return false;
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

	public void setAuthenticationManager(IAuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}