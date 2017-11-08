package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;

/**
 * 适用于需要采用每次Basic或其他认证的验证场合，禁止有状态的session id机制
 */
public class SessionManagerDummy implements ISessionManager {

	@Override
	public boolean isValid(final String sid) {
		return false;
	}

	@Override
	public Session loadSession(final String sid) {

		return null;
	}

	@Override
	public boolean remove(String sid) {
		return false;
	}

	@Override
	public String saveOrUpdateSession(IUser user) {
		return null;
	}

}
