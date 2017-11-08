package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;

public interface IAuthenticationManager {

	IUser login(String username, String password);

}
