package com.pine.login.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.login.R;
import com.pine.login.contract.ILoginContract;
import com.pine.login.presenter.LoginPresenter;

/**
 * Created by tanghongfeng on 2018/9/11.
 */

public class LoginActivity extends BaseMvpActionBarActivity<ILoginContract.Ui, LoginPresenter>
        implements ILoginContract.Ui, View.OnClickListener {

    private TextView login_btn_tv;
    private EditText mobile_et;
    private EditText password_et;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void findViewOnCreate() {
        mobile_et = findViewById(R.id.mobile_et);
        password_et = findViewById(R.id.password_et);
        login_btn_tv = findViewById(R.id.login_btn_tv);
    }

    @Override
    protected void initOnCreate() {
        login_btn_tv.setOnClickListener(this);
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.login_login_title);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login_btn_tv) {
            mPresenter.login();
        }
    }

    @Override
    public InputParamBean getUserMobileParam(String key) {
        return new InputParamBean(this, key, mobile_et.getText().toString());
    }

    @Override
    public InputParamBean getUserPasswordParam(String key) {
        return new InputParamBean(this, key, password_et.getText().toString());
    }
}
