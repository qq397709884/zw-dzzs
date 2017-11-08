package cn.longicorn.modules.spring.security;

/**
 * 支持SpringSecurity的几种缺省用户状态，该常量原本定义于具体实现中，
 * 考虑到在不同的项目中实现会有变化，因此将此常量上移到core中
 * @author zhuchanglin
 */
public final class SSUserStatus {
	
	public static final int STATUS_ENABLED = 1; 			// 已激活
	public static final int STATUS_EXPIRED = -1; 			// 账号过期
	public static final int STATUS_LOCKED = -2; 			// 未激活
	public static final int STATUS_PASSWORD_EXPIRED = -3; 	// 密码过期
	
}
