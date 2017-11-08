package cn.longicorn.modules.data;

import org.apache.commons.lang3.StringUtils;

/**
 * 与具体ORM实现无关的属性过滤条件封装类.
 * PropertyFilter主要记录页面中简单的搜索过滤条件
 */
public class SearchFilter {

	/**
	 * 多个属性间OR关系的分隔符.
	 */
	public static final String OR_SEPARATOR = "_OR_";

	/**
	 * 属性比较类型.参照Criteria的对应查询方式,GTD, GED, LTD, LED 这四个是针对时间
	 */
	public enum MatchType {
		EQ, LIKE, GT, GE, LT, LE, ISNOTNULL, ISNULL, GTD, GED, LTD, LED
	}

	private String[] propertyNames;
	private String value;
	private MatchType matchType = MatchType.EQ;
	private String valueType;

	public SearchFilter() {
	}

	/**
	 * @param filterName 包含比较关系以及属性名的过滤字符串，例如LIKE_name_OR_title_OR_subject
	 * @param value	过滤值
	 */
	public SearchFilter(final String filterName, final String value) {
		//解析过滤串，获取匹配类型与属性名
		String matchTypeCode = StringUtils.substringBefore(filterName, "_");
		try {
			matchType = Enum.valueOf(MatchType.class, matchTypeCode);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("filter名称没有按照规则编写，无法得到属性比较类型", e);
		}
		String propertyNameStr = StringUtils.substringAfter(filterName, "_");
		propertyNames = StringUtils.split(propertyNameStr, SearchFilter.OR_SEPARATOR);

		this.value = value;
	}

	/**
	 * 是否有多个属性
	 */
	public boolean isMultiProperty() {
		return (propertyNames.length > 1);
	}

	public String getValue() {
		return value;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return 当属性只有一个时，返回它，否则抛出异常
	 */
	public String getPropertyName() {
		if (propertyNames.length > 1) {
			throw new IllegalStateException("There are not only one property.");
		}
		return propertyNames[0];
	}

}
