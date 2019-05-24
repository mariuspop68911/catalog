package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.models.NewsItem;

import java.util.ArrayList;

public class ClientNewsAdapter extends ArrayAdapter<NewsItem> {

    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public ClientNewsAdapter(ArrayList<NewsItem> data, Context context) {
        super(context, R.layout.client_news_item, data);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final NewsItem mesaj = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.client_news_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.alertText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(mesaj.getText());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientMaterieDetailsActivity.class);
                intent.putExtra(Constants.MATERIE_ID, mesaj.getMaterieId());
                intent.putExtra(Constants.MATERIE_NUME, mesaj.getMaterieNume());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}