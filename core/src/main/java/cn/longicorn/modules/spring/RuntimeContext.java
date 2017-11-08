package cn.longicorn.modules.spring;

import cn.longicorn.modules.spring.security.IUser;
import cn.longicorn.modules.spring.security.IUserManager;
import cn.longicorn.modules.spring.security.SpringSecurityUtils;

/**
 * 便于在任何地方获取运行时信息，包括
 * <ul>
 * <li>当前登录用户的信息</li>
 * </ul>
 * @author zhuchanglin
 * 
 * Define this bean in applicationContext.xml
 * <bean lazy-init="false" class="cn.longicorn.modules.spring.RuntimeContext" />
 */

public class RuntimeContext {

	public void setUserManager(IUserManager<? extends IUser> userManager) {
		RuntimeContext.userManager = userManager;
	}

	private static IUserManager<? extends IUser> userManager;

	public static IUser getCurrentUser() {
		String userName = SpringSecurityUtils.getCurrentUserName();
		return userName == null ? null : userManager.getUserByLoginName(userName);
	}

	public static String getCurrentUsername() {
		return SpringSecurityUtils.getCurrentUserName();
	}

	public static String getCurrentUserDisplayName() {
		IUser user = getCurrentUser();
		return user == null ? "" : user.getDisplayName();
	}

}