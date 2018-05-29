package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/28.
 */

public class PingCangInfo {

    public int listCount;
    public List<PCInfo> list;

    public static class PCInfo {


        /**
         * time1 : 1525017911
         * symbol : nzdchf
         * size : 0.01
         * sl : 0
         * holdPrice : 0
         * type : buy limit
         * tp : 0
         * price1 : 1
         * holdTime : 14389
         * price2 : 1
         */

        public int time1;
        public String symbol;
        public double size;
        public int sl;
        public int holdPrice;
        public String type;
        public int tp;
        public int price1;
        public int holdTime;
        public int price2;
    }
}
