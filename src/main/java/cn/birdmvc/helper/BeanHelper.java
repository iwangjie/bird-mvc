package cn.birdmvc.helper;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;


/**
 * Bean操作助手
 * @author 王杰
 * @since 2017年12月24日
 */
public class BeanHelper extends Helper{
	private BeanHelper(){}
	
	public static void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		 BeanUtils.setProperty(bean, name, value);
	  }
}
