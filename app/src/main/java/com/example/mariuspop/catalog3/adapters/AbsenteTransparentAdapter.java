package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariuspop.catalog3.DateUtil;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.db.DBHelper;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by mariuspop on 24/05/18.
 */

public class AbsenteTransparentAdapter extends RecyclerView.Adapter<AbsenteTransparentAdapter.ViewHolder> {

    private ArrayList<Absenta> absente;
    private Context context;

    public AbsenteTransparentAdapter(Context context, ArrayList<Absenta> absente) {
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_absenta2, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (!absente.get(position).isMotivata()) {
            viewHolder.absentaText.setTextColor(context.getResources().getColor(R.color.red_warm));
            viewHolder.dataAbsenta.setTextColor(context.getResources().getColor(R.color.red_warm));
        } else {
            viewHolder.dataAbsenta.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.absentaText.setTextColor(context.getResources().getColor(R.color.white));
        }
        viewHolder.absentaText.setText(String.valueOf(absente.get(position).isMotivata() ? context.getResources().getString(R.string.motivata) :
                context.getResources().getString(R.string.nemotivata)));
        viewHolder.dataAbsenta.setText(DateUtil.getData(context, absente.get(position).getData()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView absentaText;
        TextView dataAbsenta;

        ViewHolder(View itemView) {
            super(itemView);
            absentaText = itemView.findViewById(R.id.absentaText);
            dataAbsenta = itemView.findViewById(R.id.dataAbsenta);

        }
    }
}
