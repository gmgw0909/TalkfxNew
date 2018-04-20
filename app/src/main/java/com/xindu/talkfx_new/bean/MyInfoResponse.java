package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/4/20.
 */

public class MyInfoResponse {
    /**
     * dynamicsList : [{"currencyPair":null,"headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":458,"firstImg":"resources/images/default.png","typeTitle":"业内新闻","title":"pic","readCount":119,"userName":"撒打算abc","commentCount":1,"miniContent":"","callSingle":"多","customerId":2,"tactful":"观望","createDate":1523166787,"collectStatus":null},{"currencyPair":"AUDEUR","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":442,"firstImg":"","typeTitle":"业内新闻","title":"dsfdsds","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"sdfdsfsdfsdfs","callSingle":"多","customerId":2,"tactful":"观望","createDate":1521610621,"collectStatus":null},{"currencyPair":"AUDEUR","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":441,"firstImg":"","typeTitle":"分析","title":"dsfdsds","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"sdfdsfsdfsdfs","callSingle":"多","customerId":2,"tactful":"强烈空","createDate":1521610606,"collectStatus":null},{"currencyPair":"AUDCAD","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":440,"firstImg":null,"typeTitle":"教学","title":"wsqsadsa","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"dsadsadsafddsfds","callSingle":"多","customerId":2,"tactful":"强烈空","createDate":1521599330,"collectStatus":null},{"currencyPair":"AUDCAD","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":439,"firstImg":null,"typeTitle":"教学","title":"13123","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"21312312321","callSingle":"多","customerId":2,"tactful":"观望","createDate":1521598732,"collectStatus":null},{"currencyPair":"AUDCAD","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":438,"firstImg":"","typeTitle":"教学","title":"13123","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"21312312321","callSingle":"多","customerId":2,"tactful":"偏多","createDate":1521598715,"collectStatus":null},{"currencyPair":"AUDCHF","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":437,"firstImg":null,"typeTitle":"教学","title":"dsadsadsa","readCount":1,"userName":"撒打算abc","commentCount":0,"miniContent":"dasdasdas","callSingle":"多","customerId":2,"tactful":"强烈空","createDate":1521598432,"collectStatus":null},{"currencyPair":"AUDJPY","headImg":"headImg/2/1522834533.jpeg","abstracts":null,"columnId":436,"firstImg":"","typeTitle":"教学","title":"dsadsa","readCount":null,"userName":"撒打算abc","commentCount":0,"miniContent":"dsadasdas","callSingle":"多","customerId":2,"tactful":"偏空","createDate":1521598310,"collectStatus":null}]
     * user : {"summary":null,"headImg":"headImg/36407/1523858934.png","authenInfor":"栏作者和交易大师22222","concernUserCount":1,"dataStatus":"3","columnCount":0,"userName":"Talkfx_fmt30","levelTitle":null,"levelImg":null,"labelList":[],"attNoAchievementList":[{"img":"staticImg/1524104942.png","achievementId":8,"title":"手机认证"}],"concernCount":0,"customerId":36407,"attAchievementList":[]}
     * dynamicsListCount : 41
     */
    public UserInfo user;
    public int dynamicsListCount;
    public List<DynamicsInfo> dynamicsList;
}
