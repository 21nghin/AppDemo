package com.t3h.appdemo.model;

public class CmtModel {
    private String userName;
    private String date;
    private String cmt;

    public CmtModel(String userName, String date, String cmt) {
        this.userName = userName;
        this.date = date;
        this.cmt = cmt;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getCmt() {
        return cmt;
    }


}
