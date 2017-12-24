package cn.birdmvc.web;

import cn.birdmvc.core.MappingHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 请求调度器
 * 通过请求地址分配响应控制器进行处理
 * @author 王杰
 * @since 2017年12月24日
 */
public class DispatcherServlet extends HttpServlet{

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_VIEWPREFIX = "/WEB-INF/views/";
    private static final String DEFAULT_VIEWSUFFFIX = ".jsp";


    private String viewPrefix;
    private String viewSuffix;
    private Map<String, Object> mapping;
    /**
     * 初始化控制器
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        log.info("DispatcherServlet Init...");
        //获取初始化参数
        String basePackage = getInitParameter("basePackage");
        String viewPrefix = getInitParameter("viewPrefix")  ;
        String viewSuffix = getInitParameter("viewSuffix");

        this.viewPrefix = viewPrefix!=null?viewPrefix:DEFAULT_VIEWPREFIX;
        this.viewSuffix = viewSuffix!=null?viewSuffix:DEFAULT_VIEWSUFFFIX;
        this.mapping = MappingHolder.getMapping(basePackage);
        log.info("DispatcherServlet Init end");
    }

    /**
     * 处理访问逻辑
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求地址
    }
}
