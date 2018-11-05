package com.pine.base.component.share.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.pine.tool.util.LogUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class SinaShareManager {
    private final static String TAG = LogUtils.makeLogTag(SinaShareManager.class);
    private static volatile SinaShareManager mInstance;
    private WbShareHandler mShareHandler;
    private boolean mIsInit;
    private WbAuthListener mListener = new WbAuthListener() {

        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {

        }

        @Override
        public void cancel() {

        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {

        }
    };

    private SinaShareManager() {
    }

    protected static SinaShareManager getInstance() {
        if (mInstance == null) {
            synchronized (SinaShareManager.class) {
                if (mInstance == null) {
                    mInstance = new SinaShareManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, String wei_bo_for_app_key, String wei_bo_redirect_url) {
        String scope = "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write";
        WbSdk.install(context, new AuthInfo(context, wei_bo_for_app_key, wei_bo_redirect_url, scope));
        mIsInit = true;
    }

    private boolean isInit() {
        return mIsInit;
    }

    public boolean shareTextToWeiBo(Activity activity, String title, String text, String url) {
        if (!isInit()) {
            LogUtils.d(TAG, "SinaShareManager was not init");
            return false;
        }
        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        WeiboMultiMessage weiBoMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = text;
        textObject.title = title;
        textObject.actionUrl = url;
        weiBoMessage.textObject = textObject;
        mShareHandler.shareMessage(weiBoMessage, false);
        return true;
    }

    public boolean shareWebPageToWeiBo(Activity activity, String title, String text, String description,
                                       int resId, String url) {
        if (!isInit()) {
            LogUtils.d(TAG, "SinaShareManager was not init");
            return false;
        }
        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        WeiboMultiMessage weiBoMessage = new WeiboMultiMessage();
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), resId);
        // 设置 Bitmap 类型的图片到视频对象里
        // 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        mediaObject.defaultText = text;
        weiBoMessage.mediaObject = mediaObject;
        mShareHandler.shareMessage(weiBoMessage, false);
        return true;
    }

    public boolean shareImageToWeiBo(Activity activity, String title, String description, Uri uri) {
        if (!isInit()) {
            LogUtils.d(TAG, "SinaShareManager was not init");
            return false;
        }
        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        WeiboMultiMessage weiBoMessage = new WeiboMultiMessage();
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageObject.setImageObject(bitmap);
        weiBoMessage.imageObject = imageObject;
        mShareHandler.shareMessage(weiBoMessage, false);
        return true;
    }

    public boolean shareMultiImageToWeiBo(Activity activity, ArrayList<Uri> uriList) {
        if (!isInit()) {
            LogUtils.d(TAG, "SinaShareManager was not init");
            return false;
        }
        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        WeiboMultiMessage weiBoMessage = new WeiboMultiMessage();
        MultiImageObject multiImageObject = new MultiImageObject();
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        multiImageObject.setImageList(uriList);
        weiBoMessage.multiImageObject = multiImageObject;
        mShareHandler.shareMessage(weiBoMessage, false);
        return true;
    }

    public boolean shareVideoToWeiBo(Activity activity, Uri uri) {
        if (!isInit()) {
            LogUtils.d(TAG, "SinaShareManager was not init");
            return false;
        }
        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        WeiboMultiMessage weiBoMessage = new WeiboMultiMessage();
        //获取视频
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = uri;
        weiBoMessage.mediaObject = videoSourceObject;
        mShareHandler.shareMessage(weiBoMessage, false);
        return true;
    }

    public WbShareHandler getShareHandler() {
        return mShareHandler;
    }
}
