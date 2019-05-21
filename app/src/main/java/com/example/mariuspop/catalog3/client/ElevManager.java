package com.example.mariuspop.catalog3.client;

import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Nota;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;

import java.util.ArrayList;

public class ElevManager {

    private static final ElevManager ourInstance = new ElevManager();

    private Elev elev;

    public static ElevManager getInstance() {
        return ourInstance;
    }

    public Elev getElev() {
        return elev;
    }

    public void setElev(Elev elev) {
        this.elev = elev;
    }

    public ArrayList<Nota> getNoteByMaterieId(long materieId){
        ArrayList<Nota> noteByMaterie = new ArrayList<>();
        for (Nota nota : elev.getNote()) {
            if (materieId == nota.getMaterieId()) {
                noteByMaterie.add(nota);
            }
        }
        return noteByMaterie;
    }

    public ArrayList<Absenta> getAbsenteByMaterieId(long materieId){
        ArrayList<Absenta> absenteByMaterie = new ArrayList<>();
        for (Absenta absenta : elev.getAbsente()) {
            if (materieId == absenta.getMaterieId()) {
                absenteByMaterie.add(absenta);
            }
        }
        return absenteByMaterie;
    }

    public ArrayList<MesajProf> getMesajeByMaterieId(long materieId){
        ArrayList<MesajProf> mesajeByMaterie = new ArrayList<>();
        for (MesajProf mesaj : elev.getMesajProfs()) {
            if (materieId == mesaj.getMaterieId()) {
                mesajeByMaterie.add(mesaj);
            }
        }
        return mesajeByMaterie;
    }
}
