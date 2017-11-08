package cn.longicorn.modules.security.rest;

import java.util.Date;

import cn.longicorn.modules.spring.security.IUser;
import cn.longicorn.modules.utils.Identities;

public class Session implements java.io.Serializable {

	private static final long serialVersionUID = 6381347343062752902L;
	private String sid;
	private String uid;
	private String loginName;
	private String displayName;
	private String password;
	private Date since = new Date();

	public Session() {
		this(null);
	}

	public Session(IUser user) {
		this.sid = Identities.uuid2();
		if (user != null) {
			this.uid = user.getId();
			this.loginName = user.getLoginName();
			this.displayName = user.getDisplayName();
			this.password = user.getPassword();
		}
	}

	public String getSid() {
		return sid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

}
