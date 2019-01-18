package com.pine.welcome.ui.activity;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.base.util.DialogUtils;
import com.pine.base.widget.dialog.ProgressDialog;
import com.pine.welcome.R;
import com.pine.welcome.contract.ILoadingContract;
import com.pine.welcome.presenter.LoadingPresenter;

public class LoadingActivity extends BaseMvpNoActionBarActivity<ILoadingContract.Ui, LoadingPresenter>
        implements ILoadingContract.Ui {
    private Dialog mUpdateConfirmDialog;
    private ProgressDialog mUpdateProgressDialog;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.wel_activity_loading;
    }

    @Override
    protected void findViewOnCreate() {

    }

    @Override
    protected void init() {
        mPresenter.loadBundleSwitcherData();
    }

    @Override
    protected void onDestroy() {
        if (mUpdateConfirmDialog != null) {
            mUpdateConfirmDialog.dismiss();
            mUpdateConfirmDialog = null;
        }
        if (mUpdateProgressDialog != null) {
            mUpdateProgressDialog.dismiss();
            mUpdateProgressDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void showVersionUpdateConfirmDialog(String newVersionName) {
        if (mUpdateConfirmDialog == null) {
            mUpdateConfirmDialog = new Dialog(this);
            mUpdateConfirmDialog.setContentView(R.layout.wel_dialog_version_update_confirm);
            mUpdateConfirmDialog.setCanceledOnTouchOutside(false);
            mUpdateConfirmDialog.setCancelable(false);
            mUpdateConfirmDialog.setOwnerActivity(this);
            ((TextView) mUpdateConfirmDialog.findViewById(R.id.reason_tv)).setText(String.format(getString(R.string.wel_new_version_available), newVersionName));
            mUpdateConfirmDialog.findViewById(R.id.cancel_btn_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUpdateConfirmDialog.dismiss();
                    mPresenter.autoLogin();
                }
            });
            mUpdateConfirmDialog.findViewById(R.id.confirm_ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUpdateConfirmDialog.dismiss();
                    mPresenter.updateVersion(false);
                }
            });
        }
        mUpdateConfirmDialog.show();
        CountDownTimer countDownTimer = new CountDownTimer(900000, 1000) {

            @Override
            public void onFinish() {
                if (mUpdateConfirmDialog != null && BaseApplication.mCurResumedActivity.getClass() == LoadingActivity.class &&
                        mUpdateConfirmDialog.isShowing()) {
                    mUpdateConfirmDialog.findViewById(R.id.confirm_ll).performClick();
                    mUpdateConfirmDialog.findViewById(R.id.count_time_tv).setVisibility(View.GONE);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (mUpdateConfirmDialog != null && BaseApplication.mCurResumedActivity.getClass() == LoadingActivity.class &&
                        mUpdateConfirmDialog.isShowing()) {
                    ((TextView) mUpdateConfirmDialog.findViewById(R.id.count_time_tv)).setText("(" + millisUntilFinished / 1000 + ")");
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void showVersionUpdateProgressDialog(ProgressDialog.IDialogActionListener listener) {
        if (mUpdateProgressDialog != null) {
            mUpdateProgressDialog.dismiss();
        }
        mUpdateProgressDialog = DialogUtils.createDownloadProgressDialog(this,
                0, listener);
        mUpdateProgressDialog.show();
    }

    @Override
    public void updateVersionUpdateProgressDialog(int progress) {
        if (mUpdateProgressDialog != null) {
            mUpdateProgressDialog.setProgress(progress);
        }
    }

    @Override
    public void dismissVersionUpdateProgressDialog() {
        if (mUpdateProgressDialog != null) {
            mUpdateProgressDialog.dismiss();
        }
    }
}
