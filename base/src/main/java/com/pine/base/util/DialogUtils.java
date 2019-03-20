package com.pine.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pine.base.R;
import com.pine.base.component.share.bean.ShareBean;
import com.pine.base.component.share.manager.ShareManager;
import com.pine.base.widget.dialog.DateSelectDialog;
import com.pine.base.widget.dialog.InputTextDialog;
import com.pine.base.widget.dialog.ProgressDialog;
import com.pine.base.widget.dialog.ProvinceSelectDialog;
import com.pine.base.widget.dialog.SelectItemDialog;
import com.pine.base.widget.dialog.TimeSelectDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/7.
 */

public class DialogUtils {
    /**
     * 加载框
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.base_dialog_loading, null);
        LinearLayout layout = v.findViewById(R.id.dialog_loading_view);
        TextView tip_tv = v.findViewById(R.id.tip_tv);
        tip_tv.setText(msg);
        Dialog loadingDialog = new Dialog(context, R.style.BaseDialogStyle);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

    /**
     * 下载进度提示框
     *
     * @param context
     * @return
     */
    public static ProgressDialog createDownloadProgressDialog(Context context,
                                                              int initProgress,
                                                              ProgressDialog.IDialogActionListener listener) {
        final ProgressDialog dialog = new ProgressDialog.Builder(context)
                .create(initProgress, listener);
        return dialog;
    }

    /**
     * 确认提示框
     *
     * @param content
     * @param listener
     * @return
     */
    public static Dialog showConfirmDialog(Context context, String content, final IActionListener listener) {
        return showConfirmDialog(context, "", content,
                context.getString(R.string.base_cancel), Color.parseColor("#999999"),
                context.getString(R.string.base_confirm), Color.parseColor("#70B642"),
                listener);
    }

    /**
     * 提示框
     *
     * @param content
     * @param listener
     * @return
     */
    public static Dialog showConfirmDialog(Context context, String title, String content,
                                           String leftBtnText, @ColorInt int leftColor,
                                           String rightBtnText, @ColorInt int rightColor,
                                           final IActionListener listener) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.base_dialog_confirm, null);
        //对话框
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView title_tv = layout.findViewById(R.id.title_tv);
        TextView content_tv = layout.findViewById(R.id.content_tv);
        if (TextUtils.isEmpty(title)) {
            title_tv.setVisibility(View.GONE);
        } else {
            title_tv.setText(title);
        }
        content_tv.setText(content);
        TextView left_btn_tv = layout.findViewById(R.id.left_btn_tv);
        TextView right_btn_tv = layout.findViewById(R.id.right_btn_tv);
        left_btn_tv.setTextColor(leftColor);
        right_btn_tv.setTextColor(rightColor);
        left_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftBtnClick();
                }
                dialog.dismiss();
            }
        });
        right_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRightBtnClick();
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 分享弹出框
     *
     * @param activity
     * @param shareBeanList
     * @return
     */
    public static AlertDialog createShareDialog(final Activity activity, @NonNull final ArrayList<ShareBean> shareBeanList) {
        final View shareContent = LayoutInflater.from(activity).inflate(R.layout.base_dialog_share, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(shareContent);
        final AlertDialog shareDialog = builder.create();
        shareDialog.setCanceledOnTouchOutside(true);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < shareBeanList.size(); i++) {
            ShareBean shareBean = shareBeanList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("img", shareBean.getIconId());
            map.put("desc", shareBean.getIconName());
            items.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(activity, items, R.layout.base_item_share,
                new String[]{"img", "desc"}, new int[]{R.id.share_img, R.id.share_desc});
        GridView gridView = shareContent.findViewById(R.id.share_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShareBean shareBean = shareBeanList.get(position);
                ShareManager.getInstance().share(activity, shareBean);
                shareDialog.dismiss();
            }
        });
        return shareDialog;
    }

    /**
     * 普通文本输入框
     *
     * @param context
     * @param title
     * @param originalText
     * @param inputMaxLength
     * @param actionClickListener
     * @return
     */
    public static InputTextDialog createTextInputDialog(final Context context, String title,
                                                        String originalText, final int inputMaxLength,
                                                        final InputTextDialog.IActionClickListener actionClickListener) {
        return createTextInputDialog(context, title, originalText, inputMaxLength,
                -1, actionClickListener);
    }

    /**
     * 普通文本输入框
     *
     * @param context
     * @param title
     * @param originalText
     * @param inputMaxLength
     * @param inputType           {@link EditorInfo#inputType}
     * @param actionClickListener
     * @return
     */
    public static InputTextDialog createTextInputDialog(final Context context, String title, String originalText,
                                                        final int inputMaxLength, int inputType,
                                                        final InputTextDialog.IActionClickListener actionClickListener) {
        final InputTextDialog.Builder builder = new InputTextDialog.Builder(context);
        builder.setActionClickListener(actionClickListener);
        final InputTextDialog dialog = builder.create(title, originalText, inputMaxLength, inputType);
        return dialog;
    }

    /**
     * 有千分位分隔符数字文本输入框
     *
     * @param context
     * @param title
     * @param originalText
     * @param inputMaxLength
     * @param actionClickListener
     * @return
     */
    public static InputTextDialog createThousandthNumberInputDialog(final Context context,
                                                                    String title, String originalText,
                                                                    final int inputMaxLength,
                                                                    final InputTextDialog.IActionClickListener actionClickListener) {
        return createThousandthNumberInputDialog(context, title, originalText,
                inputMaxLength, false, 0, actionClickListener);
    }

    /**
     * 有千分位分隔符数字文本输入框
     *
     * @param context
     * @param title
     * @param originalText
     * @param inputMaxLength
     * @param allowDecimal
     * @param decimalNum
     * @param actionClickListener
     * @return
     */
    public static InputTextDialog createThousandthNumberInputDialog(final Context context,
                                                                    String title, String originalText,
                                                                    final int inputMaxLength,
                                                                    boolean allowDecimal, int decimalNum,
                                                                    final InputTextDialog.IActionClickListener actionClickListener) {
        final InputTextDialog.Builder builder = new InputTextDialog.Builder(context);
        builder.setActionClickListener(actionClickListener);
        final InputTextDialog dialog = builder.thousandthNumberInputCreate(title, originalText, inputMaxLength, allowDecimal, decimalNum);
        return dialog;
    }

    /**
     * 元素选择弹出框
     *
     * @param context
     * @param itemTextList
     * @param currentPosition
     * @param listener
     * @return
     */
    public static SelectItemDialog createItemSelectDialog(final Context context, String[] itemTextList,
                                                          int currentPosition, SelectItemDialog.IDialogSelectListener listener) {
        return new SelectItemDialog.Builder(context).create(itemTextList, currentPosition, listener);
    }

    /**
     * 日期(年月日)选择弹出框
     *
     * @param context
     * @param startYear
     * @param endYear
     * @param dialogSelect
     * @return
     */
    public static DateSelectDialog createDateSelectDialog(final Context context,
                                                          int startYear, int endYear,
                                                          DateSelectDialog.IDialogDateSelected dialogSelect) {
        return new DateSelectDialog.Builder(context).create(dialogSelect, startYear, endYear);
    }

    /**
     * 时间(时分秒)选择弹出框
     *
     * @param context
     * @param dialogSelect
     * @return
     */
    public static TimeSelectDialog createTimeSelectDialog(final Context context,
                                                          TimeSelectDialog.IDialogTimeSelected dialogSelect) {
        return createTimeSelectDialog(context, true, true, true, dialogSelect);
    }

    /**
     * 时间(时分秒)选择弹出框
     *
     * @param context
     * @param showHour
     * @param showMinute
     * @param showSecond
     * @param dialogSelect
     * @return
     */
    public static TimeSelectDialog createTimeSelectDialog(final Context context, boolean showHour,
                                                          boolean showMinute, boolean showSecond,
                                                          TimeSelectDialog.IDialogTimeSelected dialogSelect) {
        return new TimeSelectDialog.Builder(context).create(dialogSelect, showHour, showMinute, showSecond);
    }

    /**
     * 省市区选择弹出框
     *
     * @param context
     * @param dialogSelect
     * @return
     */
    public static ProvinceSelectDialog createProvinceSelectDialog(final Context context,
                                                                  ProvinceSelectDialog.IDialogDateSelected dialogSelect) {
        return new ProvinceSelectDialog.Builder(context).create(dialogSelect);
    }


    public interface IActionListener {
        void onLeftBtnClick();

        void onRightBtnClick();
    }
}
