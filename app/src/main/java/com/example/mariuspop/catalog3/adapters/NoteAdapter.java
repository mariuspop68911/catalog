package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mariuspop on 24/05/18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Nota> employeeComments;
    private Context context;

    public NoteAdapter(Context context, ArrayList<Nota> employeeComments) {
        this.employeeComments = employeeComments;
        this.context = context;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nota, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.notaValue.setText(String.valueOf(employeeComments.get(position).getValue()));
        if (employeeComments.get(position).isTeza()) {
            viewHolder.teza.setVisibility(View.VISIBLE);
        } else {
            viewHolder.teza.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView notaValue;
        TextView teza;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.comment_cv);
            notaValue = itemView.findViewById(R.id.nota_text);
            teza = itemView.findViewById(R.id.nota_teza);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
        }
    }
}
