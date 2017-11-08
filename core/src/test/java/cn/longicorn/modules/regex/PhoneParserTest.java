package cn.longicorn.modules.regex;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import cn.longicorn.modules.regex.PhoneParser;
import cn.longicorn.modules.regex.PhoneParser.TeleProvider;
import cn.longicorn.modules.utils.StopWatch;

public class PhoneParserTest {

	@Test
	public void main() {
		testPhoneParser();
	}

	public void testPhoneParser() {
		PhoneParser pp = PhoneParser.getInstance();
		TeleProvider a = pp.parseMobile("13507182880");
		String[] x = new String[1000];
		for (int i = 0; i < 200; i++) {
			x[0 + i * 5] = "13507182880";
			x[1 + i * 5] = "12323";
			x[2 + i * 5] = "18007170890";
			x[3 + i * 5] = "13071789870";
			x[4 + i * 5] = "13968888888";
		}

		StopWatch sw = new StopWatch();
		Map<String, Integer> map = pp.parseMobiles(x);
		long elapsed = sw.getMillis();

		Set<String> key = map.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String s = it.next();
			System.out.println(s);
			System.out.println(map.get(s));
		}

		System.out.println(a.getDisplayName());
		System.out.println("1000个电话号码解析共耗时(毫秒)：" + elapsed);
	}

}