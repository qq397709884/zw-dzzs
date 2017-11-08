package cn.longicorn.modules.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 反射的Utils函数集合.
 * 
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 * 
 * @author calvin
 * @author zhuchanglin
 */
public class ReflectionUtils {

	private static final String CGLIB_CLASS_SEPARATOR = "$$";
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 * 支持层次结构,如dept.name，但不支持延迟加载的POJO对象
	 */
	public static Object getFieldValue(final Object object, final String fieldName) {
		String curFieldName = fieldName;
		int pos = fieldName.indexOf('.');
		String destFieldName = "";
		if (pos > -1) {
			curFieldName = fieldName.substring(0, pos);
			destFieldName = fieldName.substring(pos + 1);
		}
		Field field = getDeclaredField(object, curFieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		if (pos > -1) {
			return ReflectionUtils.getFieldValue(result, destFieldName);
		} else {
			return result;
		}
	}

	/**
	 * 获取符合POJO规范的对象的属性值，经过getter函数
	 */
	public static Object getProperty(final Object object, final String fieldName) {
		try {
			return PropertyUtils.getProperty(object, fieldName);
		} catch (Exception e) {
			//该信息常见，仅在开发时提示开发人员，用户无需知道
			logger.debug("获取层次结构对象的属性值失败，{}", e.getMessage());
		}
		return "";
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法,无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		Assert.notNull(object, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		Assert.notNull(object, "object不能为空");

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */

	@SuppressWarnings({ "rawtypes" })
	public static Class getSuperClassGenricType(final Class clazz, final int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * 通过反射获得Bean的某属性声明的第一个泛型类型参数
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Class getPropertyArgumentsType(final Class<?> clazz, final String propertyName) {
		return getPropertyArgumentsType(clazz, propertyName, 0);
	}

	/**
	 * 通过反射获得Bean属性声明的泛型参数类型
	 * 例如，User中的roles属性定义为, Set<Role> roles，则通过getPropertyArgumentsType(User.class,"roles",0)
	 * 即可获得Role.class
	 * @param index		指定要获取从0开始的哪一个泛型参数
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Class getPropertyArgumentsType(final Class<?> clazz, final String propertyName, final int index) {
		try {
			Field field = clazz.getDeclaredField(propertyName);
			Type genericType = field.getGenericType();
			if (!(genericType instanceof ParameterizedType)) {
				logger.warn(clazz.getSimpleName() + "'s property {} is not ParameterizedType", propertyName);
				return Object.class;
			}

			Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();
			if (index >= params.length || index < 0) {
				logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
						+ "'s property {} Parameterized Type: {} ", propertyName, params.length);
				return Object.class;
			}
			if (!(params[index] instanceof Class)) {
				logger.warn(clazz.getSimpleName()
						+ "'s property {} not set the actual class on property type generic parameter", propertyName);
				return Object.class;
			}
			return (Class) params[index];
		} catch (SecurityException e) {
			logger.error("Security Error in getPropertyArgumentsType", e);
		} catch (NoSuchFieldException e) {
			logger.error("Filed {} not found in {}", propertyName, clazz.getSimpleName());
		}
		return Object.class;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成List.
	 * 
	 * @param collection 	来源集合.
	 * @param propertyName	 要提取的属性名.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List fetchElementPropertyToList(final Collection collection, final String propertyName) {
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw convertToUncheckedException(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数),组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	@SuppressWarnings("rawtypes")
	public static String fetchElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		List list = fetchElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value 待转换的字符串
	 * @param clazz 提供类型信息的Class
	 * @param propertyName 提供类型信息的Class的属性，支持嵌套属性，如dept.name
	 */
	public static Object convertValue(Object value, Class<?> clazz, String propertyName) {
		try {
			if (StringUtils.contains(propertyName, ".")) {
				String destPropertyName = StringUtils.substringAfter(propertyName, ".");
				String destObjectName = StringUtils.substringBefore(propertyName, ".");
				Class<?> objectType = BeanUtils.getPropertyDescriptor(clazz, destObjectName).getPropertyType();
				//如果是容器，则取出第一个泛型参数作为后续属性的宿主类，如roles的第一个泛型参数
				//可能是Role，且它只有一个泛型参数。
				if (Collection.class.isAssignableFrom(objectType)) {
					objectType = getPropertyArgumentsType(clazz, destObjectName, 0);
				}
				return convertValue(value, objectType, destPropertyName);
			}
			Class<?> toType = BeanUtils.getPropertyDescriptor(clazz, propertyName).getPropertyType();
			DateConverter dc = new DateConverter();
			dc.setUseLocaleFormat(true);
			dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			ConvertUtils.register(dc, Date.class);
			return ConvertUtils.convert(value, toType);

		} catch (Exception e) {
			throw convertToUncheckedException(e);
		}
	}

	/**
	 * 返回被CGLIB代理的类的实际类型，即开发人员原始创建的类型
	 */
	public static Class<?> getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		Class<?> clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static IllegalArgumentException convertToUncheckedException(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException)
			return new IllegalArgumentException("Refelction Exception.", e);
		else
			return new IllegalArgumentException(e);
	}

}