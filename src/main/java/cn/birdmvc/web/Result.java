package cn.birdmvc.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用响应对象
 * @author 王杰
 * @since 2017/12/24
 */
public class Result {

    private String view;
    private Map<String,Object> model = new HashMap<String,Object>();


    public Result add(String name,Object obj){
        model.put(name,obj);
        return this;
    }

    public Result setView(String view) {
        this.view = view;
        return this;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public static Result newInstance(){
        return new Result();
    }



}
