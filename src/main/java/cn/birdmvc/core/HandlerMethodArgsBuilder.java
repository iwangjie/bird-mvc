package cn.birdmvc.core;

import cn.birdmvc.helper.ConvertHelper;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射控制器注入参数，JDK1.7不支持反射获取参数名，使用javasist
 *
 * @author 王杰
 * @since 2017/12/24
 */
public class HandlerMethodArgsBuilder {


    private Map<String, Object> requestParamInfo;//请求参数集合
    private Map<Class<?>, String> methodParamInfo;//方法参数集合
    private List<Object> args;
    private static final Logger log = LoggerFactory.getLogger(HandlerMethodArgsBuilder.class);

    public HandlerMethodArgsBuilder(HttpServletRequest req, HttpServletResponse resp, Method method, Object controller) {
        //初始化参数集合
        initRequestParamInfo(req);
        //初始化方法参数集合
        initMethodParamInfo(controller, method.getName());
    }

    public Object[] build() {
        inject();
        return args.toArray();

    }

    private void inject() {
        args = new ArrayList<Object>();
        Set<Class<?>> paramTypeClasses = methodParamInfo.keySet();
        try {
            for (Class<?> clazz : paramTypeClasses) {
                String key = (String) methodParamInfo.get(clazz);
                Object object;
                if (requestParamInfo.containsKey(key)) {
                    Object value = requestParamInfo.get(key);
                    if ((value instanceof Map)) {//如果为Map型的转换为Bean
                        @SuppressWarnings("unchecked")
                        Map<String, Object> map = (Map<String, Object>) value;
                        object  = ConvertHelper.mapConvertToBean(map, clazz);
                    }else{
                        object = ConvertHelper.convert(value, clazz);
                    }
                }else{
                    object = clazz.newInstance();
                }
                args.add(object);
            }
        } catch (Exception e) {
            log.error("Handler method args inject fail!", e);
        }
    }

    /**
     * 初始化方法参数map集合
     *
     * @param controller
     * @param methodName
     */
    private void initMethodParamInfo(Object controller, String methodName) {
        methodParamInfo = new HashMap<Class<?>, String>();
        Class<?> clazz = controller.getClass();
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.insertClassPath(new ClassClassPath(clazz));
            CtClass ctClass = classPool.get(clazz.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute("LocalVariableTable");
            CtClass[] parameterTypes = ctMethod.getParameterTypes();

            //是否静态
            int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> aClass = Class.forName(parameterTypes[i].getName());
                String variableName = attr.variableName(i + pos);
                methodParamInfo.put(aClass, variableName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化request参数map集合
     *
     * @param req
     */
    private void initRequestParamInfo(HttpServletRequest req) {
        requestParamInfo = new HashMap<String, Object>();
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String,String[]> entry:parameterMap.entrySet()) {
            requestParamInfo.put(entry.getKey(),entry.getValue()[0]);
        }

    }


}
