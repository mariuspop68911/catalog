package com.example.mariuspop.catalog3.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.adapters.AbsenteAdapter;
import com.example.mariuspop.catalog3.adapters.MesajeAdapter;
import com.example.mariuspop.catalog3.adapters.NoteAdapter;
import com.example.mariuspop.catalog3.db.DBHelper;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Nota;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mehdi.sakout.fancybuttons.FancyButton;

public class ElevDetailsActivity extends AppActivity implements ElevDetailsView {

    private TextView materieNume;
    private TextView nume;
    private TextView phone;
    private TextView elevCode;
    private TextView medie;
    private LinearLayout noteTitle;
    private LinearLayout absenteTitle;
    private ImageView arrowImage;
    private ImageView arrowImage2;

    FancyButton adaugaAbsenta;
    FancyButton adaugaNota;

    private Context context;

    private NoteAdapter noteAdapter;
    private AbsenteAdapter absenteAdapter;
    private MesajeAdapter mesajeAdapter;
    private boolean isNoteViewCollapsed;
    private boolean isAbsenteViewCollapsed;

    private RecyclerView noteRecycleView;
    private RecyclerView absenteRecycleView;
    private RecyclerView mesajeRecycleView;

    private CheckBox checkBox = null;

    private Activity activity;

    private ElevDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;

        materieNume = findViewById(R.id.materie_nume);
        nume = findViewById(R.id.elev_name);
        phone = findViewById(R.id.elev_nr_tel);
        elevCode = findViewById(R.id.code_elev);
        medie = findViewById(R.id.medie);
        noteTitle = findViewById(R.id.schedule_text_layout);
        absenteTitle = findViewById(R.id.comment_text_layout);
        arrowImage = findViewById(R.id.arrowImage);
        arrowImage2 = findViewById(R.id.arrowImage2);

        presenter = new ElevDetailsPresenter(getIntent().getLongExtra(Constants.ELEV_ID, 0L), getIntent().getLongExtra(Constants.MATERIE_ID, 0L),
                getIntent().getStringExtra(Constants.CLASA_ID), this);
        presenter.setData();
    }

    @Override
    public void setToolbarTitle() {
        if (presenter.getClasa() != null) {
            toolbar.setTitle(presenter.getClasa().getInstitutieName());
            toolbar.setSubtitle(presenter.getClasa().getName());
        }
    }

    @Override
    public DBHelper getDB() {
        return dbHelper;
    }

    @Override
    public CheckBox getCheckBox() {
        return checkBox;
    }

    @Override
    public TextView getPhone() {
        return phone;
    }

    @Override
    public void setDataOnViews() {
        materieNume.setText(presenter.getMaterie().getName());
        nume.setText(presenter.getElev().getName());
        phone.setText(presenter.getElev().getPhoneNumber());
        elevCode.setText(presenter.getElev().getElevCode());
        medie.setText(Utils.computeMedie(presenter.getNoteByMaterie()));

        handleRecyclersViews();
        setListeners();
    }

    private void setListeners() {
        adaugaAbsenta = findViewById(R.id.adauga_absenta_btn);
        final EditText sendEditText = findViewById(R.id.send_edittext);
        final FancyButton sendButton = findViewById(R.id.button_chatbox_send);
        adaugaNota = findViewById(R.id.adauga_nota_btn);

        adaugaAbsenta.setOnClickListener(getAdaugaAbsesntaListener());
        adaugaNota.setOnClickListener(getAdaugaNotaListener());
        sendEditText.addTextChangedListener(getWatcher(sendButton));
        sendButton.setOnClickListener(getSendButtonListener(sendEditText));
        noteTitle.setOnClickListener(getnoteTitleListener());
        absenteTitle.setOnClickListener(getAbsenteTitleListener());
        phone.setOnLongClickListener(getPhoneLongListener());
    }

    private View.OnLongClickListener getPhoneLongListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.createEditPhoneDialog(activity);
                return true;
            }
        };
    }

    private View.OnClickListener getnoteTitleListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNoteViewCollapsed) {
                    displayNoteLayout(noteRecycleView);
                } else {
                    hideNoteLayout(noteRecycleView);
                }
            }
        };
    }

    private View.OnClickListener getAbsenteTitleListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAbsenteViewCollapsed) {
                    displayAbsenteLayout(absenteRecycleView);
                } else {
                    hideAbsenteLayout(absenteRecycleView);
                }
            }
        };
    }

    private View.OnClickListener getAdaugaAbsesntaListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_absenta_dialog);
                dialog.setTitle(getResources().getString(R.string.adauga_absenta));
                FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        absenteAdapter.setData(presenter.createAbsenta(context));
                        absenteAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        displayAbsenteLayout(absenteRecycleView);
                    }
                });
                dialog.show();

                FancyButton dialogButtonCancel = dialog.findViewById(R.id.dialogButtonCancel);
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    private View.OnClickListener getAdaugaNotaListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_nota_dialog);
                dialog.setTitle(getResources().getString(R.string.introdu_nota));
                final EditText name = dialog.findViewById(R.id.edit1);
                checkBox = dialog.findViewById(R.id.checkBox1);
                presenter.setCheckBoxVisibility();
                FancyButton dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String notaText = name.getText().toString();
                        if (!notaText.isEmpty()) {
                            boolean isTeza = checkBox.isChecked();
                            presenter.createNota(notaText, isTeza);
                            medie.setText(Utils.computeMedie(Utils.getNoteByYear(presenter.getElev())));
                            noteAdapter.setData(presenter.getNoteByMaterie());
                            noteAdapter.notifyDataSetChanged();
                            displayNoteLayout(noteRecycleView);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        };
    }

    private View.OnClickListener getSendButtonListener(final EditText sendEditText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.createMessage();
                mesajeAdapter.setData(presenter.getMesajeByMaterie());
                mesajeAdapter.notifyDataSetChanged();
                sendEditText.setText("");
                sendEditText.clearFocus();
                //mesajeRecycleView.smoothScrollToPosition(mesajeAdapter.getItemCount()-1);
                Utils.hideKeyboard(activity);
            }
        };
    }

    private TextWatcher getWatcher(final FancyButton sendButton) {
        return new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                presenter.setMessage(s.toString());
                if (s.length() != 0) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }
            }
        };
    }

    private void displayAbsenteLayout(RecyclerView recyclerView) {
        arrowImage2.setImageResource(R.drawable.iconfinder_arrow_uppng);
        recyclerView.setVisibility(View.VISIBLE);
        isAbsenteViewCollapsed = true;
    }

    private void hideAbsenteLayout(RecyclerView recyclerView) {
        arrowImage2.setImageResource(R.drawable.iconfinder_arrow_down);
        recyclerView.setVisibility(View.GONE);
        isAbsenteViewCollapsed = false;
    }

    private void displayNoteLayout(RecyclerView recyclerView) {
        arrowImage.setImageResource(R.drawable.iconfinder_arrow_uppng);
        recyclerView.setVisibility(View.VISIBLE);
        isNoteViewCollapsed = true;
    }

    private void hideNoteLayout(RecyclerView recyclerView) {
        arrowImage.setImageResource(R.drawable.iconfinder_arrow_down);
        recyclerView.setVisibility(View.GONE);
        isNoteViewCollapsed = false;
    }

    private void handleRecyclersViews() {
        noteRecycleView = findViewById(R.id.noteRV);
        noteRecycleView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        noteRecycleView.setLayoutManager(llm);

        ArrayList<Nota> note = presenter.getNoteByMaterie();

        Comparator<Nota> compareByNota = new Comparator<Nota>() {
            @Override
            public int compare(Nota o1, Nota o2) {
                return o2.getData().compareTo(o1.getData());
            }
        };
        Collections.sort(note, compareByNota);

        noteAdapter = new NoteAdapter(this, note);
        noteRecycleView.setAdapter(noteAdapter);

        absenteRecycleView = findViewById(R.id.absRV);
        absenteRecycleView.setHasFixedSize(true);
        LinearLayoutManager allm = new LinearLayoutManager(this);
        absenteRecycleView.setLayoutManager(allm);

        ArrayList<Absenta> absentas = presenter.getAbsenteByMaterie();

        Comparator<Absenta> compareByMedie = new Comparator<Absenta>() {
            @Override
            public int compare(Absenta o1, Absenta o2) {
                return o2.getData().compareTo(o1.getData());
            }
        };
        Collections.sort(absentas, compareByMedie);

        absenteAdapter = new AbsenteAdapter(this, absentas, presenter.getElev());
        absenteRecycleView.setAdapter(absenteAdapter);

        ArrayList<MesajProf> mesajProfs = presenter.getMesajeByMaterie();
        Comparator<MesajProf> compareByDate = new Comparator<MesajProf>() {
            @Override
            public int compare(MesajProf o1, MesajProf o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Collections.sort(mesajProfs, compareByDate);
        mesajeRecycleView = findViewById(R.id.mesajeRV);
        mesajeRecycleView.setHasFixedSize(true);
        LinearLayoutManager allms = new LinearLayoutManager(this);
        mesajeRecycleView.setLayoutManager(allms);
        mesajeAdapter = new MesajeAdapter(this, mesajProfs);
        mesajeRecycleView.setAdapter(mesajeAdapter);
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.activity_elev_details;
    }

}
