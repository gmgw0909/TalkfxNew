package com.xindu.talkfx_new.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xindu.talkfx_new.base.App;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 保存信息配置类
 * Created by LeeBoo on 2018/3/15.
 */

public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 简易保存和读取(ApplicationContext)
     */

    public static void put(String key, String value) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void put(String key, int value) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void put(String key, boolean value) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static void put(String key, float value) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public static void put(String key, long value) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static String getString(String key) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static Long getLong(String key) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, -1);
    }

    public static Integer getInt(String key) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static Float getFloat(String key) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, -1);
    }

    public static Boolean getBoolean(String key, boolean def) {
        SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }


    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
