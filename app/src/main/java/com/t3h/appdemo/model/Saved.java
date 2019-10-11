package com.t3h.appdemo.model;

public class Saved {

    private String pComments;
    private String pDateNow;
    private String pId;
    private String pImage;
    private String pIntroductJob;
    private String pLikes;
    private String pRecruitTime;
    private String pTile;
    private String uImageUrl;
    private String uName;

    public Saved() {
    }

    public Saved(String pComments, String pDateNow, String pId, String pImage, String pIntroductJob, String pLikes, String pRecruitTime, String pTile, String uImageUrl, String uName) {
        this.pComments = pComments;
        this.pDateNow = pDateNow;
        this.pId = pId;
        this.pImage = pImage;
        this.pIntroductJob = pIntroductJob;
        this.pLikes = pLikes;
        this.pRecruitTime = pRecruitTime;
        this.pTile = pTile;
        this.uImageUrl = uImageUrl;
        this.uName = uName;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    public String getpDateNow() {
        return pDateNow;
    }

    public void setpDateNow(String pDateNow) {
        this.pDateNow = pDateNow;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpIntroductJob() {
        return pIntroductJob;
    }

    public void setpIntroductJob(String pIntroductJob) {
        this.pIntroductJob = pIntroductJob;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getpRecruitTime() {
        return pRecruitTime;
    }

    public void setpRecruitTime(String pRecruitTime) {
        this.pRecruitTime = pRecruitTime;
    }

    public String getpTile() {
        return pTile;
    }

    public void setpTile(String pTile) {
        this.pTile = pTile;
    }

    public String getuImageUrl() {
        return uImageUrl;
    }

    public void setuImageUrl(String uImageUrl) {
        this.uImageUrl = uImageUrl;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
