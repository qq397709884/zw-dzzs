package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;

public class PassportManager {

	private IAuthenticationManager authenticationManager;

	private ISessionManager sessionManager;

	/**
	 * 执行登录，返回sid
	 * @param auths	[0] 用户名 [1]密码
	 * @return	登录后的sid，如果登录失败，返回null
	 */
	public String doLogin(String[] auths) {
		IUser user = authenticationManager.login(auths[0], auths[1]);
		return user == null ? null : sessionManager.saveOrUpdateSession(user);
	}

	public boolean logout(String sid) {
		return sessionManager.remove(sid);
	}

	public void setAuthenticationManager(IAuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}