package com.example.mariuspop.catalog3.models;

import java.io.Serializable;

public class Materie implements Serializable {

    private long materieId;
    private long clasaId;
    private String name;
    private boolean teza;
    private int year;

    public Materie() {
    }

    public Materie(String name) {
        this.name = name;
    }

    public long getMaterieId() {
        return materieId;
    }

    public void setMaterieId(long materieId) {
        this.materieId = materieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getClasaId() {
        return clasaId;
    }

    public void setClasaId(long clasaId) {
        this.clasaId = clasaId;
    }

    public boolean isTeza() {
        return teza;
    }

    public void setTeza(boolean teza) {
        this.teza = teza;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
