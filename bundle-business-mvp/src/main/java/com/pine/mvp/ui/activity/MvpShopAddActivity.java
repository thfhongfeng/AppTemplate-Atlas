package com.pine.mvp.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarTextMenuActivity;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.base.util.DialogUtils;
import com.pine.base.widget.dialog.DateSelectDialog;
import com.pine.base.widget.dialog.InputTextDialog;
import com.pine.base.widget.dialog.ProvinceSelectDialog;
import com.pine.base.widget.dialog.SelectItemDialog;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpShopAddContract;
import com.pine.mvp.presenter.MvpShopAddPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/10/23
 */

@PermissionsAnnotation(Permissions = {Manifest.permission.CAMERA})
public class MvpShopAddActivity extends BaseMvpActionBarTextMenuActivity<IMvpShopAddContract.Ui, MvpShopAddPresenter>
        implements IMvpShopAddContract.Ui, View.OnClickListener {
    private LinearLayout type_ll, online_time_ll;
    private EditText name_et, description_et, remark_et;
    private TextView type_tv, online_time_tv, contact_tv, address_tv;
    private InputTextDialog mContactInputDialog;
    private DateSelectDialog mOnLineDateSelectDialog;
    private SelectItemDialog mTypeSelectDialog;
    private ProvinceSelectDialog mProvinceSelectDialog;

    @Override
    protected MvpShopAddPresenter createPresenter() {
        return new MvpShopAddPresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        titleTv.setText(R.string.mvp_shop_add_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnTv.setText(R.string.mvp_shop_add_confirm_menu);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addShop();
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_add;
    }

    @Override
    protected void initViewOnCreate() {
        type_ll = findViewById(R.id.type_ll);
        online_time_ll = findViewById(R.id.online_time_ll);
        name_et = findViewById(R.id.name_et);
        description_et = findViewById(R.id.description_et);
        remark_et = findViewById(R.id.remark_et);
        type_tv = findViewById(R.id.type_tv);
        online_time_tv = findViewById(R.id.online_time_tv);
        contact_tv = findViewById(R.id.contact_tv);
        address_tv = findViewById(R.id.address_tv);
    }

    @Override
    protected void onAllAccessRestrictionReleased() {
        type_ll.setOnClickListener(this);
        online_time_ll.setOnClickListener(this);
        contact_tv.setOnClickListener(this);
        address_tv.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        if (mTypeSelectDialog != null && mTypeSelectDialog.isShowing()) {
            mTypeSelectDialog.dismiss();
        }
        if (mContactInputDialog != null && mContactInputDialog.isShowing()) {
            mContactInputDialog.dismiss();
        }
        if (mOnLineDateSelectDialog != null && mOnLineDateSelectDialog.isShowing()) {
            mOnLineDateSelectDialog.dismiss();
        }
        if (mProvinceSelectDialog != null && mProvinceSelectDialog.isShowing()) {
            mProvinceSelectDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.type_ll) {
            if (mTypeSelectDialog == null) {
                mTypeSelectDialog = DialogUtils.createItemSelectDialog(this,
                        mPresenter.getShopTypeArr(), 0, new SelectItemDialog.IDialogSelectListener() {
                            @Override
                            public void onSelect(String selectText, int position) {
                                type_tv.setText(selectText);
                            }
                        });
            }
            mTypeSelectDialog.show();
        } else if (v.getId() == R.id.online_time_ll) {
            if (mOnLineDateSelectDialog == null) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                mOnLineDateSelectDialog = DialogUtils.createDateSelectDialog(this,
                        year, year + 1, new DateSelectDialog.IDialogDateSelected() {
                            @Override
                            public void onSelected(Calendar calendar) {
                                online_time_tv
                                        .setText(new SimpleDateFormat("yyyy-MM-dd")
                                                .format(calendar.getTime()));
                            }
                        });
            }
            mOnLineDateSelectDialog.show();
        } else if (v.getId() == R.id.contact_tv) {
            if (mContactInputDialog == null) {
                mContactInputDialog = DialogUtils.createTextInputDialog(this, getString(R.string.mvp_shop_add_contact_hint),
                        "", 11,
                        EditorInfo.TYPE_CLASS_NUMBER, new InputTextDialog.IActionClickListener() {

                            @Override
                            public void onSubmitClick(Dialog dialog, List<String> textList) {
                                contact_tv.setText(textList.get(0));
                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {

                            }
                        });
            }
            mContactInputDialog.show();
        } else if (v.getId() == R.id.address_tv) {
            if (mProvinceSelectDialog == null) {
                mProvinceSelectDialog = DialogUtils.createProvinceSelectDialog(this,
                        new ProvinceSelectDialog.IDialogDateSelected() {
                            @Override
                            public void onSelected(String provinceName, String cityName,
                                                   String districtName, String zipCode) {
                                address_tv.setText(provinceName + cityName + districtName);
                            }
                        });
            }
            mProvinceSelectDialog.show();
        }
    }
}
