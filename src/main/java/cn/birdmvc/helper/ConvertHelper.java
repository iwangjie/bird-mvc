package cn.birdmvc.helper;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 转换助手
 * @author 王杰
 */
public class ConvertHelper extends Helper{

	private ConvertHelper() {}
	
	public static Object convert(Object value, Class<?> targetType){
		return ConvertUtils.convert(value, targetType);
	}
	
	public static Object mapConvertToBean(Map<String, Object> map,
                                          Class<?> beanClass) {
		Object bean = null;
		try {
			bean = beanClass.newInstance();
			for (String name : map.keySet()) {
				BeanUtils.setProperty(bean, name, map.get(name));
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
