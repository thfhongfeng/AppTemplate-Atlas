package com.pine.mvp.bean;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopAndProductEntity extends MvpShopEntity {

    /**
     * name : Shop Item 1
     * distance :
     * imgUrl :
     * products : [{"name":"Product Item 1"},{"name":"Product Item 1"}]
     */

    private List<ProductsBean> products;

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * name : Product Item 1
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
