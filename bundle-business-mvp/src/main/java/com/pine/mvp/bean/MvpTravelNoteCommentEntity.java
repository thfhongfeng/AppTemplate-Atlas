package com.pine.mvp.bean;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteCommentEntity {

    /**
     * id :
     * name :
     * imgUrl :
     * createTime :
     * content :
     */

    private String id;
    private String name;
    private String imgUrl;
    private String createTime;
    private String content;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
