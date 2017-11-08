package cn.longicorn.modules.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cn.longicorn.modules.data.EnhanceNamingStrategy;

public class EhanceNamingStrategyTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		EnhanceNamingStrategy ens = new EnhanceNamingStrategy();
		assertEquals("first_name", ens.PropertyToColumn("firstName"));
		assertEquals("firstName", ens.columnToProperty("first_name"));
	}

}
