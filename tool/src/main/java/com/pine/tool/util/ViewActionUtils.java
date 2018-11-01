package com.pine.tool.util;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ScrollView;

import com.pine.tool.R;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public class ViewActionUtils {
    public static void scrollToTargetView(Context context, ScrollView scrollView, View view) {
        if (view == null) {
            return;
        }
        int offset = context.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        int[] location = new int[2];
        int[] scrollViewLocation = new int[2];
        view.getLocationOnScreen(location);
        scrollView.getLocationOnScreen(scrollViewLocation);
        int scrollY = scrollView.getScrollY();
        int targetY = location[1] + scrollY - scrollViewLocation[1] - view.getHeight() - offset;
        if (targetY < 0) {
            targetY = 0;
        }
        scrollView.scrollTo(0, targetY);
    }

    public static void scrollToTargetView(Context context, NestedScrollView scrollView, View view) {
        if (view == null) {
            return;
        }
        int offset = context.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        int[] location = new int[2];
        int[] scrollViewLocation = new int[2];
        view.getLocationOnScreen(location);
        scrollView.getLocationOnScreen(scrollViewLocation);
        int scrollY = scrollView.getScrollY();
        int targetY = location[1] + scrollY - scrollViewLocation[1] - view.getHeight() - offset;
        if (targetY < 0) {
            targetY = 0;
        }
        scrollView.scrollTo(0, targetY);
    }
}
