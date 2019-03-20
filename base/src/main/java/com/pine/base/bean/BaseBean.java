package com.pine.base.bean;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseBean {
    public HashMap<String, String> toMapJson() {
        HashMap<String, String> map = new HashMap<>();
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    map.put(field.getName(), "");
                } else if (obj instanceof BaseBean) {
                    map.putAll(((BaseBean) obj).toMapJson());
                } else if (isBaseType(obj)) {
                    map.put(field.getName(), String.valueOf(obj));
                } else if (obj instanceof Map) {
                    map.putAll((Map) obj);
                } else {
                    map.put(field.getName(), new Gson().toJson(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public HashMap<String, String> toMapJsonIgnoreEmpty() {
        HashMap<String, String> map = new HashMap<>();
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                field.setAccessible(true);
                Object obj = field.get(this);
                if (isNullObj(obj)) {
                    continue;
                }
                if (obj instanceof BaseBean) {
                    map.putAll(((BaseBean) obj).toMapJsonIgnoreEmpty());
                } else if (isBaseType(obj)) {
                    map.put(field.getName(), String.valueOf(obj));
                } else if (obj instanceof Map) {
                    map.putAll((Map) obj);
                } else {
                    map.put(field.getName(), new Gson().toJson(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean isBaseType(Object obj) {
        return obj instanceof Byte || obj instanceof Integer || obj instanceof Short ||
                obj instanceof Long || obj instanceof Float || obj instanceof Double ||
                obj instanceof String || obj instanceof CharSequence;
    }

    public boolean isNullObj(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Byte) {
            if ((byte) obj == Byte.MAX_VALUE) {
                return true;
            }
        } else if (obj instanceof Integer) {
            if ((int) obj == Integer.MAX_VALUE) {
                return true;
            }
        } else if (obj instanceof Short) {
            if ((short) obj == Short.MAX_VALUE) {
                return true;
            }
        } else if (obj instanceof Long) {
            if ((long) obj == Long.MAX_VALUE) {
                return true;
            }
        } else if (obj instanceof Float) {
            if ((float) obj == Float.MAX_VALUE) {
                return true;
            }
        } else if (obj instanceof Double) {
            if ((double) obj == Double.MAX_VALUE) {
                return true;
            }
        }
        return TextUtils.isEmpty(obj.toString());
    }
}
