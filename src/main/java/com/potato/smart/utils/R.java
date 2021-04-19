package com.potato.smart.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Map<String, Object> meta = new HashMap<String, Object>();

    @ApiModelProperty(value = "返回数据")
    private List<Object> message =  new ArrayList<Object>();


    private R(){}


    public static R ok(){
        R r = new R();
        r.meta.put("msg","获取成功");
        r.meta.put("status",200);
        return r;
    }

    public static R error(){
        R r = new R();
        r.meta.put("msg","获取失败");
        r.meta.put("status",201);
        return r;
    }


    public R message(List message){
        this.message.addAll(message);
        return this;
    }

    public R message(Object obj){
        this.message.add(obj);
        return this;
    }
}
