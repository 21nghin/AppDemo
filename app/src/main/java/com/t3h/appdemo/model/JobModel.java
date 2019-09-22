package com.t3h.appdemo.model;

public class JobModel {
    private String title;
    private String intro;
    private String date;
    private String numberLike,numberComment,numberShare;

    public JobModel(String title, String intro, String date, String numberLike, String numberComment, String numberShare) {
        this.title = title;
        this.intro = intro;
        this.date = date;
        this.numberLike = numberLike;
        this.numberComment = numberComment;
        this.numberShare = numberShare;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public String getDate() {
        return date;
    }

    public String getNumberLike() {
        return numberLike;
    }

    public String getNumberComment() {
        return numberComment;
    }

    public String getNumberShare() {
        return numberShare;
    }
}
