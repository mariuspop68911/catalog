package com.example.mariuspop.catalog3.client.MVP;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


public interface ClientHomeView {

    void setToolbarTitle();
    ListView getList();
    ListView getAlertList();
    LinearLayout getAlertLyout();
    RelativeLayout getLoadingPanel();
    void invalidate();
}
