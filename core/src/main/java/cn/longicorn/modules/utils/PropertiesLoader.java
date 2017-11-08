package cn.longicorn.modules.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Properties文件载入工具类. 可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，
 * 但以System的Property优先.
 * 注意：本类中避免使用log4j等日志系统，因为在执行该代码的时候，日志系统有可能尚未初始化，比如
 * 在WEB系统中，手工通过listener进行log4j初始化的情形。
 */
public class PropertiesLoader {

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * 取出Property
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		String value = systemProperty != null ? systemProperty : properties.getProperty(key);
		return value == null ? null : fixValue(value);
	}

	/**
	 * 将获得的值进行转换，以支持${}格式的配置，类似Apache Configuration的properties功能
	 * @param value	原始的值，可能包含一个或多个${}关键字
	 * @return 替换后的值
	 */
	private String fixValue(String value) {
		String regex = "\\$\\{[^}]*\\}";
		Pattern patternForTag = Pattern.compile(regex);

		Matcher matcher = patternForTag.matcher(value);
		boolean result = matcher.find();
		StringBuffer sb = new StringBuffer();
		while (result) {
			StringBuilder key = new StringBuilder(matcher.group(0));
			key.delete(0, 2);								//delete "${" at left
			key.delete(key.length() - 1, key.length());		//delete "}" at right
			String v = getValue(key.toString().trim());
			matcher.appendReplacement(sb, v);
			result = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 取出String类型的Property,如果都為Null则抛出异常.
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 取出String类型的Property.如果都為Null則返回Default值.
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Propert.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
	 * 载入多个文件, 文件路径使用Spring Resource格式.
	 */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				// Don't use log4j here, perhaps the log4j has not been initialized.
				// for example, the code cn.longicorn.modules.utils.Log4jWebConfiguration
				// use this class to load log4jRoot definition to set the log4j work path,
				// at that time, the log4j cann't work properly.
				System.out.println("Warnning: Loadding Properties error，" + ex.getLocalizedMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}

}
