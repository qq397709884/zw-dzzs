package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;

public interface ISessionManager {

	/**
	 * 判断Session ID是否合法
	 * @param sid	session id
	 */
	boolean isValid(final String sid);
	
	/**
	 * 删除Session
	 * @param sid	需要删除的session的sid
	 * @return boolean 成功返回true，否则false
	 */
	boolean remove(final String sid);
	
	/**
	 * 加载sid对应的User对象的JSON描述格式
	 * 如果不存在，返回null
	 */
	Session loadSession(final String sid);
	
	/**
	 * @param user	保存或更新session
	 * @return 返回sid
	 */
	String saveOrUpdateSession(final IUser user);
	
}
