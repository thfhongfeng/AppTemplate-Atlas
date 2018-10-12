package com.pine.welcome.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.exception.MessageException;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterBundleKey;
import com.pine.router.RouterBundleSwitcher;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.R;
import com.pine.welcome.bean.BundleSwitcherEntity;
import com.pine.welcome.bean.VersionEntity;
import com.pine.welcome.contract.ILoadingContract;
import com.pine.welcome.manager.ApkVersionManager;
import com.pine.welcome.model.BundleSwitcherModel;
import com.pine.welcome.model.VersionModel;
import com.pine.welcome.ui.activity.WelcomeActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class LoadingPresenter extends BasePresenter<ILoadingContract.Ui> implements ILoadingContract.Presenter {
    private final static long LOADING_MAX_TIME = 2000;
    private BundleSwitcherModel mBundleSwitcherModel;
    private VersionModel mVersionModel;
    private long mStartTimeMillis;

    public LoadingPresenter() {
        mBundleSwitcherModel = new BundleSwitcherModel();
        mVersionModel = new VersionModel();
    }

    @Override
    public void setStartTime() {
        mStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void loadBundleSwitcherData() {
        mBundleSwitcherModel.requestBundleSwitcherData(new IModelAsyncResponse<ArrayList<BundleSwitcherEntity>>() {
            @Override
            public void onResponse(ArrayList<BundleSwitcherEntity> bundleSwitcherEntities) {
                if (bundleSwitcherEntities == null) {
                    return;
                }
                for (int i = 0; i < bundleSwitcherEntities.size(); i++) {
                    RouterBundleSwitcher.setBundleSwitchState(bundleSwitcherEntities.get(i).getBundleKey(),
                            bundleSwitcherEntities.get(i).isOpen());
                }
                checkVersion();
                return;
            }

            @Override
            public boolean onFail(Exception e) {
                if (isUiAlive()) {
                    checkVersion();
                }
                return false;
            }
        });
    }

    @Override
    public void updateVersion() {
        ApkVersionManager.getInstance().startUpdate(new ApkVersionManager.UpdateListener() {

            @Override
            public void onDownloadStart(boolean isResume, long rangeSize, long allCount) {
                LogUtils.d(TAG, "onDownloadStart isResume:" + isResume + ", rangeSize:" + rangeSize + ", allCount:" + allCount);
                if (isUiAlive()) {
                    getUi().showVersionUpdateProgressDialog();
                }
            }

            @Override
            public void onDownloadProgress(int progress, long fileCount, long speed) {
                if (isUiAlive()) {
                    getUi().updateVersionUpdateProgressDialog(progress);
                }
            }

            @Override
            public void onDownloadComplete(String filePath) {
                LogUtils.d(TAG, "onDownloadComplete filePath:" + filePath);
                if (isUiAlive()) {
                    getUi().dismissVersionUpdateProgressDialog();
                    installNewVersionApk();
                }
            }

            @Override
            public void onDownloadCancel() {
                LogUtils.d(TAG, "onDownloadCancel");
                if (isUiAlive()) {
                    getUi().showVersionUpdateToast(getContext().getString(R.string.version_update_cancel));
                    getUi().dismissVersionUpdateProgressDialog();
                    autoLogin();
                }
            }

            @Override
            public void onDownloadError(Exception exception) {
                LogUtils.d(TAG, "onDownloadError onDownloadError:" + exception);
                if (isUiAlive()) {
                    String msg = "";
                    if (exception instanceof MessageException) {
                        msg = getContext().getString(R.string.version_update_fail) +
                                "(" + exception.getMessage() + ")";
                    } else {
                        msg = getContext().getString(R.string.version_update_fail);
                    }
                    getUi().showVersionUpdateToast(msg);
                    getUi().dismissVersionUpdateProgressDialog();
                    autoLogin();
                }
            }
        });
    }

    @Override
    public void autoLogin() {
        if (!RouterBundleSwitcher.isBundleOpen(RouterBundleKey.LOGIN_BUNDLE_KEY)) {
            if (isUiAlive()) {
                goWelcomeActivity();
            }
        }
        RouterFactory.getLoginBundleManager().callOpCommand(BaseApplication.mCurResumedActivity, RouterCommand.LOGIN_autoLogin,
                null, new IRouterCallback() {
                    @Override
                    public void onSuccess(Bundle returnBundle) {
                        if (isUiAlive()) {
                            goWelcomeActivity();
                        }
                    }

                    @Override
                    public void onException(Bundle returnBundle) {
                        if (isUiAlive()) {
                            goWelcomeActivity();
                        }
                    }

                    @Override
                    public void onFail(String errorInfo) {
                        if (isUiAlive()) {
                            goWelcomeActivity();
                        }
                    }
                });
    }

    private void goWelcomeActivity() {
        long delay = LOADING_MAX_TIME - (System.currentTimeMillis() - mStartTimeMillis);
        delay = delay > 0 ? delay : 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUiAlive()) {
                    Intent intent = new Intent(getContext(), WelcomeActivity.class);
                    getContext().startActivity(intent);
                    finishUi();
                }
                return;
            }
        }, delay);
    }

    private void checkVersion() {
        mVersionModel.requestUpdateVersionData(new IModelAsyncResponse<VersionEntity>() {
            @Override
            public void onResponse(VersionEntity versionEntity) {
                if (isUiAlive() && versionEntity != null) {
                    ApkVersionManager.getInstance().setVersionEntity(versionEntity);
                    try {
                        PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                        if (packageInfo.versionCode < versionEntity.getVersionCode()) {
                            if (versionEntity.isForce()) {
                                getUi().showVersionUpdateProgressDialog();
                                updateVersion();
                            } else {
                                getUi().showVersionUpdateConfirmDialog(versionEntity.getVersionName());
                            }
                        } else {
                            autoLogin();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        autoLogin();
                    }
                }
            }

            @Override
            public boolean onFail(Exception e) {
                if (isUiAlive()) {
                    autoLogin();
                }
                return false;
            }
        });
    }

    private void installNewVersionApk() {
        if (!isUiAlive()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = ApkVersionManager.getInstance().getDownLoadFile();
        if (file != null && file.exists()) {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } else {
            getUi().showVersionUpdateToast(getContext().getString(R.string.version_update_fail));
            autoLogin();
        }
        finishUi();
    }

    @Override
    public void onUiState(int state) {

    }
}
