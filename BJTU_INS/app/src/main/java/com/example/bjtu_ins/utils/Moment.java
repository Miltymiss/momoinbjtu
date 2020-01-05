package com.example.bjtu_ins.utils;

public class Moment {
    private String name;
    private String pictureURL;
    private int likeNum;
    private String content;
    private String videoUrl;

    public Moment(String name,String pictureURL,int likeNum,String content){
        this.name = name;
        this.pictureURL = pictureURL;
        this.likeNum = likeNum;
        this.content = content;
    }

    public Moment(String name,String pictureURL,int likeNum,String content,String videoUrl){
        this.name = name;
        this.pictureURL = pictureURL;
        this.likeNum = likeNum;
        this.content = content;
        this.videoUrl = videoUrl;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPictureURL() {
        return pictureURL;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public int getLikeNum() {
        return likeNum;
    }
    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl){
        this.videoUrl = videoUrl;
    }
}
