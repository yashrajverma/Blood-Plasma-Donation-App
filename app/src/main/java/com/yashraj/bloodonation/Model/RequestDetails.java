package com.yashraj.bloodonation.Model;

public class RequestDetails {
    private String name;
    private String mobile;
    private String date;
    private String description;
    private String city;
    private String bloodgroup;

    public RequestDetails(String name, String mobile, String date, String description, String city, String bloodgroup) {
        this.name = name;
        this.mobile = mobile;
        this.date = date;
        this.description = description;
        this.city = city;
        this.bloodgroup = bloodgroup;
    }
    public RequestDetails(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }
}
