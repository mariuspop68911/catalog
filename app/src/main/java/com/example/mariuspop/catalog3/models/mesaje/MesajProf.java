package com.example.mariuspop.catalog3.models.mesaje;

import java.io.Serializable;
import java.util.Date;

public class MesajProf  implements Serializable {

    private long mesajId;
    private long elevId;
    private long materieId;
    private String message;
    private boolean isProf;
    private Date date;

    public long getMesajId() {
        return mesajId;
    }

    public void setMesajId(long mesajId) {
        this.mesajId = mesajId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean isProf() {
        return isProf;
    }

    public void setProf(boolean prof) {
        isProf = prof;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
