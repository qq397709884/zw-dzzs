package cn.longicorn.modules.data.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.data.SearchFilter;

public class MongoSearchBuilder<T> {

	private Page<T> page;

	public MongoSearchBuilder(Page<T> page) {
		this.page = page;
	}

	public Map<String, String> getCondition() {
		Map<String, String> conditions = new HashMap<String, String>();
		List<SearchFilter> searchFilters = page.getSearchFilters();
		if (page.getSearchFilters() != null) {
			for (SearchFilter filter : searchFilters) {
				String matchType = filter.getMatchType().name();
				String propertyName = filter.getPropertyNames()[0];
				conditions.put(matchType + "_" + propertyName, filter.getValue());
			}
		}
		return conditions;
	}
	
	public DBObject getSort(List<String> sortFields) {
		DBObject sort = new BasicDBObject();
		Map<String, String> sortMaps = page.getSortMap();
		for (Entry<String, String> entry : sortMaps.entrySet()) {
			String sortValue = entry.getValue();
			if (sortFields.contains(entry.getKey()) && sortValue != null) {
				if (sortValue.toUpperCase().equals("ASC")) {
					sort.put(entry.getKey(), 1);
				} else {
					sort.put(entry.getKey(), -1);
				}
			}
		}
		return sort;
	}

	public String getSort() {
		StringBuilder sb = new StringBuilder(); 
		Map<String, String> sortMaps = page.getSortMap();
		for (Entry<String, String> entry : sortMaps.entrySet()) {
			String sortValue = entry.getValue();
			String col;
			if (sortValue.toUpperCase().equals("ASC")) {
				col = entry.getKey();
			} else {
				col = "-" + entry.getKey();
			}
			if (sb.length() > 0) {
				sb.append(",").append(col);
			} else {
				sb.append(col);
			}
		}
		return sb.toString();
	}

}