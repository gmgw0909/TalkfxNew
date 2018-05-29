package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/29.
 */

public class CurrentActInfo {


    /**
     * profitability : 10
     * summary : null
     * dealerName : FxClub
     * headImg : headImg/262300/1521444168.jpg
     * comprehensiveEvaluation : 70.9166
     * veracity : 40
     * maxWithdrawlRate : 0.0066
     * antiRiskAbility : 20
     * userName : 张酷酷
     * levelTitle : null
     * replicability : 50
     * concernStatus : null
     * attNoAchievementList : []
     * averageHoldingTime : 912185
     * riskRate : 30
     * yield : 5.0E-4
     * concernCount : 8
     * actualLeverage : 0.0011
     * customerId : 207339
     * acctType : 1
     * winRate : 0.8168
     * attAchievementList : [{"img":"staticImg/1526266921.png","achievementId":2,"title":"交易大师"}]
     */

    public int profitability;
    public Object summary;
    public String dealerName;
    public String headImg;
    public double comprehensiveEvaluation;
    public int veracity;
    public double maxWithdrawlRate;
    public int antiRiskAbility;
    public String userName;
    public Object levelTitle;
    public int replicability;
    public Object concernStatus;
    public int averageHoldingTime;
    public int riskRate;
    public double yield;
    public int concernCount;
    public double actualLeverage;
    public int customerId;
    public String acctType;
    public double winRate;
    public List<?> attNoAchievementList;
    public List<AttAchievementListInfo> attAchievementList;

    public static class AttAchievementListInfo {
        /**
         * img : staticImg/1526266921.png
         * achievementId : 2
         * title : 交易大师
         */

        public String img;
        public int achievementId;
        public String title;
    }
}
