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
    private WebViewUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

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
        // [中危]WebView同源策略绕过
        // 风险详情：APP 的 WebView 加载本地资源文件并启用 JavaScript 时，存在信息泄漏风险。
        // 修复建议：避免同时使用 File 协议与 JavaScript。
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // [高危]WebView应用克隆风险
            // 风险详情：APP使用WebView访问网络，当开启了允许JS脚本访问本地文件，一旦访问恶意网址，存在被窃取APP数据并复制APP的运行环境，造成“应用克隆”的后果，可能造成严重的经济损失。
            // 修复建议：建议禁用setAllowFileAccessFromFileURLs和setAllowUniversalAccessFromFileURLs；若需要允许JS访问本地文件，则应使用白名单等策略进行严格的访问控制。
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
