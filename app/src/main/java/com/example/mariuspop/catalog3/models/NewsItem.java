package com.example.mariuspop.catalog3.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NewsItem {

    private String text;
    private long materieId;
    private String materieNume;
    private Date date;

    public NewsItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMaterieId() {
        return materieId;
    }

    public void setMaterieId(long materieId) {
        this.materieId = materieId;
    }

    public String getMaterieNume() {
        return materieNume;
    }

    public void setMaterieNume(String materieNume) {
        this.materieNume = materieNume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
