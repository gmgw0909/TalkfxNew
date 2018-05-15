package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/11.
 */

public class CalendarsInfo {

    /**
     * groupDate : 2018-05-15
     * list : [{"dateTime":1526320800000,"country":"China","actual":"","previous":"10.1%","teforecast":"9.8%","importance":2,"forecast":"10%","event":"Retail Sales YoY"},{"dateTime":1526320800000,"country":"China","actual":"","previous":"6%","teforecast":"6.8%","importance":2,"forecast":"6.3%","event":"Industrial Production YoY"},{"dateTime":1526320800000,"country":"China","actual":"","previous":"7.5%","teforecast":"7.4%","importance":2,"forecast":"7.4%","event":"Fixed Asset Investment (YTD) YoY"}]
     */

    public String groupDate;
    public List<ListInfo> list;

    public static class ListInfo {
        /**
         * dateTime : 1526320800000
         * country : China
         * actual :
         * previous : 10.1%
         * teforecast : 9.8%
         * importance : 2
         * forecast : 10%
         * event : Retail Sales YoY
         */

        public long dateTime;
        public String country;
        public String actual;
        public String previous;
        public String teforecast;
        public int importance;
        public String forecast;
        public String event;
    }
}
