package com.t3h.appdemo.model;

public class Comments {
    private String cId;
    private String comment;
    private String timeStamp;
    private String uid;
    private String uEmail;
    private String uImageUrl;
    private String uName;

    public Comments() {

    }

    public Comments(String cId, String comment, String timeStamp, String uid, String uEmail, String uImageUrl, String uName) {
        this.cId = cId;
        this.comment = comment;
        this.timeStamp = timeStamp;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uImageUrl = uImageUrl;
        this.uName = uName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
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
