package com.fy.customview.utils;

import java.util.HashMap;

/**
 * 选择工具，用于协助进行一些条件复杂的选择
 */
public class OptionUtil {

    private HashMap<Object,Object> map = new HashMap<>();

    public void addCondition(Object condition,Object result){
        this.map.get(condition);
    }

    public Object option(Object condition){
        return map.get(condition);
    }

}
