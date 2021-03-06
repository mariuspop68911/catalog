package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.AbsenteWrapper;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.MediiEleviWrapper;

import java.util.ArrayList;
import java.util.Objects;

public class ClasaPerformanceAbsAdapter extends ArrayAdapter<AbsenteWrapper> {

    private ArrayList<AbsenteWrapper> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView medie;

    }

    public ClasaPerformanceAbsAdapter(ArrayList<AbsenteWrapper> data, Context context) {
        super(context, R.layout.clasa_perf_abs_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final AbsenteWrapper elev = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.clasa_perf_abs_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName = convertView.findViewById(R.id.name);
        viewHolder.medie = convertView.findViewById(R.id.medie);

        viewHolder.txtName.setText(Objects.requireNonNull(elev).getNumeElev());
        viewHolder.medie.setText(String.valueOf(elev.getNumarAbsNemotivate()));
        return convertView;
    }
}