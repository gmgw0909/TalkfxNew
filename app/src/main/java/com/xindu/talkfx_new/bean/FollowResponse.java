package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/4/25.
 */

public class FollowResponse {

    /**
     * list : [{"user_name":"撒打算abc","head_img":"headImg/2/1522834533.jpeg"},{"user_name":"徐杰","head_img":"headImg/262300/1521444168.jpg"}]
     * listCount : 2
     */

    public int listCount;
    public List<UserInfo> list;
}
