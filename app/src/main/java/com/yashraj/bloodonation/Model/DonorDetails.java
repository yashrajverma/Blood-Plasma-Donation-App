package com.yashraj.bloodonation.Model;

public class DonorDetails {
    private String user_firstname;
    private String user_lastname;
    private String user_bloodgroup;
    private String user_address;
    private String user_mobile;
    private String user_city;
    private String user_state;

    public DonorDetails() {
    }

    public DonorDetails(String user_firstname, String user_lastname, String user_bloodgroup, String user_address, String user_mobile, String user_city, String user_state) {
        this.user_firstname = user_firstname;
        this.user_bloodgroup = user_bloodgroup;
        this.user_address = user_address;
        this.user_mobile = user_mobile;
        this.user_city = user_city;
        this.user_state = user_state;
        this.user_lastname = user_lastname;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_bloodgroup() {
        return user_bloodgroup;
    }

    public void setUser_bloodgroup(String user_bloodgroup) {
        this.user_bloodgroup = user_bloodgroup;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }
}
