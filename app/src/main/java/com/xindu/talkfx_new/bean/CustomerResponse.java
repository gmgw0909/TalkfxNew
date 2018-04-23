package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/31.
 */

public class CustomerResponse {
    /**
     * columns : []
     * columnsCount : 0
     * user : {"summary":null,"headImg":null,"authenInfor":null,"concernUserCount":1,"dataStatus":"3","columnCount":null,"userName":"Talkfx_l04h6","levelTitle":null,"levelImg":null,"labelList":[],"attNoAchievementList":[{"img":"staticImg/1524104942.png","achievementId":8,"title":"手机认证"}],"concernStatus":"1","concernCount":null,"customerId":36424,"attAchievementList":[]}
     */
    public int columnsCount;
    public UserInfo user;
    public List<ColumnInfo> columns;
}
