package com.pine.login.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.base.widget.view.PicVerifyCodeImageView;
import com.pine.login.LoginUrlConstants;
import com.pine.login.R;
import com.pine.login.contract.IRegisterContract;
import com.pine.login.presenter.RegisterPresenter;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class RegisterActivity extends
        BaseMvpActionBarActivity<IRegisterContract.Ui, RegisterPresenter>
        implements IRegisterContract.Ui, View.OnClickListener {

    private ScrollView scroll_view;
    private TextView register_btn_tv;
    private PicVerifyCodeImageView verify_code_iv;
    private EditText mobile_et, verify_code_et;
    private EditText password_et, confirm_pwd_et;

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.login_register_title);
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.login_activity_register;
    }

    @Override
    protected void findViewOnCreate() {
        scroll_view = findViewById(R.id.scroll_view);
        mobile_et = findViewById(R.id.mobile_et);
        verify_code_et = findViewById(R.id.verify_code_et);
        verify_code_iv = findViewById(R.id.verify_code_iv);
        password_et = findViewById(R.id.password_et);
        confirm_pwd_et = findViewById(R.id.confirm_pwd_et);
        register_btn_tv = findViewById(R.id.register_btn_tv);
    }

    @Override
    protected void init() {
        verify_code_iv.init(LoginUrlConstants.Verify_Code_Image);
        register_btn_tv.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        verify_code_iv.onResume();
        super.onResume();
    }

    @NonNull
    @Override
    public InputParamBean getUserMobileParam(String key) {
        return new InputParamBean(this, key, mobile_et.getText().toString(),
                scroll_view, mobile_et);
    }

    @NonNull
    @Override
    public InputParamBean getVerificationCodeParam(String key) {
        return new InputParamBean(this, key, verify_code_et.getText().toString(),
                scroll_view, verify_code_et);
    }

    @NonNull
    @Override
    public InputParamBean getUserPasswordParam(String key) {
        return new InputParamBean(this, key, password_et.getText().toString(),
                scroll_view, password_et);
    }

    @NonNull
    @Override
    public InputParamBean getUserConfirmPasswordParam(String key) {
        return new InputParamBean(this, key, confirm_pwd_et.getText().toString(),
                scroll_view, confirm_pwd_et);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.verify_code_iv) {

        } else if (v.getId() == R.id.register_btn_tv) {

        }
    }
}
