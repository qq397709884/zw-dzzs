package cn.longicorn.modules.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 
 * @param <T> Page中记录的类型.
 */
public class Page<T> {

	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final int DEFAULT_PAGE_SIZE = 20;

	protected long currentPage = 1;
	protected long pageSize;
	protected List<T> result = Collections.emptyList();
	protected long totalCount = -1;

	//查询条件
	private List<SearchFilter> searchFilters;

	private Map<String, String> sortMap;

	public Page() {
	}

	public Page(final int pageSize) {
		setPageSize(pageSize);
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public long getCurrentPage() {
		return currentPage;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
		if (currentPage < 1) {
			this.currentPage = 1;
		}
	}

	public Page<T> currentPage(final long thePageNo) {
		setCurrentPage(thePageNo);
		return this;
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 */
	public long getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为1.
	 */
	public void setPageSize(final long pageSize) {
		this.pageSize = pageSize;
		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	* 根据currentPage和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	*/
	public long getFirst() {
		return ((currentPage - 1) * pageSize);
	}

	/**
	 * 取得页内的记录列表.
	 */
	public List<T> getResult() {
		return result;
	}

	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 取得总记录数,默认值为-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数,默认值为-1.
	 */
	public long getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}
		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean hasNext() {
		return (currentPage + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号,序号从1开始.
	 */
	public long getNextPage() {
		if (hasNext()) {
			return currentPage + 1;
		}
		else {
			return currentPage;
		}
	}

	/**
	 * 是否还有上一页. 
	 */
	public boolean hasPre() {
		return (currentPage - 1 >= 1);
	}

	/**
	 * 取得上页的页号,序号从1开始.
	 */
	public long getPrePage() {
		if (hasPre()) {
			return currentPage - 1;
		}
		else {
			return currentPage;
		}
	}

	public void addSearchFilter(SearchFilter filter) {
		if (searchFilters == null) {
			searchFilters = new ArrayList<SearchFilter>();
		}
		searchFilters.add(filter);
	}

	public void addSort(String field, String type) {
		if (sortMap == null) {
			sortMap = new HashMap<String, String>();
		}
		sortMap.put(field, type);
	}

	public List<SearchFilter> getSearchFilters() {
		return searchFilters;
	}

	public void setSearchFilters(List<SearchFilter> searchFilters) {
		this.searchFilters = searchFilters;
	}

	public Map<String, String> getSortMap() {
		return sortMap;
	}

	public void setSortMap(Map<String, String> sortMap) {
		this.sortMap = sortMap;
	}
	
}
