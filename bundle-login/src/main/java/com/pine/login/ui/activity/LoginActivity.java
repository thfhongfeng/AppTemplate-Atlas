package com.pine.login.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.base.bean.BaseInputParam;
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
    private TextView go_register_tv;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void findViewOnCreate() {
        mobile_et = findViewById(R.id.mobile_et);
        password_et = findViewById(R.id.password_et);
        login_btn_tv = findViewById(R.id.login_btn_tv);
        go_register_tv = findViewById(R.id.go_register_tv);
    }

    @Override
    protected void init() {
        login_btn_tv.setOnClickListener(this);
        go_register_tv.setOnClickListener(this);
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.login_login_title);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login_btn_tv) {
            mPresenter.login();
        } else if (id == R.id.go_register_tv) {
            mPresenter.goRegister();
        }
    }

    @Override
    public BaseInputParam getUserMobileParam(String key) {
        return new BaseInputParam(this, key, mobile_et.getText().toString());
    }

    @Override
    public BaseInputParam getUserPasswordParam(String key) {
        return new BaseInputParam(this, key, password_et.getText().toString());
    }
}
