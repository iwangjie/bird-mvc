package cn.birdmvc.helper;

import org.apache.commons.beanutils.Converter;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.beanutils.ConvertUtils.register;

/**
 * 由于org.apache.commons.beanutils.BeanUtils.setProperty 只支持基本类型的转换
 * @author 王杰
 * @since 2017年12月24日
 */
public abstract class Helper {
	static {
		register(new Converter() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T convert(Class<T> type, Object value) {
				Date date = null;
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				try {
					date = dateFormat.parse((String) value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return (T) date;
			}
		}, Data.class);

	}
}
