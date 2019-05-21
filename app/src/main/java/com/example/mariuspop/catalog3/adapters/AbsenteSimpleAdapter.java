package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariuspop.catalog3.DateUtil;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Absenta;

import java.util.ArrayList;

/**
 * Created by mariuspop on 24/05/18.
 */

public class AbsenteSimpleAdapter extends RecyclerView.Adapter<AbsenteSimpleAdapter.ViewHolder> {

    private ArrayList<Absenta> absente;
    private Context context;

    public AbsenteSimpleAdapter(Context context, ArrayList<Absenta> absente) {
        this.absente = absente;
        this.context = context;
    }

    public void setData(ArrayList<Absenta> absente){
        this.absente = absente;
    }

    @Override
    public int getItemCount() {
        return absente.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_simple_absenta, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (!absente.get(position).isMotivata()) {
            viewHolder.dataAbsenta.setTextColor(context.getResources().getColor(R.color.red_warm));
        } else {
            viewHolder.dataAbsenta.setTextColor(context.getResources().getColor(R.color.white));
        }
        viewHolder.dataAbsenta.setText(DateUtil.getData(context, absente.get(position).getData()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView dataAbsenta;

        ViewHolder(View itemView) {
            super(itemView);
            dataAbsenta = itemView.findViewById(R.id.dataAbsenta);

        }
    }
}
