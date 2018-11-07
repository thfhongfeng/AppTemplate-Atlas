package com.pine.base.architecture.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.text.TextUtils;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.tool.util.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public abstract class BasePresenter<V extends IBaseContract.Ui> {
    public final static int UI_STATE_ON_CREATE = 1;
    public final static int UI_STATE_ON_START = 2;
    public final static int UI_STATE_ON_RESUME = 3;
    public final static int UI_STATE_ON_PAUSE = 4;
    public final static int UI_STATE_ON_STOP = 5;
    public final static int UI_STATE_ON_DETACH = 6;
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
    /**
     * UI的弱引用
     */
    private WeakReference<V> mUiRef;

    /**
     * 关联UI
     *
     * @param ui
     */
    @CallSuper
    public void attachUi(V ui) {
        mUiRef = new WeakReference<V>(ui);
    }

    /**
     * 解除关联
     */
    @CallSuper
    public void detachUi() {
        if (mUiRef != null) {
            onUiState(BasePresenter.UI_STATE_ON_DETACH);
            mUiRef.clear();
        }
    }

    /**
     * 得到UI
     *
     * @return
     */
    public V getUi() {
        return mUiRef.get();
    }

    /**
     * 得到Application
     *
     * @return
     */
    public Application getApplication() {
        return BaseApplication.mApplication;
    }

    /**
     * 得到Activity
     *
     * @return
     */
    public Activity getActivity() {
        return getUi().getContextActivity();
    }

    /**
     * 得到Context
     *
     * @return
     */
    public Context getContext() {
        return getUi().getContextActivity();
    }

    /**
     * 得到Intent
     *
     * @return
     */
    public Intent getIntent() {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent();
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getActivity().getIntent();
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getActivity().getIntent();
            }
        }
        return new Intent();
    }

    public final boolean isUiAlive() {
        if (mUiRef.get() == null) {
            return false;
        }
        if (mUiRef.get() instanceof Activity) {
            return !((Activity) mUiRef.get()).isFinishing();
        }
        return true;
    }

    public final void finishUi() {
        if (mUiRef.get() != null && mUiRef.get() instanceof Activity) {
            ((Activity) mUiRef.get()).finish();
        }
    }

    public abstract boolean parseIntentDataOnCreate();

    public abstract void onUiState(int state);

    /**
     * 得到Bundle
     *
     * @return
     */
    public Bundle getBundleExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getBundleExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getBundle(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getBundle(key);
            }
        }
        return null;
    }

    public String getStringExtra(String key, String defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                if (TextUtils.isEmpty(((Activity) mUiRef.get()).getIntent().getStringExtra(key))) {
                    return defaultValue;
                }
                return ((Activity) mUiRef.get()).getIntent().getStringExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getString(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getString(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public String[] getStringArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getStringArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getStringArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getStringArray(key);
            }
        }
        return null;
    }

    public ArrayList<String> getStringArrayListExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getStringArrayListExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getStringArrayList(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getStringArrayList(key);
            }
        }
        return null;
    }

    public int getIntExtra(String key, int defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getIntExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getInt(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getInt(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public int[] getIntArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getIntArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getIntArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getIntArray(key);
            }
        }
        return null;
    }

    public ArrayList<Integer> getIntegerArrayListExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getIntegerArrayListExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getIntegerArrayList(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getIntegerArrayList(key);
            }
        }
        return null;
    }

    public boolean getBooleanExtra(String key, boolean defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getBooleanExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getBoolean(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getBoolean(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public boolean[] getBooleanArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getBooleanArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getBooleanArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getBooleanArray(key);
            }
        }
        return null;
    }

    public byte getByteExtra(String key, byte defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getByteExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getByte(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getByte(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public byte[] getByteArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getByteArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getByteArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getByteArray(key);
            }
        }
        return null;
    }

    public char getCharExtra(String key, char defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getCharExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getChar(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getChar(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public char[] getCharArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getCharArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getCharArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getCharArray(key);
            }
        }
        return null;
    }

    public float getFloatExtra(String key, float defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getFloatExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getFloat(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getFloat(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public float[] getFloatArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getFloatArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getFloatArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getFloatArray(key);
            }
        }
        return null;
    }

    public double getDoubleExtra(String key, double defaultValue) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getDoubleExtra(key, defaultValue);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((Fragment) mUiRef.get()).getArguments().getDouble(key, defaultValue);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        defaultValue : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getDouble(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public double[] getDoubleArrayExtra(String key) {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent().getDoubleArrayExtra(key);
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((Fragment) mUiRef.get()).getArguments().getDoubleArray(key);
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getArguments() == null ?
                        null : ((android.support.v4.app.Fragment) mUiRef.get()).getArguments().getDoubleArray(key);
            }
        }
        return null;
    }
}
