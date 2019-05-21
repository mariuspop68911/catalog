package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.main.MateriiActivity;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Objects;

public class ScHomeAdapter extends ArrayAdapter<Clasa> {

    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public ScHomeAdapter(ArrayList<Clasa> data, Context context) {
        super(context, R.layout.clasa_item, data);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Clasa clasa = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.clasa_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/

        viewHolder.txtName.setText(Objects.requireNonNull(clasa).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddManager.getInstance().setClasa(clasa);
                PreferencesManager.saveStringToPrefs(Constants.CURRENT_CLASS, String.valueOf(clasa.getClasaId()));
                Intent intent = new Intent(context, MateriiActivity.class);
                intent.putExtra(Constants.EXTRA_MESSAGE_CLASA, clasa.getClasaId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}