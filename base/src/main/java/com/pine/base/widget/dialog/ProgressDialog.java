package com.pine.base.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pine.base.R;

/**
 * Created by tanghongfeng on 2018/11/12
 */

public class ProgressDialog extends Dialog {
    private Builder mBuilder;

    protected ProgressDialog(Context context) {
        super(context);
    }

    protected ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setProgress(int progress) {
        mBuilder.setProgress(progress);
    }

    public interface IDialogActionListener {
        void onCancel();
    }

    public static class Builder {
        private Context context;
        private TextView cancel_tv, progress_tv, title_tv;
        private ProgressBar progress_bar;

        public Builder(Context context) {
            this.context = context;
        }

        public ProgressDialog create(int initProgress, final IDialogActionListener listener) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.base_dialog_progress, null);
            title_tv = view.findViewById(R.id.title_tv);
            progress_bar = view.findViewById(R.id.progress_bar);
            cancel_tv = view.findViewById(R.id.cancel_tv);
            progress_tv = view.findViewById(R.id.progress_tv);
            progress_bar.setProgress(initProgress);
            progress_tv.setText(initProgress + "/100");
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setContentView(view);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            dialog.getWindow().setAttributes(p); //设置生效
            if (listener != null) {
                cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCancel();
                        dialog.dismiss();
                    }
                });
                cancel_tv.setVisibility(View.VISIBLE);
            } else {
                cancel_tv.setVisibility(View.GONE);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.mBuilder = this;
            return dialog;
        }

        public void setProgress(int progress) {
            progress_bar.setProgress(progress);
            progress_tv.setText(progress + "/100");
        }
    }
}
