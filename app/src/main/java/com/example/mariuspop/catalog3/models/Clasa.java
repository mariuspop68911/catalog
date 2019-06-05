package com.example.mariuspop.catalog3.models;

import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;

import java.io.Serializable;
import java.util.ArrayList;

public class Clasa implements Serializable {

    private long clasaId;
    private long institutieId;
    private String name;
    private String institutieName;
    private ArrayList<Materie> materii;
    private ArrayList<Elev> elevi;
    private String userId;
    private String scoalaToken;
    private ArrayList<MessageForTeacher> messageForTeachers;
    private int year;
    private String currentYearStart;

    public Clasa() {
    }

    public Clasa(String name) {
        this.name = name;
    }

    public long getClasaId() {
        return clasaId;
    }

    public void setClasaId(long clasaId) {
        this.clasaId = clasaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Materie> getMaterii() {
        if (materii == null) {
            materii = new ArrayList<>();
        }
        return materii;
    }

    public void setMaterii(ArrayList<Materie> materii) {
        this.materii = materii;
    }

    public ArrayList<Elev> getElevi() {
        return elevi;
    }

    public void setElevi(ArrayList<Elev> elevi) {
        this.elevi = elevi;
    }

    public long getInstitutieId() {
        return institutieId;
    }

    public void setInstitutieId(long institutieId) {
        this.institutieId = institutieId;
    }

    public String getInstitutieName() {
        return institutieName;
    }

    public void setInstitutieName(String institutieName) {
        this.institutieName = institutieName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScoalaToken() {
        return scoalaToken;
    }

    public void setScoalaToken(String scoalaToken) {
        this.scoalaToken = scoalaToken;
    }

    public ArrayList<MessageForTeacher> getMessageForTeachers() {
        return messageForTeachers;
    }

    public void setMessageForTeachers(ArrayList<MessageForTeacher> messageForTeachers) {
        this.messageForTeachers = messageForTeachers;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCurrentYearStart() {
        return currentYearStart;
    }

    public void setCurrentYearStart(String currentYearStart) {
        this.currentYearStart = currentYearStart;
    }
}
