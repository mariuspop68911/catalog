package com.example.mariuspop.catalog3.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Nota implements Serializable {

    private long notaId;
    private long elevId;
    private long materieId;
    private Date data;
    private int value;
    private boolean teza;

    public Nota() {
        notaId = Calendar.getInstance().getTimeInMillis();
    }

    public long getNotaId() {
        return notaId;
    }

    public void setNotaId(long notaId) {
        this.notaId = notaId;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isTeza() {
        return teza;
    }

    public void setTeza(boolean teza) {
        this.teza = teza;
    }
}
