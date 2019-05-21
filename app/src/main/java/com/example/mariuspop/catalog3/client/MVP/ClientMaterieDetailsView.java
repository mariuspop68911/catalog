package com.example.mariuspop.catalog3.client.MVP;

import android.widget.EditText;
import android.widget.ScrollView;

import com.example.mariuspop.catalog3.adapters.MesajeAdapter;

public interface ClientMaterieDetailsView {

    EditText getSendEditText();
    MesajeAdapter getMesajeAdapter();
    ScrollView getScrolLayout();
    void setToolbarTitle();

}
