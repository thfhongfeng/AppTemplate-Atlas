package com.pine.base.component.share.bean;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.pine.base.R;
import com.pine.tool.util.AppUtils;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/10/11
 */

public class ShareBean {
    public static final int SHARE_TARGET_QQ = 0;
    public static final int SHARE_TARGET_WX = 1;
    public static final int SHARE_TARGET_WX_FRIEND_CIRCLE = 2;
    public static final int SHARE_TARGET_WEI_BO = 3;

    public static final int SHARE_CONTENT_TYPE_TEXT_URL = 1;
    public static final int SHARE_CONTENT_TYPE_IMAGE = 2;
    public static final int SHARE_CONTENT_TYPE_MULTI_IMAGE = 3;
    public static final int SHARE_CONTENT_TYPE_VIDEO = 4;

    private static final int[] DEFAULT_ICON_IDS = {
            R.mipmap.base_ic_share_qq,
            R.mipmap.base_ic_share_weixin,
            R.mipmap.base_ic_share_weixin_friend_circle,
            R.mipmap.base_ic_share_weibo
    };

    private static final String[] DEFAULT_ICON_NAMES = AppUtils
            .getApplication()
            .getResources().getStringArray(R.array.base_share_icon_name);

    private int shareTarget;
    private int shareContentType;
    private String iconName;
    private int iconId;
    private String shareTitle;
    private String shareText;
    private String shareDescription;
    private String shareUrl;
    private int shareThumbId;
    private ArrayList<Uri> shareUriList;

    public ShareBean(@NonNull int shareTarget, @NonNull int shareContentType) {
        this(shareTarget, shareContentType,
                DEFAULT_ICON_NAMES[shareTarget % DEFAULT_ICON_NAMES.length],
                DEFAULT_ICON_IDS[shareTarget % DEFAULT_ICON_IDS.length],
                "", "", "", "", null);
    }

    public ShareBean(@NonNull int shareTarget, @NonNull int shareContentType, @NonNull ArrayList<Uri> shareUriList) {
        this(shareTarget, shareContentType,
                DEFAULT_ICON_NAMES[shareTarget % DEFAULT_ICON_NAMES.length],
                DEFAULT_ICON_IDS[shareTarget % DEFAULT_ICON_IDS.length],
                "", "", "", "", shareUriList);
    }

    public ShareBean(@NonNull int shareTarget, @NonNull int shareContentType, @NonNull String shareUrl) {
        this(shareTarget, shareContentType,
                DEFAULT_ICON_NAMES[shareTarget % DEFAULT_ICON_NAMES.length],
                DEFAULT_ICON_IDS[shareTarget % DEFAULT_ICON_IDS.length],
                "", "", "", shareUrl, null);
    }

    public ShareBean(@NonNull int shareTarget, @NonNull int shareContentType, String shareTitle,
                     String shareText, String shareDescription, @NonNull String shareUrl) {
        this(shareTarget, shareContentType,
                DEFAULT_ICON_NAMES[shareTarget % DEFAULT_ICON_NAMES.length],
                DEFAULT_ICON_IDS[shareTarget % DEFAULT_ICON_IDS.length],
                shareTitle, shareText, shareDescription, shareUrl, null);
    }

    private ShareBean(@NonNull int shareTarget, @NonNull int shareContentType,
                      @NonNull String iconName, @NonNull int iconId,
                      String shareTitle, String shareText, String shareDescription,
                      String shareUrl, ArrayList<Uri> shareUriList) {
        this.shareTarget = shareTarget;
        this.shareContentType = shareContentType;
        this.iconName = iconName;
        this.iconId = iconId;
        this.shareTitle = shareTitle;
        this.shareText = shareText;
        this.shareDescription = shareDescription;
        this.shareUrl = shareUrl;
        this.shareUriList = shareUriList;
    }

    public int getShareTarget() {
        return shareTarget;
    }

    public void setShareTarget(int shareTarget) {
        this.shareTarget = shareTarget;
    }

    public int getShareContentType() {
        return shareContentType;
    }

    public void setShareContentType(int shareContentType) {
        this.shareContentType = shareContentType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getShareThumbId() {
        return shareThumbId;
    }

    public void setShareThumbId(int shareThumbId) {
        this.shareThumbId = shareThumbId;
    }

    public ArrayList<Uri> getShareUriList() {
        return shareUriList;
    }

    public void setShareUriList(ArrayList<Uri> shareUriList) {
        this.shareUriList = shareUriList;
    }
}
