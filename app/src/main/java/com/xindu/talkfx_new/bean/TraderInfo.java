package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/25.
 */

public class TraderInfo {

    public List<DealerInfo> dealer;
    public List<String> platform;

    public static class DealerInfo {
        /**
         * dealerName : FxClub
         * dealerId : 1
         */

        public String dealerName;
        public int dealerId;
    }
}
