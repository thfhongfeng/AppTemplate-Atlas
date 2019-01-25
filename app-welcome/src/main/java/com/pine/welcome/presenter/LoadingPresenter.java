package com.pine.welcome.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.exception.MessageException;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.widget.dialog.ProgressDialog;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterBundleKey;
import com.pine.router.RouterBundleSwitcher;
import com.pine.router.command.RouterCommand;
import com.pine.router.manager.RouterManager;
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
    public boolean parseIntentData() {
        mStartTimeMillis = System.currentTimeMillis();
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public void loadBundleSwitcherData() {
        mBundleSwitcherModel.requestBundleSwitcherData(new IModelAsyncResponse<ArrayList<BundleSwitcherEntity>>() {
            @Override
            public void onResponse(ArrayList<BundleSwitcherEntity> bundleSwitcherEntities) {
                if (bundleSwitcherEntities != null) {
                    for (int i = 0; i < bundleSwitcherEntities.size(); i++) {
                        RouterBundleSwitcher.setBundleSwitchState(bundleSwitcherEntities.get(i).getBundleKey(),
                                bundleSwitcherEntities.get(i).isOpen());
                    }
                }
                if (isUiAlive()) {
                    checkVersion();
                }
                return;
            }

            @Override
            public boolean onFail(Exception e) {
                if (isUiAlive()) {
                    checkVersion();
                }
                return true;
            }
        });
    }

    @Override
    public void updateVersion(final boolean isForce) {
        ApkVersionManager.getInstance().startUpdate(new ApkVersionManager.UpdateListener() {

            @Override
            public void onDownloadStart(boolean isResume, long rangeSize, long allCount) {
                LogUtils.d(TAG, "onDownloadStart isResume:" + isResume +
                        ", rangeSize:" + rangeSize + ", allCount:" + allCount);
                if (isUiAlive()) {
                    showVersionUpdateProgressDialog(isForce);
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
                    Toast.makeText(getContext(), R.string.wel_version_update_cancel, Toast.LENGTH_SHORT).show();
                    getUi().dismissVersionUpdateProgressDialog();
                    if (isForce) {
                        getActivity().finish();
                    } else {
                        autoLogin();
                    }
                }
            }

            @Override
            public void onDownloadError(Exception exception) {
                LogUtils.d(TAG, "onDownloadError onDownloadError:" + exception);
                if (isUiAlive()) {
                    String msg = "";
                    if (exception instanceof MessageException) {
                        msg = getContext().getString(R.string.wel_version_update_fail) +
                                "(" + exception.getMessage() + ")";
                    } else {
                        msg = getContext().getString(R.string.wel_version_update_fail);
                    }
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    getUi().dismissVersionUpdateProgressDialog();
                    if (isForce) {
                        getActivity().finish();
                    } else {
                        autoLogin();
                    }
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
        RouterManager.getInstance(RouterBundleKey.LOGIN_BUNDLE_KEY)
                .callOpCommand(BaseApplication.mCurResumedActivity, RouterCommand.LOGIN_autoLogin,
                        null, new IRouterCallback() {
                            @Override
                            public void onSuccess(Bundle responseBundle) {
                                if (isUiAlive()) {
                                    goWelcomeActivity();
                                }
                            }

                            @Override
                            public boolean onFail(String errorInfo) {
                                if (isUiAlive()) {
                                    goWelcomeActivity();
                                }
                                return true;
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
                        PackageInfo packageInfo = getContext().getPackageManager()
                                .getPackageInfo(getContext().getPackageName(), 0);
                        if (packageInfo.versionCode < versionEntity.getVersionCode()) {
                            if (versionEntity.isForce()) {
                                updateVersion(true);
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

    private void showVersionUpdateProgressDialog(boolean isForce) {
        getUi().showVersionUpdateProgressDialog(isForce ? null :
                new ProgressDialog.IDialogActionListener() {
                    @Override
                    public void onCancel() {
                        HttpRequestManager.cancelBySign(ApkVersionManager.getInstance().CANCEL_SIGN);
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
            Toast.makeText(getContext(), R.string.wel_version_update_fail, Toast.LENGTH_SHORT).show();
            autoLogin();
        }
        finishUi();
    }
}
