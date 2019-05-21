package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;

import java.util.ArrayList;

/**
 * Created by mariuspop on 24/05/18.
 */

public class MesajeAdapter extends RecyclerView.Adapter<MesajeAdapter.ViewHolder> {

    private ArrayList<MesajProf> data;
    private Context context;

    public MesajeAdapter(Context context, ArrayList<MesajProf> employeeComments) {
        this.data = employeeComments;
        this.context = context;
    }

    public void setData(ArrayList<MesajProf> data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.v("TESTLOGG", " i " + i);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mesaj, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.mesajText.setText(String.valueOf(data.get(position).getMessage()));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (data.get(position).isProf()) {
            params.gravity = Gravity.END;
            viewHolder.layout.setLayoutParams(params);
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_warm));
        } else {
            params.gravity = Gravity.START;
            viewHolder.layout.setLayoutParams(params);
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.comments));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView mesajText;
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.comment_cv);
            layout = itemView.findViewById(R.id.layout_id);
            mesajText = itemView.findViewById(R.id.mesaj_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
        }
    }
}
