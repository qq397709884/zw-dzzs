package cn.longicorn.modules.spring.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Spring security UserDetailService implementation
 * <p>After username/password verfied successfully，load user's detailed information from database,
 * such as user's status, user's authorities.
 * 
 * @author zhuchanglin
 */
public class UserDetailServiceImpl implements UserDetailsService {

	private IUserManager<?> userManager;

	/**
	 * Auto binding role name, default is ROLE_USER
	 */
	private String autoBindRoleName = "ROLE_USER";

	/**
	 * Need binding a inside role to any user. Turn off this feature by setting the 
	 * <code>autoBindRole</code> as false.  
	 */
	private boolean autoBindRole = true;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		IUser user = userManager.getUserByLoginName(userName);
		if (user == null) {
			throw new UsernameNotFoundException(userName + " not found");
		}
		Set<GrantedAuthority> arrayAuths = new HashSet<GrantedAuthority>();
		Collection<String> authNames = user.getAuthorities();
		for (String authName : authNames) {
			arrayAuths.add(new SimpleGrantedAuthority(authName));
		}
		GrantedAuthority authority = new SimpleGrantedAuthority(autoBindRoleName);
		if (autoBindRole && !arrayAuths.contains(authority)) {
			arrayAuths.add(authority);
		}
		String password = user.getPassword() == null ? "" : user.getPassword();
		return new User(user.getLoginName(), password, user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(), arrayAuths);
	}

	@Autowired
	public void setUserManager(IUserManager<? extends IUser> userManager) {
		this.userManager = userManager;
	}

	public void setAutoBindRoleName(String autoBindRoleName) {
		this.autoBindRoleName = autoBindRoleName;
	}

	public void setAutoBindRole(boolean autoBindRole) {
		this.autoBindRole = autoBindRole;
	}

}
