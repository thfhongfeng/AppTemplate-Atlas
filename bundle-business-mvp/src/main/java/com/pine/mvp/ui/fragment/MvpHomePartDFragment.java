package com.pine.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.base.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.R;

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
    protected void initData() {

    }

    @Override
    protected void initView(View layout) {
        refresh_btn_tv = (TextView) layout.findViewById(R.id.refresh_btn_tv);

        initWebView(layout);
        initEvent();
    }

    private void initEvent() {
        refresh_btn_tv.setOnClickListener(this);
    }

    private void initWebView(View view) {
        web_view = (WebView) view.findViewById(R.id.web_view);
        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowContentAccess(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setDomStorageEnabled(true); // 必须设置为true，否则杀死APP后，再次加载时，onGeolocationPermissionsShowPrompt不会调用(除非清除APP缓存)。从而授权无法完成，导致页面加载出问题。
        web_view.setWebChromeClient(new InjectedChromeClient("appInterface", JsInterface.class) {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        web_view.setWebViewClient(new WebViewClient() {

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
        web_view.removeJavascriptInterface("searchBoxJavaBredge_");
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
