package com.example.mariuspop.catalog3.models;

public class Institutie {

    private long institutieId;

    private String nume;

    private long clasaId;

    private String userId;

    public long getInstitutieId() {
        return institutieId;
    }

    public void setInstitutieId(long institutieId) {
        this.institutieId = institutieId;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public long getClasaId() {
        return clasaId;
    }

    public void setClasaId(long clasaId) {
        this.clasaId = clasaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
