package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/4/11.
 */

public class UserInfo {
    /**
     * summary : null
     * headImg : headImg/36407/1523858934.png
     * authenInfor : 栏作者和交易大师22222
     * concernUserCount : 1
     * dataStatus : 3
     * columnCount : 0
     * userName : Talkfx_fmt30
     * levelTitle : null
     * levelImg : null
     * labelList : []
     * attNoAchievementList : [{"img":"staticImg/1524104942.png","achievementId":8,"title":"手机认证"}]
     * concernCount : 0
     * customerId : 36407
     * attAchievementList : []
     */

    public String summary;
    public String headImg;
    public String authenInfor;
    public int concernUserCount;
    public String dataStatus;
    public int columnCount;
    public String userName;
    public Object levelTitle;
    public Object levelImg;
    public int concernCount;
    public int customerId;
    public List<?> labelList;
    public List<AttAchievementInfo> attNoAchievementList;
    public List<AttAchievementInfo> attAchievementList;
    public int concernStatus;
}
