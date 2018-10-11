package com.pine.base.share.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.pine.base.BuildConfig;
import com.pine.base.R;
import com.pine.base.share.bean.ShareBean;
import com.pine.base.util.DialogUtils;
import com.pine.tool.util.LogUtils;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/10/11
 */

public class ShareManager {
    private final static String TAG = LogUtils.makeLogTag(ShareManager.class);
    private static volatile ShareManager mInstance;
    private ShareBean mCurShareBean;

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        if (mInstance == null) {
            synchronized (ShareManager.class) {
                if (mInstance == null) {
                    mInstance = new ShareManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        initTencent(BuildConfig.QQ_FOR_APP_ID, BuildConfig.WX_FOR_APP_KEY, BuildConfig.WX_SECRET_KEY,
                R.mipmap.base_ic_launcher, BuildConfig.APPLICATION_ID, BuildConfig.BASE_URL);
        initSina(context, BuildConfig.WEI_BO_FOR_APP_KEY, BuildConfig.WEI_BO_REDIRECT_URL);
    }

    public void initTencent(String qq_for_app_id, String wx_for_app_key, String wx_secret_key,
                            int icon, String appName, String baseUrl) {
        TencentShareManager.getInstance().init(qq_for_app_id, wx_for_app_key, wx_secret_key,
                icon, appName, baseUrl);
    }

    public void initSina(Context context, String wei_bo_for_app_key, String wei_bo_redirect_url) {
        SinaShareManager.getInstance().init(context, wei_bo_for_app_key, wei_bo_redirect_url);
    }

    public AlertDialog createShareDialog(final Activity context, @NonNull final ArrayList<ShareBean> shareBeanList) {
        return DialogUtils.createShareDialog(context, shareBeanList);
    }

    public void share(Activity activity, ShareBean shareBean) {
        switch (shareBean.getShareTarget()) {
            case ShareBean.SHARE_TARGET_QQ:
                ShareManager.getInstance().shareToQQ(activity, shareBean);
                break;
            case ShareBean.SHARE_TARGET_WX:
                ShareManager.getInstance().shareToWX(activity, true, shareBean);
                break;
            case ShareBean.SHARE_TARGET_WX_FRIEND_CIRCLE:
                ShareManager.getInstance().shareToWX(activity, false, shareBean);
                break;
            case ShareBean.SHARE_TARGET_WEI_BO:
                ShareManager.getInstance().shareToWeiBo(activity, shareBean);
                break;
        }
    }

    /**
     * 分享到QQ
     *
     * @param context
     * @param shareBean
     */
    private void shareToQQ(Context context, ShareBean shareBean) {
        mCurShareBean = shareBean;
        TencentShareManager.getInstance().shareWebPageToQQ(context, shareBean.getShareTitle(),
                shareBean.getShareDescription(), shareBean.getShareUrl());
    }

    /**
     * 分享到微信朋友 or 朋友圈
     *
     * @param context
     * @param isTimeline true为朋友  false为朋友圈
     * @param shareBean
     */
    private void shareToWX(Context context, boolean isTimeline, ShareBean shareBean) {
        mCurShareBean = shareBean;
        TencentShareManager.getInstance().shareWebPageToWX(context, isTimeline,
                shareBean.getShareUrl(), shareBean.getShareTitle(), shareBean.getShareDescription());
    }

    /**
     * 分享到新浪微博
     *
     * @param activity
     * @param shareBean
     */
    private void shareToWeiBo(Activity activity, ShareBean shareBean) {
        mCurShareBean = shareBean;
        switch (shareBean.getShareContentType()) {
            case ShareBean.SHARE_CONTENT_TYPE_TEXT_URL:
                if (shareBean.getShareThumbId() != 0) {
                    SinaShareManager.getInstance().shareWebPageToWeiBo(activity,
                            shareBean.getShareTitle(), shareBean.getShareText(), shareBean.getShareDescription(),
                            shareBean.getShareThumbId(), shareBean.getShareUrl());
                } else {
                    SinaShareManager.getInstance().shareTextToWeiBo(activity,
                            shareBean.getShareTitle(), shareBean.getShareText(), shareBean.getShareUrl());
                }
                break;
            case ShareBean.SHARE_CONTENT_TYPE_IMAGE:
                if (shareBean.getShareUriList() != null && shareBean.getShareUriList().size() > 0) {
                    SinaShareManager.getInstance().shareImageToWeiBo(activity,
                            shareBean.getShareTitle(), shareBean.getShareDescription(),
                            shareBean.getShareUriList().get(0));
                }
                break;
            case ShareBean.SHARE_CONTENT_TYPE_MULTI_IMAGE:
                if (shareBean.getShareUriList() != null) {
                    SinaShareManager.getInstance().shareMultiImageToWeiBo(activity,
                            shareBean.getShareUriList());
                }
                break;
            case ShareBean.SHARE_CONTENT_TYPE_VIDEO:
                if (shareBean.getShareUriList() != null && shareBean.getShareUriList().size() > 0) {
                    SinaShareManager.getInstance().shareVideoToWeiBo(activity,
                            shareBean.getShareUriList().get(0));
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data,
                                 @NonNull final ShareCallback callback) {
        if (mCurShareBean == null) {
            return;
        }
        switch (mCurShareBean.getShareTarget()) {
            case ShareBean.SHARE_TARGET_QQ:
            case ShareBean.SHARE_TARGET_WX:
            case ShareBean.SHARE_TARGET_WX_FRIEND_CIRCLE:
                Tencent.handleResultData(data, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        callback.onShareSuccess(mCurShareBean);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        callback.onShareFail(mCurShareBean);
                    }

                    @Override
                    public void onCancel() {
                        callback.onShareCancel(mCurShareBean);
                    }
                });
                break;
            case ShareBean.SHARE_TARGET_WEI_BO:
                if (SinaShareManager.getInstance().getShareHandler() != null) {
                    SinaShareManager.getInstance().getShareHandler().doResultIntent(data, new WbShareCallback() {
                        @Override
                        public void onWbShareSuccess() {
                            callback.onShareSuccess(mCurShareBean);
                        }

                        @Override
                        public void onWbShareCancel() {
                            callback.onShareCancel(mCurShareBean);
                        }

                        @Override
                        public void onWbShareFail() {
                            callback.onShareFail(mCurShareBean);
                        }
                    });
                }
                break;
        }
    }

    public interface ShareCallback {
        void onShareSuccess(ShareBean shareBean);

        void onShareCancel(ShareBean shareBean);

        void onShareFail(ShareBean shareBean);
    }
}
