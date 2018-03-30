package com.xindu.talkfx_new.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/31.
 */

public class CustomerResponse {

    private List<AchievementList> achievementList;
    private String authenInfor;
    private int columnCount;
    private List<ColumnInfo> columns;
    private int columnsCount;
    private int concernCount;
    private int concernStatus;
    private int concernUserCount;
    private String headImg;
    private List<LabelList> labelList;
    private String levelImg;
    private String levelTitle;
    private String summary;
    private String userName;

    public void setAchievementList(List<AchievementList> achievementList) {
        this.achievementList = achievementList;
    }

    public List<AchievementList> getAchievementList() {
        return achievementList;
    }

    public void setAuthenInfor(String authenInfor) {
        this.authenInfor = authenInfor;
    }

    public String getAuthenInfor() {
        return authenInfor;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public void setConcernCount(int concernCount) {
        this.concernCount = concernCount;
    }

    public int getConcernCount() {
        return concernCount;
    }

    public void setConcernStatus(int concernStatus) {
        this.concernStatus = concernStatus;
    }

    public int getConcernStatus() {
        return concernStatus;
    }

    public void setConcernUserCount(int concernUserCount) {
        this.concernUserCount = concernUserCount;
    }

    public int getConcernUserCount() {
        return concernUserCount;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setLabelList(List<LabelList> labelList) {
        this.labelList = labelList;
    }

    public List<LabelList> getLabelList() {
        return labelList;
    }

    public void setLevelImg(String levelImg) {
        this.levelImg = levelImg;
    }

    public String getLevelImg() {
        return levelImg;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    class LabelList {
        private String labelType;
        private String title;

        public void setLabelType(String labelType) {
            this.labelType = labelType;
        }

        public String getLabelType() {
            return labelType;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }

    class AchievementList {

        private int achievementId;
        private String dataStatus;
        private String img;
        private String title;

        public void setAchievementId(int achievementId) {
            this.achievementId = achievementId;
        }

        public int getAchievementId() {
            return achievementId;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }
}
