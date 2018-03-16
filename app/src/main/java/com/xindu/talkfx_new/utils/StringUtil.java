package com.xindu.talkfx_new.utils;

import java.text.ParseException;

/**
 * Created by LeeBoo on 2016/7/7.10:29
 */
public class StringUtil {
    private static String usernameRegular = "^[1][3578][0-9]{9}$";
    private static String pwdRegular = "^[^\\s]{6,18}$";
    private static String idCardRegular = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
    private static String bankNumRegular = "^(\\d{16}|\\d{19})$";
    private static String carNumRegular = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
    private static String carNumRegular_ = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{5}[A-Z0-9挂学警港澳]{1}$";

    //校验用户名格式
    public static boolean validatePhone(String phone) {

        return phone == null ? false : phone.matches(usernameRegular);
    }

    //校验车牌号格式
    public static boolean validateCarNum(String carNum) {

        return carNum == null ? false : carNum.matches(carNumRegular) || carNum.matches(carNumRegular_);
    }

    //校验密码格式
    public static boolean validatePwd(String pwd) {
        return pwd == null ? false : pwd.matches(pwdRegular);
    }

    //校验身份证格式
    public static boolean validateIdCard(String idCard) {
        //        return idCard == null ? false : idCard.matches(idCardRegular);
        try {
            return idCard == null ? false : IDValidator.IDCardValidate(idCard).equals("");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    //校验银行卡号格式
    public static boolean validateBankNum(String bankNum) {
        return bankNum == null ? false : bankNum.matches(bankNumRegular);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

}
