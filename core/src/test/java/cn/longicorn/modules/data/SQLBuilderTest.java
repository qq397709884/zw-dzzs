package cn.longicorn.modules.data;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;
import cn.longicorn.modules.data.SQLBuilder;
import cn.longicorn.modules.data.jdbc.Mysql5Dialect;

public class SQLBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetOrderConditions() {
		Map<String, String> sortMaps = new TreeMap<String, String>();
		sortMaps.put("0name", "desc");
		sortMaps.put("1id", "asc");
		sortMaps.put("2type", "desc");
		SQLBuilder builder = new SQLBuilder(sortMaps);
		assertEquals("order by 0name desc,1id asc,2type desc", builder.getSortConditions());
	}

	
	@Test
	public void testBuild() {
		Map<String, String> sortMaps = new TreeMap<String, String>();
		sortMaps.put("salary", "desc");
		SQLBuilder builder = new SQLBuilder(sortMaps);
		builder.setDialect(new Mysql5Dialect());
		assertEquals("order by salary desc", builder.build());
	}
	
}
