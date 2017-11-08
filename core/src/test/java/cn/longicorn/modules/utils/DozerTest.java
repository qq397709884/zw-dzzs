package cn.longicorn.modules.utils;

import static org.junit.Assert.*;
import java.text.ParseException;
import org.junit.Test;
import cn.longicorn.modules.mapper.BeanMapper;

public class DozerTest {

	@Test
	public void testRandomStringUtils() throws ParseException {
		User user = new User();
		UserDTO u = BeanMapper.map(user, UserDTO.class);

		assertEquals("Default", u.getName());
	}

}