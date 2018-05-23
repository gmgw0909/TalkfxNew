package com.xindu.talkfx_new.bean;

/**
 * Created by LeeBoo on 2018/5/3.
 */

public class TVInfo {
    public long date;
    public double dailyChange;
    public double dailyPercentualChange;
    public double last;
    public double yearlyPercentualChange;
    public String name;
    public String symbol;
    public double weeklyPercentualChange;
    public double monthlyPercentualChange;
    public boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
