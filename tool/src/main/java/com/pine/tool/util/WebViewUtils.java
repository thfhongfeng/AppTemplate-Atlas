package com.pine.tool.util;

import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tanghongfeng on 2018/10/10
 */

public class WebViewUtils {
    public static void setupCommonWebView(WebView webView, DownloadListener downloadListener,
                                          WebChromeClient webChromeClient, WebViewClient webViewClient) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setAllowContentAccess(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (downloadListener != null) {
            webView.setDownloadListener(downloadListener);
        }
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        if (webChromeClient != null) {
            // 必须设置为true，否则杀死APP后，再次加载时，onGeolocationPermissionsShowPrompt不会调用(除非清除APP缓存)。从而授权无法完成，导致页面加载出问题。
            webSettings.setDomStorageEnabled(true);
            webView.setWebChromeClient(webChromeClient);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (webViewClient != null) {
            webView.setWebViewClient(webViewClient);
        }
        webView.removeJavascriptInterface("searchBoxJavaBredge_");
    }
}
