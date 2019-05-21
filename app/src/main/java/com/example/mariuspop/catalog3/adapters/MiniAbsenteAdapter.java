package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.models.Absenta;

import java.util.ArrayList;

/**
 * Created by mariuspop on 24/05/18.
 */

public class MiniAbsenteAdapter extends RecyclerView.Adapter<MiniAbsenteAdapter.ViewHolder> {

    private ArrayList<Absenta> absente;
    private Context context;
    private String materieNume;

    public MiniAbsenteAdapter(Context context, ArrayList<Absenta> absente, String materieNume) {
        this.absente = absente;
        this.context = context;
        this.materieNume = materieNume;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mini_absenta, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (!absente.get(position).isMotivata()) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.btn_logut_bg));
        } else {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_warm));
        }
        viewHolder.absentaText.setText(String.valueOf(absente.get(position).isMotivata() ? context.getResources().getString(R.string.absenta_motivata) :
                context.getResources().getString(R.string.absenta_nemotivata)));
        viewHolder.cv.setTag(absente.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView absentaText;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.abs_cv);
            absentaText = itemView.findViewById(R.id.absentaText);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClientMaterieDetailsActivity.class);
                    intent.putExtra(Constants.MATERIE_ID, absente.get(0).getMaterieId());
                    intent.putExtra(Constants.MATERIE_NUME, materieNume);
                    context.startActivity(intent);
                }
            });
        }
    }
}
