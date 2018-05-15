package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/11.
 */

public class CurrencyResponse {
    public List<ListInfo> currencys;

    public static class ListInfo {
        public String group;
        public List<TVInfo> list;
    }
}
