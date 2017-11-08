package cn.longicorn.modules.spring.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验异常
 * @author zhuchanglin
 */
public class CaptchaValidationException extends AuthenticationException {

	private static final long serialVersionUID = -4187675080783168032L;

	public CaptchaValidationException(String msg) {
		super(msg);
	}
	
}
