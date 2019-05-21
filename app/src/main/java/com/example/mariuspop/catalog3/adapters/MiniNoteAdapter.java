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
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;

/**
 * Created by mariuspop on 24/05/18.
 */

public class MiniNoteAdapter extends RecyclerView.Adapter<MiniNoteAdapter.ViewHolder> {

    private ArrayList<Nota> employeeComments;
    private Context context;
    private String materieNume;

    public MiniNoteAdapter(Context context, ArrayList<Nota> employeeComments, String materieNume) {
        this.employeeComments = employeeComments;
        this.context = context;
        this.materieNume = materieNume;
    }

    public void setData(ArrayList<Nota> employeeComments){
        this.employeeComments = employeeComments;
    }

    @Override
    public int getItemCount() {
        return employeeComments.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mini_nota, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        int nota = employeeComments.get(position).getValue();
        if (nota == 10) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.fancy3));
        } else if (nota < 5) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.btn_logut_bg));
        }
        viewHolder.notaValue.setText(String.valueOf(employeeComments.get(position).getValue()));
        if (employeeComments.get(position).isTeza()) {
            viewHolder.notaTeza.setVisibility(View.VISIBLE);
        } else {
            viewHolder.notaTeza.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView notaValue;
        TextView notaTeza;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.comment_cv);
            notaValue = itemView.findViewById(R.id.nota_text);
            notaTeza = itemView.findViewById(R.id.nota_teza);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClientMaterieDetailsActivity.class);
                    intent.putExtra(Constants.MATERIE_ID, employeeComments.get(0).getMaterieId());
                    intent.putExtra(Constants.MATERIE_NUME, materieNume);
                    context.startActivity(intent);
                }
            });
        }
    }
}
