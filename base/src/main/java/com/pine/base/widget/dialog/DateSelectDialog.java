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

public class DateSelectDialog extends Dialog {
    private Builder mBuilder;

    private DateSelectDialog(@NonNull Context context) {
        super(context);
    }

    private DateSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public static class Builder {
        private TextView cancelBtn, confirmBtn;
        private WheelPicker wheelYear, wheelMonth, wheelDay;
        private Context context;
        private Calendar selectedDate;
        private int startYear, endYear;

        public Builder(Context context) {
            this.context = context;
        }

        public DateSelectDialog create(IDialogDateSelected dialogSelect) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            return this.create(dialogSelect, year, year + 1);
        }

        public DateSelectDialog create(IDialogDateSelected dialogSelect, int startYear, int endYear) {
            this.startYear = startYear;
            this.endYear = endYear;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DateSelectDialog dialog = new DateSelectDialog(context, R.style.BaseSelectDialogStyle);
            View layout = inflater.inflate(R.layout.base_dialog_date_or_time_select, null);
            cancelBtn = (TextView) layout.findViewById(R.id.cancel_tv);
            confirmBtn = (TextView) layout.findViewById(R.id.confirm_tv);
            wheelYear = (WheelPicker) layout.findViewById(R.id.wheel_one);
            wheelMonth = (WheelPicker) layout.findViewById(R.id.wheel_two);
            wheelDay = (WheelPicker) layout.findViewById(R.id.wheel_three);
            initViewOnCreate(dialog, dialogSelect);
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

        private void initViewOnCreate(final DateSelectDialog dialog, final IDialogDateSelected dialogSelect) {
            List<String> yearList = new ArrayList<>();
            for (int i = 0; i <= endYear - startYear; i++) {
                yearList.add(context.getString(R.string.base_date_year, String.valueOf(startYear + i)));
            }
            wheelYear.setData(yearList);
            List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                String monthStr = String.valueOf(i + 1);
                if (i < 9) {
                    monthStr = "0" + monthStr;
                }
                monthList.add(context.getString(R.string.base_date_month, monthStr));
            }
            wheelMonth.setData(monthList);
            selectedDate = Calendar.getInstance();
            selectedDate.setTime(new Date());
            showDate(selectedDate);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialogSelect != null) {
                        dialogSelect.onSelected(selectedDate);
                    }
                }
            });
            wheelYear.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                    selectedDate.set(Calendar.YEAR, startYear + i);
                }
            });
            wheelMonth.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                    selectedDate.set(Calendar.MONTH, i);
                    showDate(selectedDate);
                }
            });
            wheelDay.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                    selectedDate.set(Calendar.DAY_OF_MONTH, i + 1);
                }
            });
        }

        public void showDate(Calendar calendar) {
            int month = calendar.get(Calendar.MONTH);
            wheelMonth.setSelectedItemPosition(month);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int dayCount = getMonthDayCount(calendar);
            List<String> dayList = new ArrayList<>();
            for (int i = 0; i < dayCount; i++) {
                String dayStr = String.valueOf(i + 1);
                if (i < 9) {
                    dayStr = "0" + dayStr;
                }
                dayList.add(context.getString(R.string.base_date_day, dayStr));
            }
            wheelDay.setData(dayList);
            wheelDay.setSelectedItemPosition(day - 1);
            int year = calendar.get(Calendar.YEAR);
            int index = wheelYear.getData().indexOf(context.getString(R.string.base_date_year,
                    String.valueOf(year)));
            wheelYear.setSelectedItemPosition(index);
        }

        private int getMonthDayCount(Calendar calendar) {
            Calendar c = Calendar.getInstance();
            c.setTime(calendar.getTime());
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            return c.get(Calendar.DATE);
        }

        public interface IDialogDateSelected {
            void onSelected(Calendar calendar);
        }
    }
}
