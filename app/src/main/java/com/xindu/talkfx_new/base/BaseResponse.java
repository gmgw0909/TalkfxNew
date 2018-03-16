package com.xindu.talkfx_new.base;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    public int code;
    public String msg;
    public T datas;
}