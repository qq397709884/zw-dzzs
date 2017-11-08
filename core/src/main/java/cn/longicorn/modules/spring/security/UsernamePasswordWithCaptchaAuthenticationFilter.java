package cn.longicorn.modules.spring.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 扩展<code>UsernamePasswordAuthenticationFilter</code>以支持插入验证码的校验
 * 
 * @author zhangzhenxin
 * @author zhuchanglin
 */
public class UsernamePasswordWithCaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements
		MessageSourceAware {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String inputValidationCode = request.getParameter(Constants.CAPTCHA_KEY);

		String validationCodeInSession = (String) request.getSession().getAttribute(Constants.CAPTCHA_KEY);
		request.getSession().removeAttribute(Constants.CAPTCHA_KEY);
				
		if (validationCodeInSession != null && (inputValidationCode == null || inputValidationCode.equals(""))) {
			throw new CaptchaValidationException(messages.getMessage("Security.Captcha.noCaptcha", "Need captcha"));
		}

		if (validationCodeInSession != null && !validationCodeInSession.equalsIgnoreCase(inputValidationCode)) {
			throw new CaptchaValidationException(messages.getMessage("Security.Captcha.invalid", "Captcha error"));
		}

		return super.attemptAuthentication(request, response);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authResult) throws IOException, ServletException {
		request.getSession().removeAttribute(Constants.LOGIN_FAIL_TIMES_KEY);
		super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Object times = request.getSession().getAttribute(Constants.LOGIN_FAIL_TIMES_KEY);
		Integer failTimes = times == null ? 1 : (Integer) times + 1;
		request.getSession().setAttribute(Constants.LOGIN_FAIL_TIMES_KEY, failTimes);
		super.unsuccessfulAuthentication(request, response, failed);
	}

}