package com.pine.login.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.login_login_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected boolean initDataOnCreate() {
        return false;
    }

    @Override
    protected void initViewOnCreate() {
        mobile_et = (EditText) findViewById(R.id.mobile_et);
        password_et = (EditText) findViewById(R.id.password_et);
        login_btn_tv = (TextView) findViewById(R.id.login_btn_tv);

        login_btn_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login_btn_tv) {
            mPresenter.login();
        }
    }

    @Override
    public void showLoginResultToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getUserMobile() {
        return mobile_et.getText().toString();
    }

    @Override
    public String getUserPassword() {
        return password_et.getText().toString();
    }
}
