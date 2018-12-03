package com.pine.base.component.image_loader;

/**
 * Created by tanghongfeng on 2018/11/21
 */

public enum ImageCacheStrategy {
    NONE,  // 不缓存文件
    DATA, // 只缓存原图（默认的缓存策略）
    RESOURCE, // 只缓存结果图
    ALL, // 远程图片同时缓存原图和结果图, 本地度图片缓存结果图
    AUTOMATIC // 根据数据来源智能选择策略
}
