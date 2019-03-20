package com.pine.mvp.bean;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopAndProductEntity {

    /**
     * id :
     * name : Shop Item 1
     * distance :
     * mainImgUrl :
     * products : [{"name":"Product Item 1"},{"name":"Product Item 1"}]
     */

    private String id;
    private String name;
    private String distance;
    private String mainImgUrl;

    private List<ProductsBean> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * id :
         * name : Product Item 1
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
