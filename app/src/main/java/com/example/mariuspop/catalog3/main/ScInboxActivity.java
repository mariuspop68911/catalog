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
import android.widget.EditText;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.adapters.InboxMessagesAdapter;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackNotifications;
import com.example.mariuspop.catalog3.models.Announcement;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.InboxMessage;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

public class ScInboxActivity extends AppActivity implements FirebaseCallbackNotifications {

    private Activity activity;
    private InboxMessagesAdapter mesajeAdapter;
    private Comparator<InboxMessage> compareByDate;
    private Clasa clasa;
    private RecyclerView mesajeRecycleView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        final EditText sendEditText = findViewById(R.id.send_edittext);
        final FancyButton sendButton = findViewById(R.id.button_chatbox_send);
        sendEditText.addTextChangedListener(getWatcher(sendButton));
        sendButton.setOnClickListener(getSendButtonListener(sendEditText));

        mesajeRecycleView = findViewById(R.id.inbox_lista);
        mesajeRecycleView.setHasFixedSize(true);
        LinearLayoutManager allms = new LinearLayoutManager(this);
        mesajeRecycleView.setLayoutManager(allms);

        clasa = AddManager.getInstance().getClasa();

        compareByDate = new Comparator<InboxMessage>() {
            @Override
            public int compare(InboxMessage o1, InboxMessage o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };

        FirebaseDb.getNotificationsByClasaId(this, clasa.getClasaId());

    }

    private ArrayList<InboxMessage> getMessages(ArrayList<Announcement> announcements) {
        ArrayList<InboxMessage> inboxMessages = new ArrayList<>();

        for (Elev elev : clasa.getElevi()) {
            for (MesajProf mesajProf : elev.getMesajProfs()) {
                if (!mesajProf.isProf()) {
                    InboxMessage inboxMessage = new InboxMessage();
                    inboxMessage.setElevId(mesajProf.getElevId());
                    inboxMessage.setMaterieId(mesajProf.getMaterieId());
                    inboxMessage.setDate(mesajProf.getDate());
                    inboxMessage.setFrom(mesajProf.isProf()? mesajProf.getMaterieName() : mesajProf.getElevName());
                    inboxMessage.setTo(!mesajProf.isProf()? mesajProf.getMaterieName() : mesajProf.getElevName());
                    inboxMessage.setProf(mesajProf.isProf());
                    inboxMessage.setMessage(mesajProf.getMessage());
                    inboxMessages.add(inboxMessage);
                }
            }
        }

        for (Announcement announcement : announcements) {
            InboxMessage inboxMessage = new InboxMessage();
            inboxMessage.setDate(announcement.getDate());
            inboxMessage.setMessage(announcement.getText());
            inboxMessages.add(inboxMessage);
        }

        return inboxMessages;

    }

    @Override
    protected void setToolbarTitle() {
        toolbar.setTitle(getResources().getString(R.string.mess));
    }

    private View.OnClickListener getSendButtonListener(final EditText sendEditText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_anunt_dialog);
                //dialog.setTitle(getResources().getString(R.string.trimite_anunt));
                FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = sendEditText.getText().toString();
                        if(!message.isEmpty()) {
                            Announcement announcement = new Announcement();
                            announcement.setClasaId(clasa.getClasaId());
                            announcement.setDate(new Date());
                            announcement.setText(message);
                            FirebaseDb.saveAnnouncement(announcement);
                            InboxMessage inboxMessage = new InboxMessage();
                            inboxMessage.setDate(announcement.getDate());
                            inboxMessage.setMessage(announcement.getText());
                            ArrayList<InboxMessage> inboxMessages = mesajeAdapter.getData();
                            inboxMessages.add(inboxMessage);
                            Collections.sort(inboxMessages, compareByDate);

                            mesajeAdapter.setData(inboxMessages);
                            mesajeAdapter.notifyDataSetChanged();
                        }
                        sendEditText.setText("");
                        sendEditText.clearFocus();
                        Utils.hideKeyboard(activity);
                        dialog.dismiss();
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
                //presenter.setMessage(s.toString());
                if (s.length() != 0) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    public void onNotificationsReceived(ArrayList<Announcement> announcements) {
        ArrayList<InboxMessage> inboxMessages = getMessages(announcements);

        Collections.sort(inboxMessages, compareByDate);
        mesajeAdapter = new InboxMessagesAdapter(this, inboxMessages);
        mesajeRecycleView.setAdapter(mesajeAdapter);
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.sc_inbox_activity;
    }

}