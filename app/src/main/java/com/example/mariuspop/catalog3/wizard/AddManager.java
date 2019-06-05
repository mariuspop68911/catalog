package com.example.mariuspop.catalog3.wizard;

import com.example.mariuspop.catalog3.models.Clasa;

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

}
