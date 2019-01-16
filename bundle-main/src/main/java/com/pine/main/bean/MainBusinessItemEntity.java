package com.pine.main.bean;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainBusinessItemEntity {

    /**
     * name : Business
     * bundle : business_bundle
     * command : goBusinessAHomeActivity
     */

    private String name;
    private String bundle;
    private String command;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
