package cn.longicorn.modules.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * 辅助获取当前用户的各种信息
 * 
 * @author zhuchanglin
 */
public class SpringSecurityUtils {

	public static Authentication getAuthentication() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		return ctx != null ? ctx.getAuthentication() : null;
	}

	public static UserDetails getUserDetails() {
		Authentication auth = getAuthentication();
		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				return (UserDetails) principal;
			}
		}
		return null;
	}

	/**
	 * 获取当前用户密码
	 */
	public static String getPassword() {
		UserDetails userDetails = getUserDetails();
		return userDetails != null ? userDetails.getPassword() : null;
	}

	/**
	 * 获取当前用户session id
	 */
	public static String getSessionID() {
		Authentication auth = getAuthentication();
		if (auth != null) {
			Object details = auth.getDetails();
			if (details instanceof WebAuthenticationDetails) {
				return ((WebAuthenticationDetails) details).getSessionId();
			}
		}
		return null;
	}
	
	/**
	 * 获取当前用户的用户名
	 */
	public static String getCurrentUserName() {
		UserDetails userDetails = getUserDetails();
		return userDetails != null ? userDetails.getUsername() : null;
	}
	
	public static List<String> getRoles() {
		List<String> roles = new ArrayList<String>();
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> c = currentUser.getAuthorities();
        for (GrantedAuthority authority : c) {
        	 String role = authority.getAuthority();
        	 if (role != null) roles.add(role);
        }
		return roles;
	}

}
