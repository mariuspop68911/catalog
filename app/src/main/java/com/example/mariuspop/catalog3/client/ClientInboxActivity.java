package com.example.mariuspop.catalog3.client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mariuspop.catalog3.AppActivity;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.adapters.ClientInboxMessagesAdapter;
import com.example.mariuspop.catalog3.adapters.InboxMessagesAdapter;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackNotifications;
import com.example.mariuspop.catalog3.models.Announcement;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.InboxMessage;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mehdi.sakout.fancybuttons.FancyButton;

public class ClientInboxActivity extends AppActivity implements FirebaseCallbackNotifications {

    private Activity activity;
    private ClientInboxMessagesAdapter mesajeAdapter;
    private Comparator<InboxMessage> compareByDate;
    private Elev elev;
    private RecyclerView mesajeRecycleView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        final EditText sendEditText = findViewById(R.id.send_edittext);
        final FancyButton sendButton = findViewById(R.id.button_chatbox_send);

        sendEditText.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);

        mesajeRecycleView = findViewById(R.id.inbox_lista);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) mesajeRecycleView.getLayoutParams();
        marginLayoutParams.setMargins(0, 20, 0, 20);
        mesajeRecycleView.setLayoutParams(marginLayoutParams);

        mesajeRecycleView.setHasFixedSize(true);
        LinearLayoutManager allms = new LinearLayoutManager(this);
        mesajeRecycleView.setLayoutManager(allms);

        elev = ElevManager.getInstance().getElev();

        compareByDate = new Comparator<InboxMessage>() {
            @Override
            public int compare(InboxMessage o1, InboxMessage o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        };

        FirebaseDb.getNotificationsByClasaId(this, elev.getClasaId());

    }

    private ArrayList<InboxMessage> getMessages(ArrayList<Announcement> announcements) {
        ArrayList<InboxMessage> inboxMessages = new ArrayList<>();

        for (MesajProf mesajProf : elev.getMesajProfs()) {
            if (mesajProf.isProf()) {
                InboxMessage inboxMessage = new InboxMessage();
                inboxMessage.setElevId(mesajProf.getElevId());
                inboxMessage.setMaterieId(mesajProf.getMaterieId());
                inboxMessage.setDate(mesajProf.getDate());
                inboxMessage.setFrom(mesajProf.isProf() ? mesajProf.getMaterieName() : mesajProf.getElevName());
                inboxMessage.setTo(!mesajProf.isProf() ? mesajProf.getMaterieName() : mesajProf.getElevName());
                inboxMessage.setProf(mesajProf.isProf());
                inboxMessage.setMessage(mesajProf.getMessage());
                inboxMessages.add(inboxMessage);
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

    @Override
    public void onNotificationsReceived(ArrayList<Announcement> announcements) {
        ArrayList<InboxMessage> inboxMessages = getMessages(announcements);

        Collections.sort(inboxMessages, compareByDate);
        mesajeAdapter = new ClientInboxMessagesAdapter(this, inboxMessages);
        mesajeRecycleView.setAdapter(mesajeAdapter);
    }

    @Override
    public int getContentAreaLayoutId() {
        return R.layout.sc_inbox_activity;
    }

}