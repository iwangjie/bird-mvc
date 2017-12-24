package cn.birdmvc.web;

import cn.birdmvc.core.HandlerMethodArgsBuilder;
import cn.birdmvc.core.MappingHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private static final String DEFAULT_CONTROLLER_NAME = "index";
    private static final String DEFAULT_NOTFOUNDVIEW = "404";



    private String viewPrefix;
    private String viewSuffix;
    private String notFoundView;
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
        String notFoundView = getInitParameter("notFoundView");

        this.viewPrefix = viewPrefix!=null?viewPrefix:DEFAULT_VIEWPREFIX;
        this.viewSuffix = viewSuffix!=null?viewSuffix:DEFAULT_VIEWSUFFFIX;
        this.notFoundView = notFoundView!=null?notFoundView:DEFAULT_NOTFOUNDVIEW;
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
        String requestURI = req.getRequestURI();
        //项目部署路径
        String contextPath = req.getServletContext().getContextPath();
        String path = requestURI.substring(contextPath.length()).toLowerCase();
        String[] names = path.substring(1).split("/");
        Object controller = this.mapping.get("/"+names[0]);
        String methodName = names.length>1?names[1]:DEFAULT_CONTROLLER_NAME;
        Method method = gethandlerMethod(controller, methodName);
        if(null != method){
            doDispatch(req,resp,controller,method);
        }else{
            log.info("找不到对应的路径处理器  -->"+requestURI);
            render(req,resp,notFoundView);
        }
    }


    /**
     * 将请求分配给对应的处理器的方法执行
     * @param req
     * @param resp
     * @param controller
     * @param method
     */
    protected void doDispatch(HttpServletRequest req,HttpServletResponse resp,Object controller,Method method){
        try {
            Object[] args = new HandlerMethodArgsBuilder(req, resp, method, controller).build();
            Object result = method.invoke(controller, args);
            render(req,resp,result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据处理结果进行响应
     * @param req
     * @param resp
     * @param object
     */
    protected void render(HttpServletRequest req,HttpServletResponse resp,Object object){

        if(object instanceof String){
            String viewPath = viewPrefix+(String) object+viewSuffix;
            try {
                req.getRequestDispatcher(viewPath).forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(object instanceof Result){
            Result rs = (Result) object;
            String viewPath = viewPrefix+rs.getView()+viewSuffix;
            Map<String, Object> model = rs.getModel();
            modelAddAll(req,model);
            try {
                req.getRequestDispatcher(viewPath).forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加所有参数到Request
     * @param req
     * @param model
     */
    private void modelAddAll(HttpServletRequest req, Map<String, Object> model) {
        for (Map.Entry<String,Object> entry:
             model.entrySet()) {
            req.setAttribute(entry.getKey(),entry.getValue());
        }
    }


    /**
     * 获取对应路径处理方法
     * @param controller
     * @param name
     * @return
     */
    private Method gethandlerMethod(Object controller, String name) {
        if(controller!=null){
            Method[] handlerMethods = controller.getClass().getDeclaredMethods();
            for (Method handlerMethod : handlerMethods) {
                String methodName = handlerMethod.getName();
                if (name.equals(methodName)) {
                    return handlerMethod;
                }
            }
        }
        return null;
    }

}
