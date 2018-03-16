package com.xindu.talkfx_new.base;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class MJsonCallBack<T> extends AbsCallback<T> {

    /*类的类型抽象*/private Type type;

    /*类类型*/private Class<T> clazz;

    /**
     * 无参情况
     */
    public MJsonCallBack() {

    }

    /**
     * 构造传递泛型-集合
     */
    public MJsonCallBack(Type type) {
        this.type = type;
    }

    /**
     * 构造传递泛型-通用对象
     */
    public MJsonCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }


    /**
     * 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
     *
     * @param response 需要转换的对象
     * @return 转换后的结果
     * @throws Exception 转换过程发生的异常
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        /**结果集*/ResponseBody body = response.body();

        if (body == null) {
            return null;
        }

        /**解析后的对象*/T data = null;

        /**JSON读取流-直接把流转换为Josn*/JsonReader jsonReader = new JsonReader(body.charStream());

        if (type != null) {
            data = MJsonConver.fromJosn(jsonReader, type);
        } else if (clazz != null) {
            data = MJsonConver.fromJosn(jsonReader, clazz);
        } else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            data = MJsonConver.fromJosn(jsonReader, type);
        }

        return data;
    }
}
