package com.t3h.appdemo.model;

import com.google.firebase.database.Exclude;

public class JobModel {

    private String id;
    private String image;
    private String title;
    private String introduceJob;
    private String companyAddress;
    private String jobTime;
    private String companyEmail;
    private String someCompanyInformation;
    private String infomationJob;
    private String recruiTime;
//    private String date;

    private String numberLike,numberComment,numberShare;

    public JobModel() {
    }

    public JobModel(String image, String title, String introduceJob, String companyAddress, String jobTime, String companyEmail, String someCompanyInformation, String infomationJob, String recruiTime) {
        this.image = image;
        this.title = title;
        this.introduceJob = introduceJob;
        this.companyAddress = companyAddress;
        this.jobTime = jobTime;
        this.companyEmail = companyEmail;
        this.someCompanyInformation = someCompanyInformation;
        this.infomationJob = infomationJob;
        this.recruiTime = recruiTime;
//        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduceJob() {
        return introduceJob;
    }

    public void setIntroduceJob(String introduceJob) {
        this.introduceJob = introduceJob;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getSomeCompanyInformation() {
        return someCompanyInformation;
    }

    public void setSomeCompanyInformation(String someCompanyInformation) {
        this.someCompanyInformation = someCompanyInformation;
    }

    public String getInfomationJob() {
        return infomationJob;
    }

    public void setInfomationJob(String infomationJob) {
        this.infomationJob = infomationJob;
    }

    public String getRecruiTime() {
        return recruiTime;
    }

    public void setRecruiTime(String recruiTime) {
        this.recruiTime = recruiTime;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }
}
