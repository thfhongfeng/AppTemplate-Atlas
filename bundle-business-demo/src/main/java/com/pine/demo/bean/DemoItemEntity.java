package com.pine.demo.bean;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class DemoItemEntity {

    /**
     * name : "新手引导"
     * class : DemoNoviceGuideActivity.class
     */

    private String name;
    private Class clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
