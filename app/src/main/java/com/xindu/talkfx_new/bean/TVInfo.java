package com.xindu.talkfx_new.bean;

/**
 * Created by LeeBoo on 2018/5/3.
 */

public class TVInfo {
    public TVInfo(String a, String b, String c, String d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String a;
    public String b;
    public String c;
    public String d;
    public boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
