package com.pine.base.component.image_selector.bean;

import java.io.Serializable;

public class ImageItemBean implements Serializable {
    public String path;

    public boolean selected;

    public ImageItemBean(String path) {
        this.path = path;
    }
}
