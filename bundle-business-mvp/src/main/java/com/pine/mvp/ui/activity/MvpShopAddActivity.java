package com.pine.mvp.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pine.base.BaseConstants;
import com.pine.base.access.UiAccessAnnotation;
import com.pine.base.access.UiAccessType;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarTextMenuActivity;
import com.pine.base.component.map.MapSdkManager;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.base.util.DialogUtils;
import com.pine.base.widget.dialog.DateSelectDialog;
import com.pine.base.widget.dialog.InputTextDialog;
import com.pine.base.widget.dialog.ProvinceSelectDialog;
import com.pine.base.widget.dialog.SelectItemDialog;
import com.pine.base.widget.view.ImageUploadView;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopAddContract;
import com.pine.mvp.presenter.MvpShopAddPresenter;
import com.pine.tool.util.DecimalUtils;
import com.pine.tool.util.ViewActionUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/10/23
 */

@PermissionsAnnotation(Permissions = {Manifest.permission.CAMERA})
@UiAccessAnnotation(AccessTypes = {UiAccessType.LOGIN}, Args = {""})
public class MvpShopAddActivity extends BaseMvpActionBarTextMenuActivity<IMvpShopAddContract.Ui, MvpShopAddPresenter>
        implements IMvpShopAddContract.Ui, View.OnClickListener {
    private final int REQUEST_CODE_BAIDU_MAP = 1;
    private NestedScrollView nested_scroll_view;
    private LinearLayout type_ll, online_date_ll;
    private EditText name_et, address_detail_et, description_et, remark_et;
    private TextView type_tv, online_date_tv, contact_tv, address_tv, address_marker_tv;
    private ImageUploadView photo_iuv;
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
                onAddShopBtnClicked();
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_add;
    }

    @Override
    protected void initViewOnCreate() {
        nested_scroll_view = findViewById(R.id.nested_scroll_view);
        type_ll = findViewById(R.id.type_ll);
        online_date_ll = findViewById(R.id.online_date_ll);
        name_et = findViewById(R.id.name_et);
        address_detail_et = findViewById(R.id.address_detail_et);
        description_et = findViewById(R.id.description_et);
        remark_et = findViewById(R.id.remark_et);
        type_tv = findViewById(R.id.type_tv);
        online_date_tv = findViewById(R.id.online_date_tv);
        contact_tv = findViewById(R.id.contact_tv);
        address_tv = findViewById(R.id.address_tv);
        address_marker_tv = findViewById(R.id.address_marker_tv);
        photo_iuv = findViewById(R.id.photo_iuv);
    }

    @Override
    protected void onAllAccessRestrictionReleased() {
        type_ll.setOnClickListener(this);
        online_date_ll.setOnClickListener(this);
        contact_tv.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        address_marker_tv.setOnClickListener(this);

        photo_iuv.init(this, MvpUrlConstants.Add_HomeShopPhoto,
                makeUploadParams(), true,
                new ImageUploadView.UploadResponseAdapter() {
                    @Override
                    public String getRemoteUrl(JSONObject response) {
                        // Test code begin
                        if (response == null) {
                            return null;
                        }
                        if (!response.optBoolean(BaseConstants.SUCCESS)) {
                            return null;
                        }
                        JSONObject data = response.optJSONObject(BaseConstants.DATA);
                        if (data == null) {
                            return null;
                        }
                        return data.optString("fileUrl");
                        // Test code end
                    }
                });
        photo_iuv.setCropEnable();
    }

    private HashMap<String, String> makeUploadParams() {
        HashMap<String, String> params = new HashMap<>();
        // Test code begin
        params.put("bizType", "10");
        params.put("orderNum", "100");
        params.put("orderNum", "100");
        params.put("descr", "desc");
        params.put("fileType", "1");
        // Test code end
        return params;
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
                        mPresenter.getShopTypeNameArr(), 0, new SelectItemDialog.IDialogSelectListener() {
                            @Override
                            public void onSelect(String selectText, int position) {
                                type_tv.setText(selectText);
                                type_tv.setTag(position);
                            }
                        });
            }
            mTypeSelectDialog.show();
        } else if (v.getId() == R.id.online_date_ll) {
            if (mOnLineDateSelectDialog == null) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                mOnLineDateSelectDialog = DialogUtils.createDateSelectDialog(this,
                        year, year + 1, new DateSelectDialog.IDialogDateSelected() {
                            @Override
                            public void onSelected(Calendar calendar) {
                                online_date_tv
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
                                address_tv.setTag(zipCode);
                            }
                        });
            }
            mProvinceSelectDialog.show();
        } else if (v.getId() == R.id.address_marker_tv) {
            String marker = address_marker_tv.getText().toString();
            double[] latLng = new double[2];
            latLng[0] = -1;
            latLng[1] = -1;
            if (!TextUtils.isEmpty(marker)) {
                String[] latLngStr = marker.split(",");
                if (latLngStr.length == 2) {
                    latLng[0] = DecimalUtils.format(latLngStr[0].trim(), 6);
                    latLng[1] = DecimalUtils.format(latLngStr[1].trim(), 6);
                }
            }
            startActivityForResult(MapSdkManager.getInstance().getMapActivityIntent(this,
                    MapSdkManager.MapType.MAP_TYPE_NORMAL, latLng[0], latLng[1]),
                    REQUEST_CODE_BAIDU_MAP);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BAIDU_MAP) {
            if (resultCode == RESULT_OK) {
                address_marker_tv.setText(DecimalUtils.format(data.getDoubleExtra("latitude", 0d), 6) + "," +
                        DecimalUtils.format(data.getDoubleExtra("longitude", 0d), 6));
            }
        }
        photo_iuv.onActivityResult(requestCode, resultCode, data);
    }

    private void onAddShopBtnClicked() {
        MvpShopDetailEntity entity = new MvpShopDetailEntity();
        String name = name_et.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.mvp_shop_add_name_need_msg, Toast.LENGTH_SHORT).show();
            ViewActionUtils.scrollToTargetView(this, nested_scroll_view, name_et);
            return;
        }
        entity.setName(name);

        String typeName = type_tv.getText().toString();
        if (TextUtils.isEmpty(typeName)) {
            Toast.makeText(this, R.string.mvp_shop_add_type_need_msg, Toast.LENGTH_SHORT).show();
            ViewActionUtils.scrollToTargetView(this, nested_scroll_view, type_tv);
            return;
        }
        entity.setTypeName(typeName);
        entity.setType(type_tv.getTag().toString());

        String onlineDate = online_date_tv.getText().toString();
        if (TextUtils.isEmpty(onlineDate)) {
            Toast.makeText(this, R.string.mvp_shop_add_online_date_need_msg, Toast.LENGTH_SHORT).show();
            ViewActionUtils.scrollToTargetView(this, nested_scroll_view, online_date_tv);
            return;
        }
        entity.setOnlineDate(onlineDate);

        String mobile = contact_tv.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, R.string.mvp_shop_add_contact_need_msg, Toast.LENGTH_SHORT).show();
            ViewActionUtils.scrollToTargetView(this, nested_scroll_view, contact_tv);
            return;
        }
        entity.setMobile(mobile);

        String address = address_tv.getText().toString();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, R.string.mvp_shop_add_address_need_msg, Toast.LENGTH_SHORT).show();
            ViewActionUtils.scrollToTargetView(this, nested_scroll_view, address_tv);
            return;
        }
        entity.setAddress(address);
        entity.setAddressZipCode(address_tv.getTag().toString());

        entity.setDescription(description_et.getText().toString());
        entity.setRemark(remark_et.getText().toString());

        mPresenter.addShop(entity);
    }
}
