package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.main.ElevDetailsActivity;
import com.example.mariuspop.catalog3.models.mesaje.InboxMessage;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by mariuspop on 24/05/18.
 */

public class InboxMessagesAdapter extends RecyclerView.Adapter<InboxMessagesAdapter.ViewHolder> {

    private ArrayList<InboxMessage> data;
    private Context context;

    public InboxMessagesAdapter(Context context, ArrayList<InboxMessage> messages) {
        this.data = messages;
        this.context = context;
    }

    public void setData(ArrayList<InboxMessage> data){
        this.data = data;
    }

    public ArrayList<InboxMessage> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_mesaj, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        if(data.get(position).getFrom() != null && data.get(position).getTo() != null
                && !data.get(position).getFrom().isEmpty() && !data.get(position).getTo().isEmpty()) {
            viewHolder.fromEdit.setVisibility(View.VISIBLE);
            viewHolder.toEdit.setVisibility(View.VISIBLE);
            viewHolder.mesajText.setText(data.get(position).getMessage());
            viewHolder.fromText.setText(context.getResources().getString(R.string.de_la_parinte));
            viewHolder.toText.setText(context.getResources().getString(R.string.pentru_profesor));
            viewHolder.fromEdit.setText(data.get(position).getFrom());
            viewHolder.toEdit.setText(data.get(position).getTo());
            viewHolder.date.setText(Utils.getDate(data.get(position).getDate().getTime(), "dd-MM hh:mm"));
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.comments));

            viewHolder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ElevDetailsActivity.class);
                    intent.putExtra(Constants.ELEV_ID, data.get(position).getElevId());
                    intent.putExtra(Constants.CLASA_ID, AddManager.getInstance().getClasa().getClasaId());
                    intent.putExtra(Constants.MATERIE_ID, data.get(position).getMaterieId());
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        } else {
            viewHolder.mesajText.setText(data.get(position).getMessage());
            viewHolder.fromText.setText(context.getResources().getString(R.string.de_la_diriginte));
            viewHolder.toText.setText(context.getResources().getString(R.string.pentru_all));
            viewHolder.fromEdit.setVisibility(View.GONE);
            viewHolder.toEdit.setVisibility(View.GONE);
            viewHolder.date.setText(Utils.getDate(data.get(position).getDate().getTime(), "dd-MM hh:mm"));
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.yellow_warm));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView mesajText;
        TextView fromText;
        TextView toText;
        TextView fromEdit;
        TextView toEdit;
        TextView date;
        LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.comment_cv);
            layout = itemView.findViewById(R.id.layout_id);
            mesajText = itemView.findViewById(R.id.mesaj_text);
            fromText = itemView.findViewById(R.id.from_text);
            toText = itemView.findViewById(R.id.to_text);
            fromEdit = itemView.findViewById(R.id.from_edit);
            toEdit = itemView.findViewById(R.id.to_edit);
            date = itemView.findViewById(R.id.date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
        }
    }
}
