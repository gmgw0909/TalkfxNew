package com.xindu.talkfx_new.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LeeBoo on 2018/3/9.
 */

public class CommentInfo implements Serializable{
    public List<CommentInfo> childList;
    public int columnId;
    public String commentCount;
    public int commentId;
    public String content;
    public String createDate;
    public int fromUserId;
    public String fromUserImg;
    public String fromUserName;
    public String lz;
    public String opposeCount;
    public String supportCount;
    public String toUserId;
    public String toUserName;
    public String type;
}
