package com.pine.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.widget.view.ThousandthNumberEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/2/12.
 */

public class InputTextDialog extends Dialog {
    public static int ACTION_CANCEL = 1;
    public static int ACTION_SUBMIT = 0;

    public InputTextDialog(Context context) {
        super(context);
    }

    public InputTextDialog(Context context, int theme) {
        super(context, theme);
    }

    protected InputTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface SubmitClickListener {
        void onSubmitClick(Dialog dialog, int which, List<String> list);
    }

    public static class Builder {
        private Context context;
        private OnClickListener cancelClickListener;
        private SubmitClickListener submitClickListener;
        private List<String> list = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
        }

        public OnClickListener getCancelClickListener() {
            return cancelClickListener;
        }


        public void setCancelClickListener(OnClickListener cancelClickListener) {
            this.cancelClickListener = cancelClickListener;
        }

        public SubmitClickListener getSubmitClickListener() {
            return submitClickListener;
        }

        public void setSubmitClickListener(SubmitClickListener submitClickListener) {
            this.submitClickListener = submitClickListener;
        }

        public Dialog create(String title) {
            return this.create(title, null, -1);
        }

        public Dialog create(String title, String originalText, final int inputMaxLength) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Dialog dialog = new Dialog(context, R.style.BaseInputTextDialogStyle);
            View layout = inflater.inflate(R.layout.base_dialog_text_input, null);
            TextView title_tv = layout.findViewById(R.id.title_tv);
            final EditText input_et = layout.findViewById(R.id.input_et);
            TextView cancel_btn_tv = layout.findViewById(R.id.cancel_btn_tv);
            TextView submit_btn_tv = layout.findViewById(R.id.submit_btn_tv);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if (cancelClickListener != null)
                cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelClickListener.onClick(dialog, ACTION_CANCEL);
                    }
                });
            if (submitClickListener != null)
                submit_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = new ArrayList<>();
                        list.add(input_et.getText().toString());
                        list.add(input_et.getText().toString());
                        submitClickListener.onSubmitClick(dialog, ACTION_SUBMIT, list);
                    }
                });
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideKeyboard(input_et);
                }
            });
            dialog.setContentView(layout);
            title_tv.setText(title);
            if (inputMaxLength > 0) {
                input_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Editable editable = input_et.getText();
                        int len = editable.length();
                        if (len > inputMaxLength) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取新字符串
                            String newStr = str.substring(0, inputMaxLength);
                            input_et.setText(newStr);
                            editable = input_et.getText();
                            //新字符串的长度
                            int newLen = editable.length();
                            //旧光标位置超过字符串长度
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editable, selEndIndex);
                            Toast.makeText(context, context.getString(R.string.base_text_max_length, inputMaxLength), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            if (!TextUtils.isEmpty(originalText)) {
                input_et.setText(originalText);
                input_et.setSelection(originalText.length());
            }
            return dialog;
        }

        /**
         * 创建对输入的类dialog
         *
         * @param title
         * @param originalText
         * @param inputMaxLength
         * @param inputType
         * @return
         */
        public Dialog create(String title, String originalText, final int inputMaxLength, int inputType) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Dialog dialog = new Dialog(context, R.style.BaseInputTextDialogStyle);
            View layout = inflater.inflate(R.layout.base_dialog_text_input, null);
            TextView title_tv = layout.findViewById(R.id.title_tv);
            final EditText input_et = layout.findViewById(R.id.input_et);
            TextView cancel_btn_tv = layout.findViewById(R.id.cancel_btn_tv);
            TextView submit_btn_tv = layout.findViewById(R.id.submit_btn_tv);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if (cancelClickListener != null)
                cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelClickListener.onClick(dialog, ACTION_CANCEL);
                    }
                });
            if (submitClickListener != null)
                submit_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = new ArrayList<>();
                        list.add(input_et.getText().toString());
                        list.add(input_et.getText().toString());
                        submitClickListener.onSubmitClick(dialog, ACTION_SUBMIT, list);
                    }
                });
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideKeyboard(input_et);
                }
            });
            dialog.setContentView(layout);
            input_et.setInputType(inputType);
            title_tv.setText(title);
            if (inputMaxLength > 0) {
                input_et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Editable editable = input_et.getText();
                        int len = editable.length();
                        if (len > inputMaxLength) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取新字符串
                            String newStr = str.substring(0, inputMaxLength);
                            input_et.setText(newStr);
                            editable = input_et.getText();
                            //新字符串的长度
                            int newLen = editable.length();
                            //旧光标位置超过字符串长度
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editable, selEndIndex);
                            Toast.makeText(context, context.getString(R.string.base_text_max_length, inputMaxLength), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            if (!TextUtils.isEmpty(originalText)) {
                input_et.setText(originalText);
                input_et.setSelection(originalText.length());
            }
            return dialog;
        }

        /**
         * 创建数字输入dialog
         */
        public Dialog thousandthNumberInputCreate(String title, String originalText, final int inputMaxLength, boolean allowDecimal, int decimalNum) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Dialog dialog = new Dialog(context, R.style.BaseInputTextDialogStyle);
            View layout = inflater.inflate(R.layout.base_dialog_thounsandth_number_text_input, null);
            TextView title_tv = layout.findViewById(R.id.title_tv);
            final EditText input_et = layout.findViewById(R.id.input_et);
            TextView cancel_btn_tv = layout.findViewById(R.id.cancel_btn_tv);
            TextView submit_btn_tv = layout.findViewById(R.id.submit_btn_tv);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if (cancelClickListener != null)
                cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelClickListener.onClick(dialog, ACTION_CANCEL);
                    }
                });
            if (submitClickListener != null)
                submit_btn_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list = new ArrayList<>();
                        list.add(input_et.getText().toString());
                        list.add(((ThousandthNumberEditText) input_et).getOriginalText());
                        submitClickListener.onSubmitClick(dialog, ACTION_SUBMIT, list);
                    }
                });
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideKeyboard(input_et);
                }
            });
            dialog.setContentView(layout);
            ((ThousandthNumberEditText) input_et).setDecimalAllow(allowDecimal);
            ((ThousandthNumberEditText) input_et).setDecimalNum(decimalNum);
            title_tv.setText(title);
            if (inputMaxLength > 0) {
                ((ThousandthNumberEditText) input_et).setNumberMaxLength(inputMaxLength, new ThousandthNumberEditText.MaxLengthOverflowListener() {
                    @Override
                    public void onLengthOverflow() {
                        Toast.makeText(context, context.getString(R.string.base_number_max_length, inputMaxLength), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (!TextUtils.isEmpty(originalText)) {
                input_et.setText(originalText);
                input_et.setSelection(originalText.length());
            }
            return dialog;
        }

        public void showKeyboard(EditText editText) {
            if (editText != null) {
                //设置可获得焦点
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                //请求获得焦点
                editText.requestFocus();
                //调用系统输入法

                InputMethodManager imm = (InputMethodManager) editText
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }

        public void hideKeyboard(EditText editText) {
            if (editText != null) {
                //设置可获得焦点
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
            }
        }
    }
}
