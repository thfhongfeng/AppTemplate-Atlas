package com.pine.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.pine.tool.R;

/**
 * Created by tanghongfeng on 2018/9/18
 */

public class ThousandthNumberTextView extends android.support.v7.widget.AppCompatTextView {

    // 千分位分隔符
    private String mSpaceChar = ",";
    private AfterTextChangedListener mAfterTextChangedListener = null;
    // 是否允许小数位
    private boolean mDecimalAllow;
    // 小数位数
    private int mDecimalNum;
    // 非小数位最大允许位数
    private int mNumberMaxLength;

    public ThousandthNumberTextView(Context context) {
        super(context);
    }

    public ThousandthNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThousandthNumberTextView);
        mDecimalNum = typedArray.getInteger(R.styleable.ThousandthNumberTextView_textView_decimalNum, 2);
        mNumberMaxLength = typedArray.getInteger(R.styleable.ThousandthNumberTextView_textView_numberMaxLength, 99);
        mDecimalAllow = typedArray.getBoolean(R.styleable.ThousandthNumberTextView_textView_decimalAllow, false);
        mSpaceChar = typedArray.getString(R.styleable.ThousandthNumberTextView_textView_spaceChar);
        if (TextUtils.isEmpty(mSpaceChar)) {
            mSpaceChar = ",";
        }
        if (mDecimalAllow) {
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        textListener();
    }

    public ThousandthNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void textListener() {
        this.addTextChangedListener(new TextWatcher() {
            private boolean isChange = false;
            private int lastLength = 0;
            private String lastText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastLength = s.length();
                lastText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (!isChange) {
                    if (lastLength < length) {
                        String currentText = s.toString().replace(mSpaceChar, "");
                        String[] splitArr = currentText.split("\\.", -1);
                        if (splitArr[0].length() > mNumberMaxLength) {
                            setText(lastText);
                            return;
                        }
                        isChange = true;
                        String str = addSpace(s.toString());
                        setText(str);
                    } else {
                        isChange = true;
                        String currentStr = s.toString();
                        String str = addSpace(currentStr);
                        setText(str);
                    }
                } else {
                    isChange = false;
                }
            }

            private String addSpace(String currentText) {
                StringBuffer sb = new StringBuffer("");
                currentText = currentText.toString().replace(mSpaceChar, "");
                String[] splitArr = currentText.split("\\.", -1);
                char[] charArray = splitArr[0].toCharArray();
                int startCount = charArray.length % 3;
                for (int i = 0; i < charArray.length; i++) {
                    if ((i - startCount) % 3 == 0 && i != 0) {
                        sb.append(mSpaceChar);
                    }
                    sb.append(charArray[i]);
                }
                if (splitArr.length > 1 && mDecimalAllow) {
                    sb.append(".").append(splitArr[1].length() > mDecimalNum ? splitArr[1].substring(0, mDecimalNum) : splitArr[1]);
                }
                return sb.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isChange) {
                    if (mAfterTextChangedListener != null) {
                        mAfterTextChangedListener.afterTextChanged(s.toString().replace(mSpaceChar, ""));
                    }
                }
            }
        });
    }

    public void setDecimalAllow(boolean decimalAllow) {
        mDecimalAllow = decimalAllow;
        if (mDecimalAllow) {
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public void setDecimalNum(int decimalNum) {
        mDecimalNum = decimalNum;
    }

    public void setNumberMaxLength(int numberMaxLength) {
        mNumberMaxLength = numberMaxLength;
    }

    public void setSpaceChar(String spaceChar) {
        mSpaceChar = spaceChar;
    }

    public void setAfterTextChangedListener(AfterTextChangedListener listener) {
        mAfterTextChangedListener = listener;
    }

    public String getOriginalText() {
        return getText().toString().replace(mSpaceChar, "");
    }

    /**
     * 定义一个回调监听接口，在完成输入时返回格式化好的输入文本
     */
    public interface AfterTextChangedListener {
        void afterTextChanged(String text);
    }
}
