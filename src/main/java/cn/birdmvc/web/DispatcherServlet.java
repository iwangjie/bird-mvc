package cn.birdmvc.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求调度器
 * 通过请求地址分配响应控制器进行处理
 * @author 王杰
 * @since 2017年12月24日
 */
public class DispatcherServlet extends HttpServlet{


    /**
     * 初始化控制器
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {

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

    }
}
