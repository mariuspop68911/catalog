package com.example.mariuspop.catalog3.models;

import java.io.Serializable;

public class AbsentaMessageWrapper implements Serializable {

    private long elevId;
    private String elevName;
    private Absenta absenta;
    private String nrTel;
    private Clasa clasa;

    public String getElevName() {
        return elevName;
    }

    public void setElevName(String elevName) {
        this.elevName = elevName;
    }

    public Absenta getAbsenta() {
        return absenta;
    }

    public void setAbsenta(Absenta absenta) {
        this.absenta = absenta;
    }

    public String getNrTel() {
        return nrTel;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public Clasa getClasa() {
        return clasa;
    }

    public void setClasa(Clasa clasa) {
        this.clasa = clasa;
    }

    public long getElevId() {
        return elevId;
    }

    public void setElevId(long elevId) {
        this.elevId = elevId;
    }
}
