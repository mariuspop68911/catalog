package com.example.mariuspop.catalog3.client.MVP;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.MessageManager;
import com.example.mariuspop.catalog3.NotificationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientElev;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;

import java.util.ArrayList;

public class ClientMaterieDetailsPresenter implements FirebaseCallbackClasaById, FirebaseCallbackClientElev {

    private MessageForTeacher messageForTeacher;
    private Activity context;
    private Elev elev = ElevManager.getInstance().getElev();

    private ClientMaterieDetailsView view;

    ClientMaterieDetailsPresenter(Activity context, ClientMaterieDetailsView view) {
        this.context = context;
        this.view = view;
        if (elev == null) {
            FirebaseDb.getElevByCodeNumber(this, PreferencesManager.getStringFromPrefs(Constants.CODE_NUMBER));
        } else {
            view.setToolbarTitle();
        }
    }

    public Elev getElev() {
        return elev;
    }

    void handleSend(String materieNume, long materieId) {
        String mesaj = view.getSendEditText().getText().toString();
        if (!mesaj.isEmpty()) {
            messageForTeacher = new MessageForTeacher();
            messageForTeacher.setMessage(mesaj);
            messageForTeacher.setElevName(elev.getName());
            messageForTeacher.setMaterieName(materieNume);
            messageForTeacher.setMaterieId(String.valueOf(materieId));
            messageForTeacher.setElevId(String.valueOf(elev.getElevId()));
            messageForTeacher.setProf(Constants.MESSAGE_TEACHER_IS_FROM_CLIENT);
            messageForTeacher.setClasaId(String.valueOf(elev.getClasaId()));
            messageForTeacher.setClientToken(PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS));
            setMesajProfToElev(elev);
            view.getMesajeAdapter().setData(ElevManager.getInstance().getMesajeByMaterieId(materieId));
            view.getMesajeAdapter().notifyDataSetChanged();
            view.getSendEditText().setText("");
            view.getSendEditText().clearFocus();
            view.getScrolLayout().fullScroll(View.FOCUS_DOWN);
            Utils.hideKeyboard(context);

            FirebaseDb.getClasaById(this, elev.getClasaId());
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.scrie_mesaj_empty), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClasaReceived(Clasa clasa) {
        if (messageForTeacher != null) {
            messageForTeacher.setTeacherToken(clasa.getScoalaToken());
            for (Elev elev : clasa.getElevi()) {
                if (elev.getElevId() == Long.valueOf(messageForTeacher.getElevId())) {
                    setMesajProfToElev(elev);
                }
            }
            FirebaseDb.saveClasa(clasa);
        }

        NotificationManager.getInstance().sendMessageToTeacher(messageForTeacher);
    }

    private void setMesajProfToElev(Elev elev) {
        ArrayList<MesajProf> mesajProfs = elev.getMesajProfs();
        mesajProfs.add(MessageManager.convertFromMessageForTeacher(messageForTeacher));
        elev.setMesajProfs(mesajProfs);
    }

    @Override
    public void onElevReceived(Elev elev) {
        this.elev = elev;
        ElevManager.getInstance().setElev(elev);
        view.setToolbarTitle();
    }
}
