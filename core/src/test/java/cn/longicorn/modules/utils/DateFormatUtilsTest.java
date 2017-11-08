package cn.longicorn.modules.utils;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class DateFormatUtilsTest {
	
	@Test
	public void testDateFormatUtils() throws ParseException {
		final String s = "2012-10-10 19:10:01";
		final String patern = "yyyy-MM-dd HH:mm:ss";
		final Date date = DateUtils.parseDate(s, patern);
		// TimeZone参数传null表示使用操作系统设定的时区
		final String s2 = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss", (TimeZone) null);
		assertEquals(s, s2);
	}

}
