package com.pine.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopItemEntity implements Parcelable {

    /**
     * id :
     * name :
     * distance :
     * mainImgUrl :
     */

    private String id;
    private String name;
    private String distance;
    private String mainImgUrl;

    protected MvpShopItemEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        distance = in.readString();
        mainImgUrl = in.readString();
    }

    public static final Creator<MvpShopItemEntity> CREATOR = new Creator<MvpShopItemEntity>() {
        @Override
        public MvpShopItemEntity createFromParcel(Parcel in) {
            return new MvpShopItemEntity(in);
        }

        @Override
        public MvpShopItemEntity[] newArray(int size) {
            return new MvpShopItemEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(distance);
        dest.writeString(mainImgUrl);
    }

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
}
