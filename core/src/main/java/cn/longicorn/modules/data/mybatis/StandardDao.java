package cn.longicorn.modules.data.mybatis;

import java.util.List;
import java.util.Map;
/**
 * 标准的基础Dao接口声明
 * @param <T>	管理的实体类
 */
public interface StandardDao<T, K> {

	public T get(K id);

	public void save(T entity);

	public void update(T entity);

	public void delete(K id);

	public List<T> searchPage(Map<String, Object> map);

	public List<T> search();

}