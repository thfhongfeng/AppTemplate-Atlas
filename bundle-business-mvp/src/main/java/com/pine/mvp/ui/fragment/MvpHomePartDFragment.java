package com.pine.mvp.ui.fragment;

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
import android.widget.TextView;

import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.base.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.R;
import com.pine.tool.util.WebViewUtils;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartDFragment extends BaseMvpFragment implements View.OnClickListener {
    private WebView web_view;
    private TextView refresh_btn_tv;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.mvp_fragment_home_part_d;
    }

    @Override
    protected void onCreateViewInitData() {

    }

    @Override
    protected void onCreateViewInitView(View layout) {
        refresh_btn_tv = (TextView) layout.findViewById(R.id.refresh_btn_tv);
        initWebView(layout);
        initEvent();
    }

    private void initEvent() {
        refresh_btn_tv.setOnClickListener(this);
    }

    private void initWebView(View view) {
        web_view = (WebView) view.findViewById(R.id.web_view);
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
        web_view.loadUrl(MvpUrlConstants.H5_HomePartD);
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
