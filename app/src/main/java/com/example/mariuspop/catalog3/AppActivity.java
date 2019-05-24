package com.example.mariuspop.catalog3;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.charts.AbsenteOverviewActivity;
import com.example.mariuspop.catalog3.charts.NotePerformanceActivity;
import com.example.mariuspop.catalog3.client.ClientInboxActivity;
import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.db.DBHelper;
import com.example.mariuspop.catalog3.main.ClassPerformanceActivity;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.main.ScHomeActivity;
import com.example.mariuspop.catalog3.main.ScInboxActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public abstract class AppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DBHelper dbHelper;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clase_main);
        initViews();
        dbHelper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view_client);

        if (PreferencesManager.getStringFromPrefs(Constants.APP_MODE).equals(Constants.APP_MODE_PARINTE)) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_client);
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        }

        TextView nameDrawer = navigationView.getHeaderView(0).findViewById(R.id.name_drawer);
        nameDrawer.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        TextView emailDrawer = navigationView.getHeaderView(0).findViewById(R.id.email_drawer);
        emailDrawer.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        navigationView.setNavigationItemSelectedListener(this);

    }

    protected abstract void setToolbarTitle();

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarTitle();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.refresh) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (!PreferencesManager.getStringFromPrefs(Constants.APP_MODE).equals(Constants.APP_MODE_PARINTE)) {
            if (id == R.id.home) {
                Intent intent = new Intent(this, MateriiActivity.class);
                intent.putExtra(Constants.EXTRA_MESSAGE_CLASA, Long.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_CLASS)));
                startActivity(intent);
            } else if (id == R.id.situatie_generala) {
                Intent intent = new Intent(this, ClassPerformanceActivity.class);
                startActivity(intent);
            } else if (id == R.id.alege_clasa) {
                Intent intent = new Intent(this, ScHomeActivity.class);
                startActivity(intent);
            } else if (id == R.id.mesaje) {
                Intent intent = new Intent(this, ScInboxActivity.class);
                startActivity(intent);
            } else if (id == R.id.setari) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
        } else {
            if (id == R.id.situatie_generala) {
                Intent intent = new Intent(this, ClientHomeActivity.class);
                startActivity(intent);
            } else if (id == R.id.setari) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            } else if (id == R.id.situatie_absente) {
                Intent intent = new Intent(this, AbsenteOverviewActivity.class);
                startActivity(intent);
            } else if (id == R.id.performante) {
                Intent intent = new Intent(this, NotePerformanceActivity.class);
                startActivity(intent);
            } else if (id == R.id.mesaje) {
                Intent intent = new Intent(this, ClientInboxActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public abstract int getContentAreaLayoutId();

    void initViews() {
        ConstraintLayout constraintLayout = findViewById(R.id.content_container);
        View child = getLayoutInflater().inflate(getContentAreaLayoutId(), null);
        child.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        constraintLayout.addView(child);
    }

}
