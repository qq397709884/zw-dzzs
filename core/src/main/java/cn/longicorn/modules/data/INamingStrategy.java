package cn.longicorn.modules.data;

/**
 * 数据库范畴名子和类范畴名字之间的转换
 */
public interface INamingStrategy {
	
	public String columnToProperty(String columnName);

	public String PropertyToColumn(String propertyName);
	
}
