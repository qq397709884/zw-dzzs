package cn.longicorn.modules.web.crud;

/**
 * 便于支持页面中的批量操作功能，因为Id为泛型，在spring webmvc注入的时候只好用string，
 * 此处将根据具体的类型参数，将string转换为实际类型
 */
public class IdTypeConerter {

	public static Object fromString(Class<?> clazz, String val) {
		try {
			if (clazz == Long.class) {
				return Long.parseLong(val);
			} else if (clazz == Integer.class) {
				return Integer.parseInt(val);
			} else if (clazz == String.class) {
				return val;
			}
		} catch (Exception e) {
		     // Nothing to do
		}
		return null;
	}
}
