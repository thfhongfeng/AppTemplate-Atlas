package com.pine.base.widget.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pine.base.R;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class BottomTabNavigationBar extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private ImageView bottom_main_home_iv, bottom_user_center_iv;
    private LinearLayout bottom_main_home_ll, bottom_user_center_ll;
    private View layout_view;
    private int mCurrentItem = 0;

    public BottomTabNavigationBar(Context context) {
        super(context);
        mContext = context;
        initView();
        initEvent();
    }

    public BottomTabNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initEvent();
        init(attrs);
    }

    private void initView() {
        layout_view = LayoutInflater.from(mContext).inflate(R.layout.base_bottom_tab_navigation_bar, this, true);
        setBackgroundColor(Color.TRANSPARENT);
        bottom_main_home_iv = (ImageView) layout_view.findViewById(R.id.bottom_main_home_iv);
        bottom_user_center_iv = (ImageView) layout_view.findViewById(R.id.bottom_user_center_iv);
        bottom_main_home_ll = (LinearLayout) layout_view.findViewById(R.id.bottom_main_home_ll);
        bottom_user_center_ll = (LinearLayout) layout_view.findViewById(R.id.bottom_user_center_ll);
    }

    private void initEvent() {
        bottom_main_home_ll.setOnClickListener(this);
        bottom_user_center_ll.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BottomTabNavigationBar);
        mCurrentItem = typedArray.getInt(R.styleable.BottomTabNavigationBar_selectedItem, 0);
        switch (mCurrentItem) {
            case 0:
                bottom_main_home_iv.setSelected(true);
                break;
            case 1:
                bottom_user_center_iv.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bottom_main_home_ll) {
            if (mCurrentItem != 0) {
                RouterFactory.getMainBundleManager().callUiCommand((Activity) mContext, RouterCommand.MAIN_goMainHomeActivity, null, null);
            }
        } else if (id == R.id.bottom_user_center_ll) {
            if (mCurrentItem != 1) {
                RouterFactory.getUserBundleManager().callUiCommand((Activity) mContext, RouterCommand.USER_goUserCenterActivity, null, null);
            }
        }
    }
}
