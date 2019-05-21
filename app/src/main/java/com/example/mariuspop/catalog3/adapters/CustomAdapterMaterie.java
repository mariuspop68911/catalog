package com.example.mariuspop.catalog3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.models.Materie;

import java.util.ArrayList;

public class CustomAdapterMaterie extends ArrayAdapter<Materie> implements View.OnClickListener{

    private ArrayList<Materie> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }

    public CustomAdapterMaterie(ArrayList<Materie> data, Context context) {
        super(context, R.layout.materie_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Materie materie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.materie_item, parent, false);
            viewHolder.txtName =  convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/

        viewHolder.txtName.setText(materie.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}