package cn.longicorn.modules.spring.security;

public interface IUserManager<T extends IUser> {

	public T getUserByLoginName(String userName);

}
