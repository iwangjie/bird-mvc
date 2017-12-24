package cn.birdmvc.controller;

import cn.birdmvc.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王杰
 * @since 2017/12/24
 */
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public Result index(String name,Result result){
        System.out.println("name"+name);
        return result.setView("home");
    }
}
