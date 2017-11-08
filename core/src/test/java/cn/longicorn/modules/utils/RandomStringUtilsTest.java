package cn.longicorn.modules.utils;

import static org.junit.Assert.*;
import java.text.ParseException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomStringUtilsTest {
	@Test
	public void testRandomStringUtils() throws ParseException {
		final String s = RandomStringUtils.random(5, false, true);
		assertEquals("获得的随机字符串长度不对，应该是5个字符", 5, s.length());
		System.out.println("产生的随机字符串是：" + s);
	}
}
