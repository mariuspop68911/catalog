package com.example.mariuspop.catalog3.wizard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.CustomAdapterMaterie;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Materie;

import java.util.ArrayList;
import java.util.Objects;

public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_NUME_CLASA = "nume_clasa";

    private OnButtonClickListener mOnButtonClickListener;

    interface OnButtonClickListener {
        void onButtonClicked(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnButtonClickListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity) context).getLocalClassName()
                    + " must implement OnButtonClickListener");
        }
    }

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //args.putString(ARG_SECTION_NUME_CLASA);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_add, container, false);
        TextView textView = rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER)));
        Log.v("TESTLOGG", " getInt " + getArguments().getInt(ARG_SECTION_NUMBER));
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1: {
                rootView = handleAddClass(inflater, container);
                break;
            }
            case 2: {
                rootView = handleAddMaterie(inflater, container);
                break;
            }
            case 3: {
                rootView = inflater.inflate(R.layout.main_add_elevi, container, false);
                break;
            }
        }
        return rootView;
    }

    private View handleAddClass(final LayoutInflater inflater, final ViewGroup container) {
        View rootView = inflater.inflate(R.layout.main_add, container, false);
        final EditText edit = rootView.findViewById(R.id.edit);
        Button continua = rootView.findViewById(R.id.continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit.getText().toString().isEmpty()) {
                    AddManager.getInstance().setClasa(new Clasa(edit.getText().toString()));
                    //handleAddMaterie(inflater, container);
                    mOnButtonClickListener.onButtonClicked(v);
                } else {
                    Toast.makeText(getActivity(), "Completeaza", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    private View handleAddMaterie(LayoutInflater inflater, ViewGroup container) {
        ListView adaugaMateriiLista;
        final ArrayList<Materie> materii = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.main_add_materie, container, false);
        adaugaMateriiLista = rootView.findViewById(R.id.adauga_materii_lista);
        final CustomAdapterMaterie adapter = new CustomAdapterMaterie(materii, getActivity());
        adaugaMateriiLista.setAdapter(adapter);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // custom dialog
                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setContentView(R.layout.add_dialog);
                dialog.setTitle(getResources().getString(R.string.adauga_materie));
                final EditText name = dialog.findViewById(R.id.edit1);
                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameText = name.getText().toString();
                        if (!nameText.isEmpty()) {
                            Materie materie = new Materie(name.getText().toString());
                            materii.add(materie);
                            adapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        Button continua = rootView.findViewById(R.id.continua);
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!materii.isEmpty()) {
                    AddManager.getInstance().getClasa().setMaterii(materii);
                    mOnButtonClickListener.onButtonClicked(v);
                } else {
                    Toast.makeText(getActivity(), "Completeaza", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }
}
