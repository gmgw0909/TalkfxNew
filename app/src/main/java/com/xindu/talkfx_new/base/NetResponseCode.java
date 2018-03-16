package com.xindu.talkfx_new.base;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public enum NetResponseCode {

    登录成功("000203", "登录成功"),
    验证码已发送("000002", "验证码已发送,请注意查收"),
    用户名或密码错误("010002", "用户名或密码错误");

    private String value;
    private String code;

    NetResponseCode(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getName(String code) {
        for (NetResponseCode c : NetResponseCode.values()) {
            if (c.getCode().equals(code)) {
                return c.value;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
