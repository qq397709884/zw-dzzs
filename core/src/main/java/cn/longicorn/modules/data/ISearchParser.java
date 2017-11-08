package cn.longicorn.modules.data;

/**
 * Web请求中查询条件解析器接口
 * @author zhuchanglin
 */
public interface ISearchParser<T> {

	/**
	 * @return Page	包含页面参数，如查询条件，
	 */
	public Page<T> parse();

}
