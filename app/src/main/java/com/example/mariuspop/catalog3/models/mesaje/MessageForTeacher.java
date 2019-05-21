package com.example.mariuspop.catalog3.models.mesaje;

public class MessageForTeacher {

    private String clasaId;
    private String elevId;
    private String elevName;
    private String teacherToken;
    private String message;
    private String materieName;
    private String materieId;
    private String prof;
    private String clientToken;

    public String getClasaId() {
        return clasaId;
    }

    public void setClasaId(String clasaId) {
        this.clasaId = clasaId;
    }

    public String getElevName() {
        return elevName;
    }

    public void setElevName(String elevName) {
        this.elevName = elevName;
    }

    public String getTeacherToken() {
        return teacherToken;
    }

    public void setTeacherToken(String teacherToken) {
        this.teacherToken = teacherToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaterieName() {
        return materieName;
    }

    public void setMaterieName(String materieName) {
        this.materieName = materieName;
    }

    public String getElevId() {
        return elevId;
    }

    public void setElevId(String elevId) {
        this.elevId = elevId;
    }

    public String getMaterieId() {
        return materieId;
    }

    public void setMaterieId(String materieId) {
        this.materieId = materieId;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }
}
