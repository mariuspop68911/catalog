package com.example.mariuspop.catalog3.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Absenta implements Serializable {

    private long absentaId;
    private long elevId;
    private long materieId;
    private String materieNume;
    private Date data;
    private boolean motivata;
    private boolean pending;
    private boolean seen;

    public Absenta() {
        absentaId = Calendar.getInstance().getTimeInMillis();
    }

    public long getAbsentaId() {
        return absentaId;
    }

    public void setAbsentaId(long absentaId) {
        this.absentaId = absentaId;
    }

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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isMotivata() {
        return motivata;
    }

    public void setMotivata(boolean motivata) {
        this.motivata = motivata;
    }

    public String getMaterieNume() {
        return materieNume;
    }

    public void setMaterieNume(String materieNume) {
        this.materieNume = materieNume;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
