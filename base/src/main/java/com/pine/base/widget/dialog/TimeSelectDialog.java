package com.pine.base.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.pine.base.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/2/27.
 */

public class TimeSelectDialog extends Dialog {
    private Builder mBuilder;

    private TimeSelectDialog(@NonNull Context context) {
        super(context);
    }

    private TimeSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public interface IDialogTimeSelected {
        void onSelected(Calendar calendar);
    }

    public static class Builder {
        private static final int MAX_YEARS = 10;
        private TextView cancel_tv, confirm_tv;
        private WheelPicker wheel_one, wheel_two, wheel_three;
        private Context context;
        private Calendar selectedTime;

        public Builder(Context context) {
            this.context = context;
        }

        public TimeSelectDialog create(IDialogTimeSelected dialogSelect) {
            return create(dialogSelect, true, true, true);
        }

        public TimeSelectDialog create(IDialogTimeSelected dialogSelect, boolean showSecond) {
            return create(dialogSelect, true, true, showSecond);
        }

        public TimeSelectDialog create(IDialogTimeSelected dialogSelect, boolean showHour,
                                       boolean showMinute, boolean showSecond) {
            if (!showHour && !showMinute && !showSecond) {
                return null;
            }
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TimeSelectDialog dialog = new TimeSelectDialog(context, R.style.BaseSelectDialogStyle);
            View layout = inflater.inflate(R.layout.base_dialog_date_or_time_select, null);
            cancel_tv = layout.findViewById(R.id.cancel_tv);
            confirm_tv = layout.findViewById(R.id.confirm_tv);
            wheel_one = layout.findViewById(R.id.wheel_one);
            wheel_two = layout.findViewById(R.id.wheel_two);
            wheel_three = layout.findViewById(R.id.wheel_three);
            initView(dialog, dialogSelect, showHour, showMinute, showSecond);
            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
            p.width = d.getWidth(); //宽度设置为屏幕
            dialog.getWindow().setAttributes(p); //设置生效
            dialog.mBuilder = this;
            return dialog;
        }

        private void initView(final TimeSelectDialog dialog, final IDialogTimeSelected dialogSelect,
                              boolean showHour, boolean showMinute, boolean showSecond) {
            selectedTime = Calendar.getInstance();
            selectedTime.setTime(new Date());
            if (showHour) {
                List<String> hourList = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    String hour = String.valueOf(i);
                    if (i < 10) {
                        hour = "0" + hour;
                    }
                    hourList.add(context.getString(R.string.base_time_hour, hour));
                }
                wheel_one.setData(hourList);
                wheel_one.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                        selectedTime.set(Calendar.HOUR_OF_DAY, i);
                    }
                });
                int hour = selectedTime.get(Calendar.HOUR_OF_DAY);
                wheel_one.setSelectedItemPosition(hour);
            } else {
                wheel_one.setVisibility(View.GONE);
            }
            if (showMinute) {
                List<String> minuteList = new ArrayList<>();
                for (int i = 0; i < 60; i++) {
                    String minute = String.valueOf(i);
                    if (i < 10) {
                        minute = "0" + minute;
                    }
                    minuteList.add(context.getString(R.string.base_time_minute, minute));
                }
                wheel_two.setData(minuteList);
                wheel_two.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                        selectedTime.set(Calendar.MINUTE, i);
                    }
                });
                int minute = selectedTime.get(Calendar.MINUTE);
                wheel_two.setSelectedItemPosition(minute);
            } else {
                wheel_two.setVisibility(View.GONE);
            }
            if (showSecond) {
                List<String> secondList = new ArrayList<>();
                for (int i = 0; i < 60; i++) {
                    String second = String.valueOf(i);
                    if (i < 10) {
                        second = "0" + second;
                    }
                    secondList.add(context.getString(R.string.base_time_second, second));
                }
                wheel_three.setData(secondList);
                wheel_three.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                        selectedTime.set(Calendar.SECOND, i);
                    }
                });
                int second = selectedTime.get(Calendar.SECOND);
                wheel_three.setSelectedItemPosition(second);
            } else {
                wheel_three.setVisibility(View.GONE);
            }

            cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            confirm_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialogSelect != null) {
                        dialogSelect.onSelected(selectedTime);
                    }
                }
            });
        }
    }
}
