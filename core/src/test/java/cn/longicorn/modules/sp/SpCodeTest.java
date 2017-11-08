package cn.longicorn.modules.sp;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import cn.longicorn.modules.sp.SpCode;
import static org.junit.Assert.*;

public class SpCodeTest {

	@Test
	public void testSpCode() {
		Map<SpCode, String> table =  new TreeMap<>();
		
		SpCode s5 = new SpCode("5003", 5);
		SpCode s4 = new SpCode("5003", 4);
		SpCode s3 = new SpCode("5003", 3);
		SpCode s2 = new SpCode("5003", 2);
		SpCode s1 = new SpCode("5003", 1);
		SpCode s0 = new SpCode("5003", 0);
		
		table.put(s1, "user1");
		table.put(s2, "user2");
		table.put(s3, "user1");
		table.put(s4, "user1");
		table.put(s5, "user1");
		table.put(s0, "user0");
		
		String result = table.get(new SpCode("5003888", 0));
		assertEquals("user1", result);
		
		result = table.get(new SpCode("5003", 0));
		assertEquals("user0", result);
		
		result = table.get(new SpCode("500399", 0));
		assertEquals("user2", result);
	}
	
	@Test
	public void testSpCode2() {
		Map<SpCode, String> table =  new TreeMap<SpCode, String>();
		/*冲突的帐号分配*/
		SpCode s1 = new SpCode("500312345", 0);
		SpCode s2 = new SpCode("5", 8);
		SpCode s0 = new SpCode("5003", 5);
		
		table.put(s2, "user2");
		table.put(s0, "user0");
		table.put(s1, "user1");
		
		assertEquals(1, table.size());
	}

}
