package cn.longicorn.modules.web.crud;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import cn.longicorn.modules.data.SearchFilter;
import cn.longicorn.modules.web.crud.DataTablesSearchParser;

public class WebSearchParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildSearchFilters() {
		DataTablesSearchParser<Object> parser = new DataTablesSearchParser<Object>(null, "Filter");
		Map<String, Object> filterParamMap = new HashMap<String, Object>();
		filterParamMap.put("LIKE_name_OR_email", new String[] { "aa", "bb" });

		List<SearchFilter> searchFilters = parser.buildSearchFilters(filterParamMap);
		assertEquals("应该获得2个查询条件", 2, searchFilters.size());

		int index = 0;
		for (SearchFilter filter : searchFilters) {
			assertTrue("应该是多属性查询条件", filter.isMultiProperty());
			assertEquals(SearchFilter.MatchType.LIKE, filter.getMatchType());
			assertEquals(2, filter.getPropertyNames().length);
			assertEquals("name", (filter.getPropertyNames())[0]);
			assertEquals("email", (filter.getPropertyNames())[1]);
			if (index++ == 0)
				assertEquals("aa", filter.getValue());
			else
				assertEquals("bb", filter.getValue());
		}
	}

}
