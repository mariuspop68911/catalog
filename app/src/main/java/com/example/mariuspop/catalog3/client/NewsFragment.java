package com.example.mariuspop.catalog3.client;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.ClientNewsAdapter;
import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.models.NewsItem;

import java.util.ArrayList;


public class NewsFragment extends DialogFragment {

    private static final String TAG = NewsFragment.class.getSimpleName();
    private ListView newsList;
    private ArrayList<NewsItem> news;


    public NewsFragment() {
    }


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: hit");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: hit");
        View rootView = inflater.inflate(R.layout.news_layout, container, false);
        newsList = rootView.findViewById(R.id.newsList);
        RelativeLayout listLayout = rootView.findViewById(R.id.list_layout);
        RelativeLayout emptylayout = rootView.findViewById(R.id.emptylayout);
        news = ((ClientHomeActivity) getActivity()).getPresenter().getNews();
        if (news != null && !news.isEmpty()) {
            listLayout.setVisibility(View.VISIBLE);
            emptylayout.setVisibility(View.GONE);
            ClientNewsAdapter clientNewsAdapter = new ClientNewsAdapter(news, getActivity());
            newsList.setAdapter(clientNewsAdapter);
        } else {
            listLayout.setVisibility(View.GONE);
            emptylayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: hit");
        super.onActivityCreated(savedInstanceState);

    }


}