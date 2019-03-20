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
import android.widget.Toast;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarImageMenuActivity;
import com.pine.base.component.share.bean.ShareBean;
import com.pine.base.component.share.manager.ShareManager;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpWebViewContract;
import com.pine.mvp.presenter.MvpWebViewPresenter;
import com.pine.tool.util.WebViewUtils;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class MvpWebViewActivity extends BaseMvpActionBarImageMenuActivity<IMvpWebViewContract.Ui, MvpWebViewPresenter>
        implements IMvpWebViewContract.Ui, View.OnClickListener {
    // 分享dialog
    private AlertDialog mShareDialog;
    private WebView web_view;
    private TextView refresh_btn_tv;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_web_view;
    }

    @Override
    protected void findViewOnCreate() {
        refresh_btn_tv = findViewById(R.id.refresh_btn_tv);
    }

    @Override
    protected void init() {
        initEvent();
        initWebView();
    }

    private void initEvent() {
        refresh_btn_tv.setOnClickListener(this);
    }

    private void initWebView() {
        web_view = findViewById(R.id.web_view);
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
                loadUrl(mPresenter.getH5Url());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                           SslError error) {
                // [高危]WebView未校验HTTPS证书
                // 风险详情：APP 的 WebView 证书认证错误时，未取消加载页面，存在中间人攻击风险。
                // 修复建议使用： handler.cancel() 停止加载问题页面。
                handler.proceed();
//                handler.cancel();
            }
        });
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv) {
        titleTv.setText(R.string.mvp_web_view_title);
        menuBtnIv.setImageResource(R.mipmap.base_ic_share);

        mShareDialog = ShareManager.getInstance().createShareDialog(this, mPresenter.getShareBeanList());
        menuBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareManager.getInstance().onActivityResult(requestCode, resultCode, data, new ShareManager.ShareCallback() {
            @Override
            public void onShareSuccess(ShareBean shareBean) {
                Toast.makeText(MvpWebViewActivity.this,
                        R.string.mvp_share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShareCancel(ShareBean shareBean) {
                Toast.makeText(MvpWebViewActivity.this,
                        R.string.mvp_share_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShareFail(ShareBean shareBean) {
                Toast.makeText(MvpWebViewActivity.this,
                        R.string.mvp_share_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.refresh_btn_tv) {
            loadUrl(mPresenter.getH5Url());
        }
    }

    @Override
    public void loadUrl(String url) {
        web_view.loadUrl(url);
    }

    static class JsInterface {

    }
}
