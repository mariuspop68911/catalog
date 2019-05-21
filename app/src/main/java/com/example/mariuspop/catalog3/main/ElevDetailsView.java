package com.example.mariuspop.catalog3.main;

import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mariuspop.catalog3.db.DBHelper;

public interface ElevDetailsView {

    void setDataOnViews();
    void setToolbarTitle();
    DBHelper getDB(); ///!!!!
    CheckBox getCheckBox();
    TextView getPhone();

}
