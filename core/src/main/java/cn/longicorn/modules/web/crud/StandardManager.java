package cn.longicorn.modules.web.crud;

import cn.longicorn.modules.data.Page;

public abstract class StandardManager<T, K> {

	public abstract Page<T> searchPage(Page<T> page);

	public abstract void delete(K id);

	//~~~~~~~~~~~ Optional method ~~~~~~~~~~~

	public abstract void create(T t);

	public abstract void update(T t);

	public abstract T get(K id);


}
