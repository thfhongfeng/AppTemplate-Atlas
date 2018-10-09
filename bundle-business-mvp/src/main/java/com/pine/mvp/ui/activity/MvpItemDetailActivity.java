package com.pine.mvp.ui.activity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.mvp.ui.activity.BaseMvpActionBarMenuActivity;
import com.pine.base.util.DialogUtils;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpItemDetailContract;
import com.pine.mvp.presenter.MvpItemDetailPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class MvpItemDetailActivity extends BaseMvpActionBarMenuActivity<IMvpItemDetailContract.Ui, MvpItemDetailPresenter>
        implements IMvpItemDetailContract.Ui {
    // 分享dialog
    private AlertDialog mShareDialog;

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv) {
        titleTv.setText(R.string.mvp_item_detail_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnIv.setImageResource(R.mipmap.base_ic_share);
        List<String> titleList = new ArrayList<String>();
        titleList.add("Item Detail Title");
        titleList.add("Item Detail Title");
        titleList.add("Item Detail Title");
        List<String> descList = new ArrayList<String>();
        descList.add("Item Detail Desc");
        descList.add("Item Detail Desc");
        descList.add("Item Detail Desc");
        List<String> shareUrlList = new ArrayList<String>();
        shareUrlList.add("http://www.baidu.com");
        shareUrlList.add("http://www.baidu.com");
        shareUrlList.add("http://www.baidu.com");
        mShareDialog = DialogUtils.createShareDialog(this, titleList, descList, shareUrlList);
        menuBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog.show();
            }
        });
    }

    @Override
    protected MvpItemDetailPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_item_detail;
    }

    @Override
    protected boolean initData() {
        return false;
    }

    @Override
    protected void initView() {

    }
}
