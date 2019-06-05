package com.example.mariuspop.catalog3.models;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;

import java.io.Serializable;
import java.util.ArrayList;

public class Elev implements Serializable {

    private long elevId;
    private String elevCode;
    private long clasaId;
    private String name;
    private String phoneNumber;
    private String instituteName;
    private ArrayList<Nota> note;
    private ArrayList<Absenta> absente;
    private ArrayList<MesajProf> mesajProfs;

    public Elev() {
    }

    public Elev(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public long getElevId() {
        return elevId;
    }

    public void setElevId(long elevId) {
        this.elevId = elevId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Nota> getNote() {
        if (note == null) {
            note = new ArrayList<>();
        }
        return note;
    }

    public void setNote(ArrayList<Nota> note) {
        this.note = note;
    }

    public ArrayList<Absenta> getAbsente() {
        if (absente == null) {
            absente = new ArrayList<>();
        }
        return absente;
    }

    public void setAbsente(ArrayList<Absenta> absente) {
        this.absente = absente;
    }

    public long getClasaId() {
        return clasaId;
    }

    public void setClasaId(long clasaId) {
        this.clasaId = clasaId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public ArrayList<MesajProf> getMesajProfs() {
        return mesajProfs == null ? new ArrayList<MesajProf>() : mesajProfs;
    }

    public void setMesajProfs(ArrayList<MesajProf> mesajProfs) {
        this.mesajProfs = mesajProfs;
    }

    public String getElevCode() {
        return elevCode;
    }

    public void setElevCode(String elevCode) {
        this.elevCode = elevCode;
    }
}
