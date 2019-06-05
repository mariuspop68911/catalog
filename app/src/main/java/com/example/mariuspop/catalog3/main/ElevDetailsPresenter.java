package com.example.mariuspop.catalog3.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.mariuspop.catalog3.AbsenteManager;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.FirebaseRm;
import com.example.mariuspop.catalog3.MessageManager;
import com.example.mariuspop.catalog3.NotificationManager;
import com.example.mariuspop.catalog3.PreferencesManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUser;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUserByPhoneNumber;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.ClientUser;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.Nota;
import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class ElevDetailsPresenter implements FirebaseCallbackClientUser, FirebaseCallbackClasaById, FirebaseCallbackClientUserByPhoneNumber {

    private Clasa clasa;
    private Elev elev;
    private MessageForTeacher messageForTeacher;
    private long elevId;
    private long materieId;
    private Materie materie;
    private String message = "";
    private String clasaIdString;

    private ElevDetailsView view;

    private FirebaseCallbackClientUserByPhoneNumber callbackClientUserByPhoneNumber;
    private FirebaseCallbackClientUser firebaseCallbackClientUser;
    private String newPhoneNumber;

    ElevDetailsPresenter(long elevId, long materieId, String clasaIdString, ElevDetailsView view) {

        this.elevId = elevId;
        this.materieId = materieId;
        this.view = view;
        this.clasaIdString = clasaIdString;
        callbackClientUserByPhoneNumber = this;
        firebaseCallbackClientUser = this;
        clasa = AddManager.getInstance().getClasa();
    }

    public void setData() {
        if (clasa != null) {
            handle();
        } else {
            if (clasaIdString != null && !clasaIdString.isEmpty()) {
                FirebaseDb.getClasaById(this, Long.valueOf(clasaIdString));
            } else {
                clasaIdString = PreferencesManager.getStringFromPrefs(Constants.CURRENT_CLASS);
                FirebaseDb.getClasaById(this, Long.valueOf(clasaIdString));
            }
        }
    }

    public Elev getElev() {
        return elev;
    }

    public Materie getMaterie() {
        return materie;
    }

    public Clasa getClasa() {
        return clasa;
    }

    void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void onClientUserReceived(ClientUser clientUser) {
        if (messageForTeacher != null && clientUser != null) {
            messageForTeacher.setClientToken(clientUser.getToken());
            FirebaseDb.saveClasa(clasa);
        } else {
           //Toast.makeText(view.getCheckBox().getContext(), "Mesajul nu a fost trimis. Parintele elevului nu are aplicatia instalata", Toast.LENGTH_LONG).show();
        }
        NotificationManager.getInstance().sendMessageToTeacher(messageForTeacher);
    }

    @Override
    public void onClasaReceived(Clasa clasaWs) {
        clasa = clasaWs;
        view.setToolbarTitle();
        handle();
    }

    private void handle() {
        for (Elev elevH : clasa.getElevi()) {
            if (elevH.getElevId() == elevId) {
                elev = elevH;
            }
        }

        for (Materie materieLoop : Utils.getMateriiByThisYear(clasa)) {
            if (materieLoop.getMaterieId() == materieId) {
                materie = materieLoop;
            }
        }

        view.setDataOnViews();
    }

    ArrayList<Absenta> getAbsenteByMaterie() {
        return Utils.getAbsenteByMaterieId(elev, materieId, FirebaseRm.getCurrentSemesterForced());
    }

    ArrayList<MesajProf> getMesajeByMaterie() {
        ArrayList<MesajProf> mesajeMaterie = new ArrayList<>();
        if (elev.getMesajProfs() != null) {
            for (MesajProf mesajProf : elev.getMesajProfs()) {
                if (mesajProf.getMaterieId() == materie.getMaterieId()) {
                    mesajeMaterie.add(mesajProf);
                }
            }
        }
        return mesajeMaterie;
    }

    ArrayList<Nota> getNoteByMaterie() {
        return Utils.getNoteByMaterieId(elev, materieId, FirebaseRm.getCurrentSemesterForced());
    }

    ArrayList<Absenta> createAbsenta(Context context) {
        Absenta absenta = new Absenta();
        absenta.setMotivata(false);
        absenta.setData(Calendar.getInstance().getTime());
        absenta.setElevId(elev.getElevId());
        absenta.setMaterieNume(materie.getName());
        absenta.setMaterieId(materie.getMaterieId());
        absenta.setPending(true);
        absenta.setYear(Integer.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_YEAR)));
        absenta.setSem(FirebaseRm.getCurrentSemesterForced());
        ArrayList<Absenta> absente = elev.getAbsente();
        absente.add(absenta);
        elev.setAbsente(absente);
        for (Elev elev1 : clasa.getElevi()) {
            if (elev.getElevId() == elev1.getElevId()) {
                elev1.setAbsente(Utils.getAbsenteByYear(elev));
            }
        }
        view.getDB().insertOrUpdateAbsenta(absenta, clasa);
        AbsenteManager.getInstance().scheduleAbsenta(context, absenta, elev, clasa);
        return Utils.getAbsenteByMaterieId(elev, materieId, FirebaseRm.getCurrentSemesterForced());
    }

    void createNota(String notaText, boolean isTeza) {
        Nota ultimaNota = new Nota();
        ultimaNota.setValue(Integer.valueOf(notaText));
        ultimaNota.setMaterieId(materie.getMaterieId());
        ultimaNota.setElevId(elev.getElevId());
        ultimaNota.setYear(Integer.valueOf(PreferencesManager.getStringFromPrefs(Constants.CURRENT_YEAR)));
        ultimaNota.setData(Calendar.getInstance().getTime());
        ultimaNota.setTeza(isTeza);
        ultimaNota.setSem(FirebaseRm.getCurrentSemesterForced());
        ArrayList<Nota> notas = elev.getNote();
        notas.add(ultimaNota);
        elev.setNote(notas);
        view.getDB().insertOrUpdateNota(ultimaNota, clasa);
                            /*SmsHandlerManager.getInstance().sendSms(SmsHandlerManager.getInstance()
                                    .createNotaSmsText(elev.getName(), String.valueOf(ultimaNota.getValue()), materie.getName()), elev.getPhoneNumber());*/
        NotificationManager.getInstance().sendNotification(elev.getPhoneNumber(),
                NotificationManager.getInstance().createNotaNotificationText(elev.getName(),
                        String.valueOf(ultimaNota.getValue()), materie.getName()));
    }

    void createMessage() {
        messageForTeacher = new MessageForTeacher();
        messageForTeacher.setMessage(message);
        messageForTeacher.setElevName(elev.getName());
        messageForTeacher.setMaterieName(materie.getName());
        messageForTeacher.setMaterieId(String.valueOf(materie.getMaterieId()));
        messageForTeacher.setElevId(String.valueOf(elev.getElevId()));
        messageForTeacher.setProf(Constants.MESSAGE_TEACHER_IS_FROM_TEACHER);
        messageForTeacher.setClasaId(String.valueOf(elev.getClasaId()));
        messageForTeacher.setTeacherToken(PreferencesManager.getStringFromPrefs(Constants.FIREBASE_TOCKEN_PREFS));
        ArrayList<MesajProf> mesajProfs = elev.getMesajProfs();
        mesajProfs.add(MessageManager.convertFromMessageForTeacher(messageForTeacher));
        elev.setMesajProfs(mesajProfs);
        FirebaseDb.getClientUserByPhoneNumber(firebaseCallbackClientUser, elev.getPhoneNumber());
    }

    void setCheckBoxVisibility() {
        if (materie.isTeza() && !checkIfNotaTezaExists()) {
            if (view.getCheckBox() != null) {
                view.getCheckBox().setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getCheckBox() != null) {
                view.getCheckBox().setVisibility(View.GONE);
            }
        }
    }

    private boolean checkIfNotaTezaExists() {
        ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materieId, FirebaseRm.getCurrentSemesterForced());
        for (Nota nota : notas) {
            if (nota.isTeza()) {
                return true;
            }
        }
        return false;
    }

    void createEditPhoneDialog(Activity activity) {
        final Dialog dialog = Utils.createDialog(activity, R.layout.editeaza_phone_dialog, elev.getName());
        final EditText phoneEdit = dialog.findViewById(R.id.edit1);
        phoneEdit.setText(elev.getPhoneNumber());
        phoneEdit.setSelection(phoneEdit.getText().length());
        FancyButton okButton = dialog.findViewById(R.id.dialogButtonOK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhoneNumber = phoneEdit.getText().toString();
                if (!newPhoneNumber.isEmpty()) {
                    String oldNumber = elev.getPhoneNumber();
                    for (Elev elevLoop : AddManager.getInstance().getClasa().getElevi()) {
                        if (elevLoop.getElevId() == elev.getElevId()) {
                            elevLoop.setPhoneNumber(newPhoneNumber);
                        }
                    }
                    FirebaseDb.saveClasa(AddManager.getInstance().getClasa());
                    FirebaseDb.getClientUserByPhoneNumber(callbackClientUserByPhoneNumber, oldNumber);
                    view.getPhone().setText(newPhoneNumber);
                    dialog.dismiss();
                }
            }
        });
        FancyButton cancelButton = dialog.findViewById(R.id.dialogButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    @Override
    public void onClientUserPhoneReceived(ClientUser clientUser) {
        if (clientUser != null) {
            FirebaseDb.removeClientUserByPhoneNumber(clientUser.getPhoneNumber());
            clientUser.setPhoneNumber(newPhoneNumber);
            FirebaseDb.saveClientUser(clientUser);
        }
    }
}
