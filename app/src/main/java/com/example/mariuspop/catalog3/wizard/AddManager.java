package com.example.mariuspop.catalog3.wizard;

import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;

public class AddManager {

    private static AddManager ourInstance;

    private Clasa clasa;

    public static AddManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new AddManager();
        }
        return ourInstance;
    }

    public Clasa getClasa() {
        return clasa;
    }

    public void setClasa(Clasa clasa) {
        this.clasa = clasa;
    }

    public ArrayList<Nota> getNoteByMaterieId(Elev elev, long materieId){
        ArrayList<Nota> noteByMaterie = new ArrayList<>();
        for (Nota nota : elev.getNote()) {
            if (materieId == nota.getMaterieId()) {
                noteByMaterie.add(nota);
            }
        }
        return noteByMaterie;
    }
}
