package com.example.mariuspop.catalog3.models;

public class SMSGateway {

    private String phoneNo;
    private String firebaseToken;

    public SMSGateway() {
    }

    public SMSGateway(String phoneNo, String firebaseToken) {
        this.phoneNo = phoneNo;
        this.firebaseToken = firebaseToken;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
