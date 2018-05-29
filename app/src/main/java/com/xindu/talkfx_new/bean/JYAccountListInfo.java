package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/28.
 */

public class JYAccountListInfo {

    /**
     * list : [{"isDefault":"1","serverStatus":null,"acctNo":"6045979","platformServer":"XM.COM-Real 6","name":"XM","tradeAcctId":25,"isSelf":"1","createDate":1527498251}]
     * listCount : 1
     */

    public int listCount;
    public List<ListInfo> list;

    public static class ListInfo {

        /**
         * isDefault : 1
         * acctNo : 6045979
         * platformServer : XM.COM-Real 6
         * dealerId : 2
         * customerId : 36407
         * tradeAcctId : 25
         * platform : MT4
         * isSelf : 1
         */

        public String isDefault;
        public String name;
        public String acctNo;
        public String createDate;
        public String platformServer;
        public int dealerId;
        public int customerId;
        public int tradeAcctId;
        public String platform;
        public String isSelf;
    }
}
