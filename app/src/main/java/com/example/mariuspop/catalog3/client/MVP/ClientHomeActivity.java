package com.example.mariuspop.catalog3.client.MVP;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Elev;


public class ClientHomeActivity extends AppActivity implements ClientHomeView {

    private RelativeLayout loadingPanel;
    private ClientHomePresenter presenter;
    private ListView dbListView;
    private ListView alertList;
    private LinearLayout alertLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingPanel = findViewById(R.id.loadingPanel);
        alertList = findViewById(R.id.alertList);
        dbListView = findViewById(R.id.main_elev_lista);
        alertLayout = findViewById(R.id.alertLayout);

        loadingPanel.setVisibility(View.VISIBLE);
        presenter = new ClientHomePresenter(this, this);
    }

    @Override
    public void setToolbarTitle() {
        if (presenter != null) {
            Elev elev = presenter.getElev();
            if (elev != null) {
                toolbar.setTitle(elev.getName());
                toolbar.setSubtitle(elev.getInstituteName());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.refresh) {
            loadingPanel.setVisibility(View.VISIBLE);
            presenter.getElevFromWs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public ListView getList() {
        return dbListView;
    }

    @Override
    public ListView getAlertList() {
        return alertList;
    }

    @Override
    public LinearLayout getAlertLyout() {
        return alertLayout;
    }

    @Override
    public RelativeLayout getLoadingPanel() {
        return loadingPanel;
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.client_home_activity;
    }


}
