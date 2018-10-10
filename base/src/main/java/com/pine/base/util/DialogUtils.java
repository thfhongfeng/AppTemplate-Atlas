package com.pine.base.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pine.base.R;
import com.pine.base.manager.TencentShareManager;

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
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);
        TextView tip_tv = (TextView) v.findViewById(R.id.tip_tv);
        tip_tv.setText(msg);
        Dialog loadingDialog = new Dialog(context, R.style.BaseDialogStyle);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

    /**
     * 进度提示框
     *
     * @param context
     * @return
     */
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(R.string.base_downloading);
        dialog.setProgress(0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 提示框
     *
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog showConfirmDialog(Context context, String msg, final IActionListener listener) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.base_dialog_confirm, null);
        //对话框
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView msg_tv = (TextView) layout.findViewById(R.id.msg_tv);
        msg_tv.setText(msg);
        TextView cancel_btn_tv = (TextView) layout.findViewById(R.id.cancel_btn_tv);
        TextView confirm_btn_tv = (TextView) layout.findViewById(R.id.confirm_btn_tv);
        cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                }
                dialog.dismiss();
            }
        });
        confirm_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 分享弹出框
     *
     * @param context
     * @return
     */
    public static AlertDialog createShareDialog(final Context context, final List<String> titleList,
                                                final List<String> descList, final List<String> shareUrlList) {
        final View shareContent = LayoutInflater.from(context).inflate(R.layout.base_dialog_share, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(shareContent);
        final AlertDialog shareDialog = builder.create();
        shareDialog.setCanceledOnTouchOutside(true);
        List<Map<String, Object>> items = new ArrayList<>();
        final int[] shareIcon = {
                R.mipmap.base_ic_share_weixin,
                R.mipmap.base_ic_share_weixin_friend_circle,
                R.mipmap.base_ic_share_qq,
        };
        final String[] shareDesc = context.getResources().getStringArray(R.array.base_share_icon_desc_arr);
        for (int i = 0; i < shareIcon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", shareIcon[i]);
            map.put("desc", shareDesc[i]);
            items.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(context, items, R.layout.base_item_share,
                new String[]{"img", "desc"}, new int[]{R.id.share_img, R.id.share_desc});
        GridView gridView = (GridView) shareContent.findViewById(R.id.share_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        TencentShareManager.getInstance().sendWXRequest(context, false, shareUrlList.get(position),
                                titleList.get(position), descList.get(position));
                        break;
                    case 1:
                        TencentShareManager.getInstance().sendWXRequest(context, true, shareUrlList.get(position),
                                titleList.get(position), descList.get(position));
                        break;
                    case 2:
                        TencentShareManager.getInstance().shareToQQ(context, titleList.get(position),
                                descList.get(position), shareUrlList.get(position));
                        break;
                }
                shareDialog.dismiss();
            }
        });
        return shareDialog;
    }

    public interface IActionListener {
        void onConfirm();

        void onCancel();
    }
}
