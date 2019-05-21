package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;
import java.util.Objects;

public class ClientAlertAdapter extends ArrayAdapter<String> {

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public ClientAlertAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.client_alert_item, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String mesaj = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.client_alert_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.alertText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(mesaj);

        return convertView;
    }
}