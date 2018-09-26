package com.pine.base.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.pine.base.R;

/**
 * Created by tanghongfeng on 2018/9/18
 */

public class ThousandthNumberTextView extends android.support.v7.widget.AppCompatTextView {

    private String spaceChar = ",";
    private MaxLengthOverflowListener maxLengthOverflowListener = null;
    private AfterTextChangedListener listener = null;
    private int decimalNum;
    private int numberMaxLength;
    private boolean decimalAllow;

    public ThousandthNumberTextView(Context context) {
        super(context);
    }

    public ThousandthNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThousandthNumberTextView);
        decimalNum = typedArray.getInteger(R.styleable.ThousandthNumberTextView_textView_decimalNum, 2);
        numberMaxLength = typedArray.getInteger(R.styleable.ThousandthNumberTextView_textView_numberMaxLength, 99);
        decimalAllow = typedArray.getBoolean(R.styleable.ThousandthNumberTextView_textView_decimalAllow, false);
        spaceChar = typedArray.getString(R.styleable.ThousandthNumberTextView_textView_spaceChar);
        if (TextUtils.isEmpty(spaceChar)) {
            spaceChar = ",";
        }
        if (decimalAllow) {
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
                    int selectIndex = getSelectionEnd();
                    if (lastLength < length) {
                        String currentText = s.toString().replace(spaceChar, "");
                        String[] splitArr = currentText.split("\\.", -1);
                        if (splitArr[0].length() > numberMaxLength) {
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
                currentText = currentText.toString().replace(spaceChar, "");
                String[] splitArr = currentText.split("\\.", -1);
                char[] charArray = splitArr[0].toCharArray();
                int startCount = charArray.length % 3;
                for (int i = 0; i < charArray.length; i++) {
                    if ((i - startCount) % 3 == 0 && i != 0) {
                        sb.append(spaceChar);
                    }
                    sb.append(charArray[i]);
                }
                if (splitArr.length > 1 && decimalAllow) {
                    sb.append(".").append(splitArr[1].length() > decimalNum ? splitArr[1].substring(0, decimalNum) : splitArr[1]);
                }
                return sb.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isChange) {
                    // 对回调方法进行调用，使监听回调的地方得到当前文本框中格式化以后的字符串结果
                    if (listener != null) {
                        listener.afterTextChanged(s.toString().replace(spaceChar, ""));
                    }
                }
            }
        });
    }

    public void setDecimalAllow(boolean decimalAllow) {
        this.decimalAllow = decimalAllow;
        if (decimalAllow) {
            setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    public void setDecimalNum(int decimalNum) {
        this.decimalNum = decimalNum;
    }

    public void setNumberMaxLength(int numberMaxLength, MaxLengthOverflowListener lengthOverflowListener) {
        this.numberMaxLength = numberMaxLength;
        this.maxLengthOverflowListener = lengthOverflowListener;
    }

    public void setSpaceChar(String spaceChar) {
        this.spaceChar = spaceChar;
    }

    public void setAfterTextChangedListener(AfterTextChangedListener listener) {
        this.listener = listener;
    }

    public void setMaxLengthOverflowListener(MaxLengthOverflowListener listener) {
        this.maxLengthOverflowListener = listener;
    }

    public String getOriginalText() {
        return getText().toString().replace(spaceChar, "");
    }

    /**
     * 定义一个回调监听接口，在完成输入时返回格式化好的输入文本
     */
    public interface AfterTextChangedListener {
        void afterTextChanged(String text);
    }

    public interface MaxLengthOverflowListener {
        void onLengthOverflow();
    }
}
