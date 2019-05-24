package com.example.mariuspop.catalog3.models.mesaje;

import java.util.Date;

public class InboxMessage {

    private long elevId;
    private long materieId;
    private String message;
    private String from;
    private String to;
    private Date date;
    private boolean isProf;

    public long getElevId() {
        return elevId;
    }

    public void setElevId(long elevId) {
        this.elevId = elevId;
    }

    public long getMaterieId() {
        return materieId;
    }

    public void setMaterieId(long materieId) {
        this.materieId = materieId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isProf() {
        return isProf;
    }

    public void setProf(boolean prof) {
        isProf = prof;
    }
}
