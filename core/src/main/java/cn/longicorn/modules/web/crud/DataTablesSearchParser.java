package cn.longicorn.modules.web.crud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.data.SearchFilter;

public class DataTablesSearchParser<T> implements ISearchParser<T> {

	private HttpServletRequest request;

	private String filterPrefix;

	public DataTablesSearchParser(HttpServletRequest request, String... filterPrefixs) {
		this.request = request;
		//默认作为查询条件的Filter属性名前缀为filter_
		this.filterPrefix = filterPrefixs.length > 0 ? filterPrefixs[0] : "filter_";
	}

	public List<SearchFilter> buildSearchFilters() {
		Map<String, Object> filterParamMap = readSearchCondition();
		return buildSearchFilters(filterParamMap);
	}

	/**
	 * 根据按PropertyFilter命名规则的Request参数,创建PropertyFilter列表.
	 * PropertyFilter命名规则为Filter属性前缀_比较类型_属性名.
	 * eg. filter_EQ_name filter_LIKE_name_OR_email
	 */
	public List<SearchFilter> buildSearchFilters(Map<String, Object> filterParamMap) {
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String searchName = entry.getKey();
			Object value = entry.getValue();
			//支持值为多个的模式，创建多个SearchFiler，多个Filter之间的关系目前不可指定（默认一般应为and）
			String[] conditions = isArray(value) ? (String[]) value : new String[] { (String) value };
			for (String condition : conditions) {
				if (StringUtils.isNotBlank(condition)) {
					SearchFilter filter = new SearchFilter(searchName, condition);
					filterList.add(filter);
				}
			}
		}
		return filterList;
	}

	private Map<String, Object> readSearchCondition() {
		return WebUtils.getParametersStartingWith(request, filterPrefix);
	}

	private int readAIntValue(String paramName, boolean fireExceptionWhenNotExpect) {
		String str = request.getParameter(paramName);
		if (StringUtils.isNumeric(str)) {
			return Integer.parseInt(str);
		} else if (fireExceptionWhenNotExpect) {
			throw new RuntimeException("Page error, can't get the DataTables component parameters.");
		}
		return 0;
	}

	private Map<String, String> buildSortMethod() {
		Map<String, String> sortMap = new HashMap<>();
		String[] cols = getAllDataTablesColumnNames();
		String[][] orders = getSortedColumns();

		for (String[] order : orders) {
			int colNum = Integer.parseInt(order[0]);
			if (!cols[colNum].equalsIgnoreCase("null") && !cols[colNum].equals("")) {
				if (Page.ASC.equalsIgnoreCase(order[1])) {
					sortMap.put(cols[colNum], Page.ASC);
				} else {
					sortMap.put(cols[colNum], Page.DESC);
				}
			}
		}
		return injectDefaultSortWhenNoSort(sortMap);
	}

	private Map<String, String> injectDefaultSortWhenNoSort(Map<String, String> sortMap) {
		if (sortMap.isEmpty()) {
			sortMap.put("id", Page.DESC);
		}
		return sortMap;
	}

	private String[] getAllDataTablesColumnNames() {
		int len = readAIntValue("iColumns", true);
		String[] cols = new String[len];
		for (int i = 0; i < len; i++) {
			cols[i] = request.getParameter("mDataProp_" + i);
		}
		return cols;
	}

	private String[][] getSortedColumns() {
		int len = this.readAIntValue("iSortingCols", true);
		String[][] orders = new String[len][2];
		for (int i = 0; i < len; i++) {
			orders[i][0] = request.getParameter("iSortCol_" + i);
			orders[i][1] = request.getParameter("sSortDir_" + i);
		}
		return orders;
	}

	private boolean isArray(Object object) {
		assert (object != null);
		Class<?> clazz = object.getClass();
		return clazz.isArray();
	}

	@Override
	public Page<T> parse() {
		Page<T> page = new Page<T>();
		int length = readAIntValue("iDisplayLength", false);
		page.setPageSize(length == 0 ? Page.DEFAULT_PAGE_SIZE : length);
		page.setCurrentPage(readAIntValue("iDisplayStart", false) / page.getPageSize() + 1);
		page.setSearchFilters(buildSearchFilters());
		page.setSortMap(buildSortMethod());
		return page;
	}

}
