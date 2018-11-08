package com.pine.tool.util;

import android.content.Context;
import android.view.View;

import com.pine.tool.R;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public class ViewActionUtils {
    public static void scrollToTargetView(Context context, View parentScrollView, View view) {
        if (view == null) {
            return;
        }
        int offset = context.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        int[] location = new int[2];
        int[] scrollViewLocation = new int[2];
        view.getLocationOnScreen(location);
        parentScrollView.getLocationOnScreen(scrollViewLocation);
        int scrollY = parentScrollView.getScrollY();
        int targetY = location[1] + scrollY - scrollViewLocation[1] - view.getHeight() - offset;
        if (targetY < 0) {
            targetY = 0;
        }
        parentScrollView.scrollTo(0, targetY);
        view.requestFocus();
    }
}
