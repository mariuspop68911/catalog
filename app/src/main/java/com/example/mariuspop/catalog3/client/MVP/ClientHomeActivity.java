package com.example.mariuspop.catalog3.client.MVP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseRm;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.NewsFragment;
import com.example.mariuspop.catalog3.interfaces.FirebaseRMCallback;
import com.example.mariuspop.catalog3.models.Elev;
import com.google.firebase.messaging.FirebaseMessaging;


public class ClientHomeActivity extends AppActivity implements ClientHomeView, FirebaseRMCallback  {

    private RelativeLayout loadingPanel;
    private ClientHomePresenter presenter;
    private ListView dbListView;
    private ListView alertList;
    private LinearLayout alertLayout;
    private TextView sem;
    private TextView text;
    private ViewGroup titleLayout2;
    private Context context;
    private ClientHomeView clientHomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        clientHomeView = this;

        subscribeToNotifications();
        FirebaseRm.fetchRemoteConfig(this);

        loadingPanel = findViewById(R.id.loadingPanel);
        alertList = findViewById(R.id.alertList);
        dbListView = findViewById(R.id.main_elev_lista);
        alertLayout = findViewById(R.id.alertLayout);
        loadingPanel.setVisibility(View.VISIBLE);
        presenter = new ClientHomePresenter(this, this);

    }

    public ClientHomePresenter getPresenter() {
        return presenter;
    }

    private void subscribeToNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("general");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.testAction);
        menuItem.setIcon(buildCounterDrawable(presenter.getCount(), R.drawable.bell));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.testAction) {
            presenter.resetCounter();

            NewsFragment fragment = NewsFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide1, R.anim.slide2);
            fragmentTransaction.add(fragment, "fragment");
            fragmentTransaction.commit();
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
    public void invalidate() {
        invalidateOptionsMenu();
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.badge_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onRemoteConfigFetched() {
        sem = findViewById(R.id.sem);
        sem.setText(FirebaseRm.getCurrentSemesterForced().equals(Constants.SPINNER_SEM_1) ? "SEM 1" : "SEM 2");
        text = findViewById(R.id.text);
        text.setText(FirebaseRm.getCurrentSemesterForced().equals(Constants.SPINNER_SEM_2) ? "SEM 1" : "SEM 2");
        titleLayout2 = findViewById(R.id.title_layout2);

        final boolean[] visible = new boolean[1];

        sem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(titleLayout2);
                visible[0] = !visible[0];
                text.setVisibility(visible[0] ? View.VISIBLE : View.GONE);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentSemester = FirebaseRm.getCurrentSemester();
                String newSemString = FirebaseRm.getCurrentSemesterForced().equals(Constants.SPINNER_SEM_1) ?
                        Constants.SPINNER_SEM_2 : Constants.SPINNER_SEM_1;
                sem.setText(newSemString.equals(Constants.SPINNER_SEM_1)? "SEM 1" : "SEM 2");
                text.setText(newSemString.equals(Constants.SPINNER_SEM_2)? "SEM 1" : "SEM 2");
                if (currentSemester.equals(newSemString)) {
                    PreferencesManager.removeStringToPrefs(Constants.SEM_OVERRIDE);
                } else {
                    PreferencesManager.saveStringToPrefs(Constants.SEM_OVERRIDE, newSemString);
                }
                TransitionManager.beginDelayedTransition(titleLayout2);
                visible[0] = !visible[0];
                text.setVisibility(visible[0] ? View.VISIBLE : View.GONE);

                loadingPanel.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter = new ClientHomePresenter(clientHomeView, context);
                    }
                }, 500);
            }
        });

    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.client_home_activity;
    }

}
