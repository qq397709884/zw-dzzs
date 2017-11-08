package cn.longicorn.modules.sp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.longicorn.modules.sp.SpAccount;
import cn.longicorn.modules.sp.SpCode;
import cn.longicorn.modules.sp.SpCodeRouter;

public class SpCodeRouterTest {

	@Test
	public void test() {
		SpCodeRouter router = new SpCodeRouter();
		List<SpAccount> accounts = new ArrayList<>();
		accounts.add(new SpAccount("famschool", "50203", false, 5));
		accounts.add(new SpAccount("onecard", "501", true, 2));
		router.rebuild(accounts);

		assertTrue(router.isExsit(new SpCode("50203", 0)));
		assertTrue(router.isExsit(new SpCode("50203", 1)));
		assertTrue(router.isExsit(new SpCode("50203", 2)));
		assertTrue(router.isExsit(new SpCode("50203", 3)));
		assertTrue(router.isExsit(new SpCode("50203", 4)));
		assertTrue(router.isExsit(new SpCode("50203", 5)));
		assertFalse(router.isExsit(new SpCode("50203", 6)));
		assertTrue(router.isExsit(new SpCode("501", 2)));
		assertFalse(router.isExsit(new SpCode("501", 0)));
		assertFalse(router.isExsit(new SpCode("501", 1)));
		assertFalse(router.isExsit(new SpCode("501", 3)));
		
		assertEquals("onecard", router.getAccount(new SpCode("501", 2)));
		assertEquals("famschool", router.getAccount(new SpCode("50203", 0)));
	}

}
