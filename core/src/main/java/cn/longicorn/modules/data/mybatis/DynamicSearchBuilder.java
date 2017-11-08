package cn.longicorn.modules.data.mybatis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.longicorn.modules.data.EnhanceNamingStrategy;
import cn.longicorn.modules.data.INamingStrategy;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.data.SearchFilter;

/**
 * 将查询条件和排序条件从SearchFilter、Sort Map转换为相应的Mybatis可用的查询参数
 * @param <T>		需要查询的类型
 */
public class DynamicSearchBuilder<T> {

	private final static String SORT_CONDITION_KEY = "sortStr";

	private List<SearchFilter> searchFilters;

	private Map<String, String> sortMap;

	private Map<String, Object> resultMap;

	private INamingStrategy namingStrategy;
	
	private Page<T> page;

	public DynamicSearchBuilder(Page<T> page) {
		this.page = page;
		this.searchFilters = page.getSearchFilters();
		this.sortMap = page.getSortMap();
		this.namingStrategy = new EnhanceNamingStrategy();
		this.resultMap = new TreeMap<String, Object>();
	}

	private void buildDynamicSearch() {
		if (searchFilters == null) {
			return;
		}
		for (SearchFilter filter : searchFilters) {
			resultMap.putAll(convertAFilter(filter));
		}
	}

	private Map<String, Object> convertAFilter(SearchFilter filter) {
		Map<String, Object> result = new TreeMap<String, Object>();
		String matchType = filter.getMatchType().name();
		String[] propertyNames = filter.getPropertyNames();
		// MatchType.name() + propertyName must be unique.
		for (String propertyName : propertyNames) {
			result.put(matchType + "_" + propertyName, filter.getValue());
		}
		return result;
	}

	public void setNamingStrategy(INamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	private void buildSortConditions() {
		if (sortMap == null) {
			return;
		}

		StringBuilder conditions = new StringBuilder();
		for (Entry<String, String> entry : sortMap.entrySet()) {
			conditions.append(",").append(namingStrategy.PropertyToColumn(entry.getKey()));
			conditions.append(" ").append(entry.getValue());
		}
		String sort = conditions.deleteCharAt(0).length() > 0 ? conditions.insert(0, "order by ").toString() : "";
		resultMap.put(SORT_CONDITION_KEY, sort);
	}

	public Map<String, Object> build() {
		buildDynamicSearch();
		buildSortConditions();
		resultMap.put("page", page);
		return resultMap;
	}
	
}
