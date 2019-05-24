package com.example.mariuspop.catalog3.models;

import java.util.Date;

public class Announcement {

    private long clasaId;
    private String text;
    private Date date;

    public long getClasaId() {
        return clasaId;
    }

    public void setClasaId(long clasaId) {
        this.clasaId = clasaId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
