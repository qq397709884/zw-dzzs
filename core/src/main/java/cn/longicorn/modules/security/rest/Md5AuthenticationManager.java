package cn.longicorn.modules.security.rest;

import cn.longicorn.modules.spring.security.IUser;
import cn.longicorn.modules.spring.security.IUserManager;
import org.apache.commons.lang3.StringUtils;

public class Md5AuthenticationManager extends DefaultAuthenticationManager {

    private IUserManager<? extends IUser> userManager;

    @Override
    public IUser login(String username, String password) {
        IUser user = userManager.getUserByLoginName(username);
        if (user == null)
            return null;
        if (StringUtils.equals(user.getPassword(), StringUtils.lowerCase(password))) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void setUserManager(IUserManager<? extends IUser> userManager) {
        this.userManager = userManager;
    }
}
