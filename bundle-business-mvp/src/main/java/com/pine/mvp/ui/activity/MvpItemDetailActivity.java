package com.pine.mvp.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.mvp.ui.activity.BaseMvpActionBarMenuActivity;
import com.pine.base.util.DialogUtils;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpItemDetailContract;
import com.pine.mvp.presenter.MvpItemDetailPresenter;
import com.pine.tool.util.WebViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class MvpItemDetailActivity extends BaseMvpActionBarMenuActivity<IMvpItemDetailContract.Ui, MvpItemDetailPresenter>
        implements IMvpItemDetailContract.Ui, View.OnClickListener {
    // 分享dialog
    private AlertDialog mShareDialog;
    private WebView web_view;
    private TextView refresh_btn_tv;

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv) {
        titleTv.setText(R.string.mvp_item_detail_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnIv.setImageResource(R.mipmap.base_ic_share);
        List<String> titleList = new ArrayList<String>();
        titleList.add("Item Detail Title");
        titleList.add("Item Detail Title");
        titleList.add("Item Detail Title");
        List<String> descList = new ArrayList<String>();
        descList.add("Item Detail Desc");
        descList.add("Item Detail Desc");
        descList.add("Item Detail Desc");
        List<String> shareUrlList = new ArrayList<String>();
        shareUrlList.add("http://www.baidu.com");
        shareUrlList.add("http://www.baidu.com");
        shareUrlList.add("http://www.baidu.com");
        mShareDialog = DialogUtils.createShareDialog(this, titleList, descList, shareUrlList);
        menuBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog.show();
            }
        });
    }

    @Override
    protected MvpItemDetailPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_item_detail;
    }

    @Override
    protected boolean onCreateInitData() {
        return false;
    }

    @Override
    protected void onCreateInitView() {
        refresh_btn_tv = (TextView) findViewById(R.id.refresh_btn_tv);
        initWebView();
        initEvent();
    }

    private void initEvent() {
        refresh_btn_tv.setOnClickListener(this);
    }

    private void initWebView() {
        web_view = (WebView) findViewById(R.id.web_view);
        WebViewUtils.setupCommonWebView(web_view, new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }, new InjectedChromeClient("appInterface", JsInterface.class) {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        }, new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                loadUrl();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                           SslError error) {
                handler.proceed();
            }
        });
        loadUrl();
    }

    private void loadUrl() {
        web_view.loadUrl(MvpUrlConstants.H5_HomeItemDetail);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.refresh_btn_tv) {
            loadUrl();
        }
    }

    static class JsInterface {

    }
}
