package com.pine.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pine.base.R;

/**
 * Created by tanghongfeng on 2018/10/24
 */

public class SelectItemDialog extends Dialog {

    protected SelectItemDialog(Context context) {
        super(context);
    }

    protected SelectItemDialog(Context context, int theme) {
        super(context, theme);
    }

    public interface IDialogSelectListener {
        void onSelect(String selectText, int position);
    }

    public static class Builder {
        private Context context;
        private ListView list_view;
        private View cancel_btn_tv;

        public Builder(Context context) {
            this.context = context;
        }

        public SelectItemDialog create(String[] itemTextList, int currentPosition,
                                       IDialogSelectListener listener) {
            return this.create(itemTextList, null, currentPosition, listener);
        }

        public SelectItemDialog create(String[] itemTextList, int[] textColors,
                                       int currentPosition, final IDialogSelectListener listener) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SelectItemDialog dialog = new SelectItemDialog(context, R.style.BaseSelectItemDialog);
            View layout = inflater.inflate(R.layout.base_dialog_item_select, null);
            dialog.setContentView(layout);
            cancel_btn_tv = layout.findViewById(R.id.cancel_btn_tv);
            cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            list_view = layout.findViewById(R.id.list_view);
            DialogListAdapter dialogListAdapter = new DialogListAdapter(context, itemTextList,
                    textColors, currentPosition, new SelectItemDialog.IDialogSelectListener() {
                @Override
                public void onSelect(String selectText, int position) {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onSelect(selectText, position);
                    }
                }
            });
            list_view.setAdapter(dialogListAdapter);

            return dialog;
        }

        public void scrollTo(int position) {
            list_view.smoothScrollToPosition(position);
        }
    }

    private static class DialogListAdapter extends BaseAdapter {
        private String[] data;
        private int[] colors;
        private Context context;
        private int currentPosition;
        private LayoutInflater mInflater = null;
        private IDialogSelectListener listener;

        public DialogListAdapter(Context context, String[] data, int currentPosition,
                                 IDialogSelectListener listener) {
            this(context, data, null, currentPosition, listener);
        }

        public DialogListAdapter(Context context, String[] data, int[] textColors,
                                 int currentPosition, IDialogSelectListener listener) {
            this.listener = listener;
            this.context = context;
            this.currentPosition = currentPosition;
            this.data = data;
            this.colors = textColors;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return data[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.base_item_select, null);
                holder = new ViewHolder();
                holder.item_tv = convertView.findViewById(R.id.item_tv);
                holder.line_rl = convertView.findViewById(R.id.line_rl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_tv.setText(data[position]);
            if (colors != null) {
                holder.item_tv.setTextColor(colors[position % colors.length]);
            }
            holder.line_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSelect(data[position], position);
                    currentPosition = position;
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public TextView item_tv;
            public RelativeLayout line_rl;
        }
    }
}
