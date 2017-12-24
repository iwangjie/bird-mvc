package cn.birdmvc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问路径与控制器对象的映射关系持有者
 * @author 王杰
 * @since 2017/12/24
 */
public class MappingHolder {

    private static final Logger log = LoggerFactory.getLogger(MappingHolder.class);

    //要扫描的包
    private static String basePakage;

    //扫描文件的基础路径
    private static String baseFilePath;

    //扫描文件后缀
    private static String FILE_SUFFFIX = "Controller.class";

    //控制器集合
    private static List<String> controllerClassNameList;

    //控制器存储类
    private static Map<String, Object> mapping;


    /**
     * 获取mapping
     * @param basePakage
     * @return
     */
    public static Map<String, Object> getMapping(String basePakage){
        MappingHolder.basePakage = basePakage != null?basePakage:"";
        if(null == mapping)initMapping();
        return mapping;
    }



    /**
     * 初始化Mapping
     */
    private static void initMapping() {

        log.info("init mapping start");
        log.info("Controller BasePckage:" + basePakage);

        basePakage = basePakage == null ? "" : basePakage;
        mapping = new HashMap<String, Object>();
        controllerClassNameList = new ArrayList<String>();

        String basePath = basePakage.replaceAll("\\.", "/");

        try {
            URI uri = Thread.currentThread().getContextClassLoader().getResource(basePath).toURI();
            File file = new File(uri);
            baseFilePath = file.getPath();
            initControllerClassNameList(file);

            //循环增加Mapping
            for (String className :
                    controllerClassNameList) {
                Class<?> clazz = Class.forName(className);
                String requestPath = "/"+className.substring(className.lastIndexOf(FILE_SUFFFIX.substring(0,FILE_SUFFFIX.lastIndexOf(".")))).toLowerCase();
                mapping.put(requestPath,clazz.newInstance());
                log.info("====>mapping add "+requestPath+"to "+className);
            }

            log.info("init mapping end");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化ControllerClassName集合
     *
     * @param file
     */
    private static void initControllerClassNameList(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile :
                    files) {
                initControllerClassNameList(childFile);
            }
        } else {
            String path = file.getPath();
            if (path.endsWith(FILE_SUFFFIX)) {
                path = path.substring(baseFilePath.length(), path.lastIndexOf("."));
                path = path.replaceAll("\\\\", ".");
                String className = basePakage+path;
                if (className.startsWith(".")) path = className.substring(1);
                controllerClassNameList.add(className);
            }
        }
    }
}
