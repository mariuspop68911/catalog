package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseRm;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;
import java.util.Objects;

public class ClientHomeAdapter extends ArrayAdapter<Materie> {

    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView nemotivateText;
        TextView motivateText;
        LinearLayout contentLayout;
        LinearLayout emptyView;
    }

    public ClientHomeAdapter(ArrayList<Materie> data, Context context) {
        super(context, R.layout.client_elev_item, data);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Materie materie = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.client_elev_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.nemotivateText = convertView.findViewById(R.id.nemotivateText);
            viewHolder.motivateText = convertView.findViewById(R.id.motivateText);
            viewHolder.contentLayout = convertView.findViewById(R.id.content_layout);
            viewHolder.emptyView = convertView.findViewById(R.id.empty);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        long materieId = Objects.requireNonNull(materie).getMaterieId();

        ArrayList<Nota> noteByMaterie = ElevManager.getInstance().getNoteByMaterieId(materieId, FirebaseRm.getCurrentSemesterForced());
        ArrayList<Absenta> absenteByMaterie = ElevManager.getInstance().getAbsenteByMaterieId(materieId, FirebaseRm.getCurrentSemesterForced());

        if (noteByMaterie.size() > 0 || absenteByMaterie.size() > 0) {
            viewHolder.contentLayout.setVisibility(View.VISIBLE);
            viewHolder.emptyView.setVisibility(View.GONE);
        } else {
            viewHolder.contentLayout.setVisibility(View.GONE);
            viewHolder.emptyView.setVisibility(View.VISIBLE);
        }

        ArrayList<Absenta> absenteMotivate = new ArrayList<>();
        ArrayList<Absenta> absenteNeMotivate = new ArrayList<>();
        for (Absenta absenta : absenteByMaterie) {
            if(absenta.isMotivata()){
                absenteMotivate.add(absenta);
            } else {
                absenteNeMotivate.add(absenta);
            }
        }
        viewHolder.nemotivateText.setText(String.valueOf(absenteNeMotivate.size()));
        viewHolder.motivateText.setText(String.valueOf(absenteMotivate.size()));
        viewHolder.txtName.setText(Objects.requireNonNull(materie).getName());

        final RecyclerView noteRecycleView = convertView.findViewById(R.id.elev_lista_note);
        noteRecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        noteRecycleView.setLayoutManager(llm);
        MiniNoteAdapter noteAdapter = new MiniNoteAdapter(context, noteByMaterie, materie.getName());
        noteRecycleView.setAdapter(noteAdapter);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientMaterieDetailsActivity.class);
                intent.putExtra(Constants.MATERIE_ID, materie.getMaterieId());
                intent.putExtra(Constants.MATERIE_NUME, materie.getName());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}