package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Typeface;
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
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;

/**
 * Created by mariuspop on 24/05/18.
 */

public class NoteTransparentAdapter extends RecyclerView.Adapter<NoteTransparentAdapter.ViewHolder> {

    private ArrayList<Nota> employeeComments;
    private Context context;

    public NoteTransparentAdapter(Context context, ArrayList<Nota> employeeComments) {
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nota2, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.notaValue.setText(String.valueOf(employeeComments.get(position).getValue()));
        viewHolder.data.setText(DateUtil.getData(context, employeeComments.get(position).getData()));
        if (employeeComments.get(position).isTeza()) {
            viewHolder.teza.setVisibility(View.VISIBLE);
        } else {
            viewHolder.teza.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView notaValue;
        TextView data;
        TextView teza;

        ViewHolder(View itemView) {
            super(itemView);
            notaValue = itemView.findViewById(R.id.nota_text);
            data = itemView.findViewById(R.id.data);
            teza = itemView.findViewById(R.id.nota_teza);
        }
    }
}
