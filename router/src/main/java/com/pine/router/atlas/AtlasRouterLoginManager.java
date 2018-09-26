package com.pine.router.atlas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.taobao.atlas.remote.IRemoteTransactor;
import android.taobao.atlas.remote.RemoteFactory;
import android.taobao.atlas.remote.transactor.RemoteTransactor;

import com.pine.router.IRouterCallback;
import com.pine.router.IRouterManager;
import com.pine.router.R;
import com.pine.router.RouterBundleKey;
import com.pine.router.RouterBundleSwitcher;
import com.pine.router.RouterConstants;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterLoginManager implements IRouterManager {
    private static final String TAG = LogUtils.makeLogTag(AtlasRouterLoginManager.class);
    private static volatile AtlasRouterLoginManager mInstance;

    private AtlasRouterLoginManager() {
    }

    public static AtlasRouterLoginManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterLoginManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterLoginManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void callUiCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback) {
        if (!RouterBundleSwitcher.isBundleOpen(RouterBundleKey.LOGIN_BUNDLE_KEY)) {
            LogUtils.releaseLog(TAG, RouterBundleKey.LOGIN_BUNDLE_KEY + " is not opened");
            if (callback != null) {
                callback.onFail(activity.getString(R.string.router_bundle_not_open));
            }
            return;
        }
        RemoteFactory.requestRemote(RemoteTransactor.class, activity,
                new Intent("atlas.transaction.intent.action.login.LoginBundleRemoteAction"),
                new RemoteFactory.OnRemoteStateListener<RemoteTransactor>() {

                    @Override
                    public void onRemotePrepared(RemoteTransactor remote) {
                        remote.call(commandName, args, new IRemoteTransactor.IResponse() {
                            @Override
                            public void OnResponse(Bundle bundle) {
                                if (callback != null) {
                                    switch (bundle.getString(RouterConstants.REMOTE_CALL_STATE_KEY)) {
                                        case RouterConstants.ON_SUCCEED:
                                            callback.onSuccess(bundle);
                                            break;
                                        default:
                                            callback.onException(bundle);
                                            break;
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(String errorInfo) {
                        LogUtils.releaseLog(TAG, "request " + RouterBundleKey.LOGIN_BUNDLE_KEY + " onFailed");
                        if (callback != null) {
                            callback.onFail(errorInfo);
                        }
                    }
                });
    }

    @Override
    public void callDataCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback) {
        if (!RouterBundleSwitcher.isBundleOpen(RouterBundleKey.LOGIN_BUNDLE_KEY)) {
            LogUtils.releaseLog(TAG, RouterBundleKey.LOGIN_BUNDLE_KEY + " is not opened");
            if (callback != null) {
                callback.onFail(activity.getString(R.string.router_bundle_not_open));
            }
            return;
        }
        if (!RouterBundleSwitcher.isBundleOpen(RouterBundleKey.LOGIN_BUNDLE_KEY)) {
            callback.onFail(activity.getString(R.string.router_bundle_not_open));
            return;
        }
        RemoteFactory.requestRemote(RemoteTransactor.class, activity,
                new Intent("atlas.transaction.intent.action.login.LoginBundleRemoteAction"),
                new RemoteFactory.OnRemoteStateListener<RemoteTransactor>() {

                    @Override
                    public void onRemotePrepared(RemoteTransactor remote) {
                        remote.call(commandName, args, new IRemoteTransactor.IResponse() {
                            @Override
                            public void OnResponse(Bundle bundle) {
                                if (callback != null) {
                                    switch (bundle.getString(RouterConstants.REMOTE_CALL_STATE_KEY)) {
                                        case RouterConstants.ON_SUCCEED:
                                            callback.onSuccess(bundle);
                                            break;
                                        default:
                                            callback.onException(bundle);
                                            break;
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(String errorInfo) {
                        LogUtils.releaseLog(TAG, "request " + RouterBundleKey.LOGIN_BUNDLE_KEY + " onFailed");
                        if (callback != null) {
                            callback.onFail(errorInfo);
                        }
                    }
                });
    }

    @Override
    public void callOpCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback) {
        if (!RouterBundleSwitcher.isBundleOpen(RouterBundleKey.LOGIN_BUNDLE_KEY)) {
            LogUtils.releaseLog(TAG, RouterBundleKey.LOGIN_BUNDLE_KEY + " is not opened");
            if (callback != null) {
                callback.onFail(activity.getString(R.string.router_bundle_not_open));
            }
            return;
        }
        RemoteFactory.requestRemote(RemoteTransactor.class, activity,
                new Intent("atlas.transaction.intent.action.login.LoginBundleRemoteAction"),
                new RemoteFactory.OnRemoteStateListener<RemoteTransactor>() {

                    @Override
                    public void onRemotePrepared(RemoteTransactor remote) {
                        remote.call(commandName, args, new IRemoteTransactor.IResponse() {
                            @Override
                            public void OnResponse(Bundle bundle) {
                                if (callback != null) {
                                    switch (bundle.getString(RouterConstants.REMOTE_CALL_STATE_KEY)) {
                                        case RouterConstants.ON_SUCCEED:
                                            callback.onSuccess(bundle);
                                            break;
                                        default:
                                            callback.onException(bundle);
                                            break;
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(String errorInfo) {
                        LogUtils.releaseLog(TAG, "request " + RouterBundleKey.LOGIN_BUNDLE_KEY + " onFailed");
                        if (callback != null) {
                            callback.onFail(errorInfo);
                        }
                    }
                });
    }
}
