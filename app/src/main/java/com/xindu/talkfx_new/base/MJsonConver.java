package com.xindu.talkfx_new.base;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;

/**
 * JSON与对象转换工具
 */

public class MJsonConver {

    protected MJsonConver() {

    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * 把数据流转换成对象 Type
     */
    public static <T> T fromJosn(JsonReader mJsonReader, Type mType) {
        Gson gson = new Gson();
        return gson.fromJson(mJsonReader, mType);
    }

    /**
     * 把数据流转换成对象 Class
     */
    public static <T> T fromJosn(JsonReader mJsonReader, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(mJsonReader, type);
    }
}
