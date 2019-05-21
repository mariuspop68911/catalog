package com.example.mariuspop.catalog3.main;

import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClase;
import com.example.mariuspop.catalog3.models.Clasa;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ScHomePresenter implements FirebaseCallbackClase {

    private ScHomeView view;

    ScHomePresenter(ScHomeView view) {
        this.view = view;
        FirebaseDb.getClaseByUserId(this, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
    }


    @Override
    public void onClaseReceived(ArrayList<Clasa> clase) {
        view.setUi(clase);
    }
}
