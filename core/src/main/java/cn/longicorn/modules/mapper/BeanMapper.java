package cn.longicorn.modules.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;
import cn.longicorn.modules.utils.Systems;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 *  
 * 1. 持有Mapper的单例. 
 * 2. 返回值类型转换.
 * 3. 批量转换Collection中的所有对象.
 * 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 */
public class BeanMapper {

	private static DozerBeanMapper dozer;

	static {
		final String defaultDozerMappingFile = "dozer_config.xml";
		if (Systems.locateFromClasspath(defaultDozerMappingFile) != null) {
			List<String> dozerMappingFiles = new ArrayList<String>();
			dozerMappingFiles.add(defaultDozerMappingFile);
			dozer = new DozerBeanMapper(dozerMappingFiles);
		} else {
			dozer = new DozerBeanMapper();
		}
	}


	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
		List<T> destinationList = new ArrayList<T>();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}

}