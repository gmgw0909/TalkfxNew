package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/9.
 */

public class ColumnDetailResponse {

    /**
     * comments : []
     * commentsCount : 0
     * column : {"columnId":459,"cycleStart":null,"firstImg":"data/resource/showImg?path=boxImg/142/1523166815.png","takeprofit":null,"title":"5555","readCount":19,"viewStatus":null,"content":"<p><img alt=\"image.png\" class=\"\" src=\"data/resource/showImg?path=boxImg/142/1523166815.png\" width=\"821\" height=\"auto\" data-image-size=\"821,300\"><br><\/p><p>共产党万岁<\/p>","callSingle":"多","customerId":142,"tactful":"观望","createDate":1523166848,"currencyPair":null,"cycleEnd":null,"supportCount":null,"abstracts":null,"admireCount":null,"typeTitle":"分析","opinion":"5555","commentCount":null,"openPosition":null,"operate":null,"opposeCount":null,"stopLoss":null,"status":"0","collectStatus":"1"}
     * user : {"summary":"简单点","headImg":"headImg/262300/1521444168.jpg","authenInfor":"大媒体\n超级大","concernUserCount":6,"dataStatus":"0","columnCount":21,"userName":"徐杰","levelTitle":null,"levelImg":null,"labelList":[],"attNoAchievementList":[{"img":"staticImg/1524104987.png","achievementId":3,"title":"实名认证"},{"img":"staticImg/1524104942.png","achievementId":8,"title":"手机认证"}],"concernStatus":"1","concernCount":4,"customerId":142,"attAchievementList":[]}
     */

    public int commentsCount;
    public ColumnInfo column;
    public UserInfo user;
    public List<CommentInfo> comments;
}
