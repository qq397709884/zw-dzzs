package cn.longicorn.modules.security.rest;

import org.apache.commons.lang3.StringUtils;

import cn.longicorn.modules.security.utils.Digests;
import cn.longicorn.modules.spring.security.IUser;
import cn.longicorn.modules.spring.security.IUserManager;
import cn.longicorn.modules.utils.Encodes;

public class DefaultAuthenticationManager implements IAuthenticationManager {

	private IUserManager<? extends IUser> userManager;

	private boolean needMd5 = false;

	@Override
	public IUser login(String username, String password) {
		String pwd;
		IUser user = userManager.getUserByLoginName(username);
		if (user == null)
			return null;

		pwd = encodePassword(user.getPassword());
		if (StringUtils.equals(pwd, password)) {
			return user;
		} else {
			return null;
		}
	}

	private String encodePassword(String plainPass) {
		if (plainPass != null && needMd5) {
			return Encodes.encodeHex(Digests.md5(plainPass.getBytes())).toUpperCase();
		} else {
			return plainPass;
		}
	}

	public void setUserManager(IUserManager<? extends IUser> userManager) {
		this.userManager = userManager;
	}
}
