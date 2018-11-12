package com.pine.mvp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pine.base.access.UiAccessAnnotation;
import com.pine.base.access.UiAccessType;
import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarTextMenuActivity;
import com.pine.base.util.DialogUtils;
import com.pine.base.widget.dialog.DateSelectDialog;
import com.pine.base.widget.dialog.InputTextDialog;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpTravelNoteReleaseContract;
import com.pine.mvp.presenter.MvpTravelNoteReleasePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/10/23
 */

@UiAccessAnnotation(AccessTypes = {UiAccessType.LOGIN}, Args = {""})
public class MvpTravelNoteReleaseActivity extends
        BaseMvpActionBarTextMenuActivity<IMvpTravelNoteReleaseContract.Ui, MvpTravelNoteReleasePresenter>
        implements IMvpTravelNoteReleaseContract.Ui, View.OnClickListener {
    private final int REQUEST_CODE_BAIDU_MAP = 1;
    private SwipeRefreshLayout swipe_refresh_layout;
    private NestedScrollView nested_scroll_view;
    private RelativeLayout set_out_date_ll, day_count_ll, belong_shop_ll;
    private LinearLayout day_note_ll;
    private EditText title_et, preface_et;
    private TextView set_out_date_tv, day_count_tv, belong_shop_tv;
    private InputTextDialog mDayCountInputDialog;
    private DateSelectDialog mSetOutDateSelectDialog;

    @Override
    protected MvpTravelNoteReleasePresenter createPresenter() {
        return new MvpTravelNoteReleasePresenter();
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_travel_note_release;
    }

    @Override
    protected void findViewOnCreate() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        nested_scroll_view = findViewById(R.id.nested_scroll_view);
        set_out_date_ll = findViewById(R.id.set_out_date_ll);
        day_count_ll = findViewById(R.id.day_count_ll);
        belong_shop_ll = findViewById(R.id.belong_shop_ll);
        day_note_ll = findViewById(R.id.day_note_ll);
        title_et = findViewById(R.id.title_et);
        preface_et = findViewById(R.id.preface_et);
        set_out_date_tv = findViewById(R.id.set_out_date_tv);
        day_count_tv = findViewById(R.id.day_count_tv);
        belong_shop_tv = findViewById(R.id.belong_shop_tv);
    }

    @Override
    protected void initOnCreate() {
        set_out_date_ll.setOnClickListener(this);
        belong_shop_ll.setOnClickListener(this);
        day_count_ll.setOnClickListener(this);

        swipe_refresh_layout.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
        swipe_refresh_layout.setEnabled(false);
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        titleTv.setText(R.string.mvp_note_release_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnTv.setText(R.string.mvp_note_release_confirm_menu);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNoteBtnClicked();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mDayCountInputDialog != null && mDayCountInputDialog.isShowing()) {
            mDayCountInputDialog.dismiss();
        }
        if (mSetOutDateSelectDialog != null && mSetOutDateSelectDialog.isShowing()) {
            mSetOutDateSelectDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.set_out_date_ll) {
            if (mSetOutDateSelectDialog == null) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                mSetOutDateSelectDialog = DialogUtils.createDateSelectDialog(this,
                        year, year + 1, new DateSelectDialog.IDialogDateSelected() {
                            @Override
                            public void onSelected(Calendar calendar) {
                                set_out_date_tv
                                        .setText(new SimpleDateFormat("yyyy-MM-dd")
                                                .format(calendar.getTime()));
                            }
                        });
            }
            mSetOutDateSelectDialog.show();
        } else if (v.getId() == R.id.day_count_ll) {
            if (mDayCountInputDialog == null) {
                mDayCountInputDialog = DialogUtils.createTextInputDialog(this,
                        getString(R.string.mvp_shop_release_day_count_hint),
                        "", 3,
                        EditorInfo.TYPE_CLASS_NUMBER, new InputTextDialog.IActionClickListener() {

                            @Override
                            public void onSubmitClick(Dialog dialog, List<String> textList) {
                                day_count_tv.setText(textList.get(0));
                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {

                            }
                        });
            }
            mDayCountInputDialog.show();
        } else if (v.getId() == R.id.belong_shop_ll) {

        }
    }

    private void onAddNoteBtnClicked() {
        mPresenter.addNote();
    }

    @Override
    public void setSwipeRefreshLayoutRefresh(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        if (processing) {
            if (!swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(processing);
            }
        } else {
            swipe_refresh_layout.setRefreshing(processing);
        }
    }

    @NonNull
    @Override
    public InputParamBean getNoteTitleParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, title_et.getText().toString(), title_et);
    }

    @NonNull
    @Override
    public InputParamBean getNoteSetOutDateParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, set_out_date_tv.getText().toString(), set_out_date_tv);
    }

    @NonNull
    @Override
    public InputParamBean getNoteTravelDayCountParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, day_count_tv.getText().toString(), day_count_tv);
    }

    @NonNull
    @Override
    public InputParamBean getNoteBelongShopsParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, belong_shop_tv.getTag() == null ? "" : belong_shop_tv.getTag().toString(), belong_shop_tv);
    }

    @NonNull
    @Override
    public InputParamBean getNoteBelongShopNamesParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, belong_shop_tv.getText().toString(), belong_shop_tv);
    }

    @NonNull
    @Override
    public InputParamBean getNotePrefaceParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, preface_et.getText().toString(), preface_et);
    }

    @NonNull
    @Override
    public InputParamBean getNoteContentParam(String key) {
        return new InputParamBean(this, nested_scroll_view,
                key, "", day_note_ll);
    }
}
