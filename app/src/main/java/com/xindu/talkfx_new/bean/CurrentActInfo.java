package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/5/29.
 */

public class CurrentActInfo {

    /**
     * profitability : -0.0321
     * summary : null
     * dealerName : XM
     * headImg : headImg/262300/1521444168.jpg
     * comprehensiveEvaluation : null
     * veracity : 0.3975
     * maxWithdrawlRate : 0.09
     * antiRiskAbility : 0.09
     * userName : 宝二代
     * levelTitle : null
     * replicability : 22364.0
     * concernStatus : null
     * attNoAchievementList : []
     * averageHoldingTime : 22364
     * riskRate : 0.2908
     * yield : -0.0321
     * concernCount : null
     * actualLeverage : 0.2908
     * customerId : 145844
     * acctType : 1
     * winRate : 0.3975
     * attAchievementList : [{"img":"staticImg/1526266921.png","achievementId":2,"title":"交易大师"}]
     */

    public double profitability;
    public Object summary;
    public String dealerName;
    public String headImg;
    public Object comprehensiveEvaluation;
    public double veracity;
    public double maxWithdrawlRate;
    public double antiRiskAbility;
    public String userName;
    public Object levelTitle;
    public double replicability;
    public Object concernStatus;
    public int averageHoldingTime;
    public double riskRate;
    public double yield;
    public Object concernCount;
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
