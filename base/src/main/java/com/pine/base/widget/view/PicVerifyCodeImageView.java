package com.pine.base.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.pine.base.component.image_loader.IImageDownloadListener;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.tool.util.LogUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class PicVerifyCodeImageView extends AppCompatImageView {
    private Context mContext;
    private String mUrl;
    private volatile List<String> mCookies;

    public PicVerifyCodeImageView(Context context) {
        super(context);
        mContext = context;
    }

    public PicVerifyCodeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PicVerifyCodeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(String url) {
        mUrl = url;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });
        onResume();
    }

    public void onResume() {
        this.setScaleType(ScaleType.FIT_XY);
        String url = mUrl + "?" + Math.random() * 100000;
        ImageLoaderManager.getInstance().downloadListener(new IImageDownloadListener() {
            @Override
            public void onRequest(URL url, Map<String, String> requestHeaders) {
                LogUtils.d("PicVerifyCodeIv", "onRequest url :" + url);
            }

            @Override
            public void onResponse(int statusCode, Map<String, List<String>> responseHeaders) {
                mCookies = responseHeaders.get("set-cookie");
                LogUtils.d("PicVerifyCodeIv", "onResponse mCookies :" + mCookies);
            }

            @Override
            public void onFail(int statusCode, String s) {
                LogUtils.d("PicVerifyCodeIv", "onFail statusCode :" + statusCode);
            }
        }).loadImage(mContext, url, this);
    }

    public HashMap<String, String> getCookies() {
        if (mCookies == null) {
            return new HashMap<>();
        }
        HashMap<String, String> cookiesMap = new HashMap<>();
        for (int i = 0; i < mCookies.size(); i++) {
            try {
                cookiesMap.put("Set-Cookie" + i, mCookies.get(i));
            } catch (Exception e) {
                cookiesMap.put("Set-Cookie", mCookies.get(i));
            }
        }
        return cookiesMap;
    }
}
