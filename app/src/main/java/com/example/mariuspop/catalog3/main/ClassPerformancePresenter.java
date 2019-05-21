package com.example.mariuspop.catalog3.main;

import com.example.mariuspop.catalog3.wizard.AddManager;

public class ClassPerformancePresenter {

    private ClassPerformanceView view;

    public ClassPerformancePresenter(ClassPerformanceView view) {
        this.view = view;
        view.setUi(AddManager.getInstance().getClasa());
    }


}
