package com.yashraj.bloodonation.Model;

public class DonorDetails {
    private String donor_name;
    private String donor_blood_group;
    private String donor_address;
    private String donor_mobile;
    private String donor_city;
    private String donor_state;

    public DonorDetails() {
    }

    public DonorDetails(String donor_name, String donor_blood_group, String donor_address, String donor_mobile, String donor_city, String donor_state) {
        this.donor_name = donor_name;
        this.donor_blood_group = donor_blood_group;
        this.donor_address = donor_address;
        this.donor_mobile = donor_mobile;
        this.donor_city = donor_city;
        this.donor_state = donor_state;
    }

    public String getDonor_name() {
        return donor_name;
    }

    public void setDonor_name(String donor_name) {
        this.donor_name = donor_name;
    }

    public String getDonor_blood_group() {
        return donor_blood_group;
    }

    public void setDonor_blood_group(String donor_blood_group) {
        this.donor_blood_group = donor_blood_group;
    }

    public String getDonor_address() {
        return donor_address;
    }

    public void setDonor_address(String donor_address) {
        this.donor_address = donor_address;
    }

    public String getDonor_mobile() {
        return donor_mobile;
    }

    public void setDonor_mobile(String donor_mobile) {
        this.donor_mobile = donor_mobile;
    }

    public String getDonor_city() {
        return donor_city;
    }

    public void setDonor_city(String donor_city) {
        this.donor_city = donor_city;
    }

    public String getDonor_state() {
        return donor_state;
    }

    public void setDonor_state(String donor_state) {
        this.donor_state = donor_state;
    }
}
