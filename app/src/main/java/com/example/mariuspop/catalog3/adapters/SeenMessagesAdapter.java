package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.main.ElevDetailsActivity;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.SeenMessageModel;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SeenMessagesAdapter extends ArrayAdapter<SeenMessageModel> {

    private Context context;
    private long materieId;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        LinearLayout parentLayout;
    }

    public SeenMessagesAdapter(ArrayList<SeenMessageModel> data, Context context, long materieId) {
        super(context, R.layout.seen_messages_item, data);
        this.context = context;
        this.materieId = materieId;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SeenMessageModel seenMessageModel = getItem(position);
        ViewHolder viewHolder;

        final Clasa clasa = AddManager.getInstance().getClasa();

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.seen_messages_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.alertText);
            viewHolder.parentLayout = convertView.findViewById(R.id.parent_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(seenMessageModel.getText());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long elevId = 0;
                for (Elev elev : clasa.getElevi()) {
                    for (MesajProf mesajProf : elev.getMesajProfs()) {
                        if (mesajProf.getMesajId() == seenMessageModel.getMesajId()) {
                            elevId = elev.getElevId();
                            mesajProf.setSeen(true);
                            FirebaseDb.saveClasa(clasa);
                            break;
                        }
                    }
                }

                Intent intent = new Intent(context, ElevDetailsActivity.class);
                intent.putExtra(Constants.ELEV_ID, elevId);
                intent.putExtra(Constants.CLASA_ID, clasa.getClasaId());
                intent.putExtra(Constants.MATERIE_ID, materieId);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}