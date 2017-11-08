package cn.longicorn.modules.spring.security;

public interface Constants {
	
	/**
	 * 存储在HttpSession中的验证码KEY
	 */
	public static final String CAPTCHA_KEY = "captcha";
	
	/**
	 * 存储在HttpSession中的登录失败次数
	 */
	public static final String LOGIN_FAIL_TIMES_KEY = "login_fail_times";
	
}
