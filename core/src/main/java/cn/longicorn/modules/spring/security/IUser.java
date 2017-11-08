package cn.longicorn.modules.spring.security;

import java.util.Collection;

public interface IUser {

	/** return all binding roles with the user  */
	public Collection<String> getAuthorities();
	
	public String getId();

	public String getPassword();

	public String getLoginName();

	public String getDisplayName();

	public boolean isEnabled();

	public boolean isAccountNonExpired();

	public boolean isCredentialsNonExpired();

	public boolean isAccountNonLocked();

}
