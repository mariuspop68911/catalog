package com.example.mariuspop.catalog3.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AbsenteManager;
import com.example.mariuspop.catalog3.Constants;
import com.example.mariuspop.catalog3.NotificationManager;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.db.DBHelper;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUserByPhoneNumber;
import com.example.mariuspop.catalog3.main.ElevDetailsActivity;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.Nota;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CustomAdapterElevi extends ArrayAdapter<Elev> {

    private ArrayList<Elev> dataSet;
    private Context mContext;
    private Materie materie;
    private DBHelper dbHelper;
    private Activity activity;
    private boolean showExtra;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        FancyButton adaugaAbsenta;
        FancyButton adaugaNota;
        LinearLayout alertLayout;
        TextView alertText;
    }

    public CustomAdapterElevi(boolean showExtra, Activity activity, ArrayList<Elev> data, Materie materie, DBHelper dbHelper, Context context) {
        super(context, R.layout.elev_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.materie = materie;
        this.dbHelper = dbHelper;
        this.activity = activity;
        this.showExtra = showExtra;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Elev elev = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.elev_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);*/

        viewHolder.txtName.setText(elev.getName());
        viewHolder.adaugaAbsenta = convertView.findViewById(R.id.adaugaAbsenta);
        viewHolder.adaugaNota = convertView.findViewById(R.id.adaugaNota);
        viewHolder.alertLayout = convertView.findViewById(R.id.alert_layout);
        viewHolder.alertText = convertView.findViewById(R.id.alertText);
        if (showExtra) {
            viewHolder.adaugaAbsenta.setVisibility(View.VISIBLE);
            viewHolder.adaugaNota.setVisibility(View.VISIBLE);
            showAlertIcon(elev, viewHolder);
            if (materie != null) {
                viewHolder.adaugaAbsenta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.add_absenta_dialog);
                        View vi = dialog.getWindow().getDecorView();
                        vi.setBackgroundResource(android.R.color.transparent);
                        FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Absenta absenta = new Absenta();
                                absenta.setMotivata(false);
                                absenta.setData(Calendar.getInstance().getTime());
                                absenta.setElevId(elev.getElevId());
                                absenta.setMaterieNume(materie.getName());
                                absenta.setMaterieId(materie.getMaterieId());
                                absenta.setPending(true);
                                ArrayList<Absenta> absente = elev.getAbsente();
                                absente.add(absenta);
                                elev.setAbsente(absente);
                                for (Elev elev1 : AddManager.getInstance().getClasa().getElevi()) {
                                    if (elev.getElevId() == elev1.getElevId()) {
                                        elev1.setAbsente(elev.getAbsente());
                                    }
                                }
                                dbHelper.insertOrUpdateAbsenta(absenta, AddManager.getInstance().getClasa());
                                AbsenteManager.getInstance().scheduleAbsenta(mContext, absenta, elev, AddManager.getInstance().getClasa());
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
                });

                viewHolder.adaugaNota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.add_nota_dialog);
                        dialog.setTitle(activity.getResources().getString(R.string.introdu_nota));
                        final CheckBox checkBox = dialog.findViewById(R.id.checkBox1);
                        if (materie.isTeza() && !checkIfNotaTezaExists(elev, materie.getMaterieId())) {
                            checkBox.setVisibility(View.VISIBLE);
                        } else {
                            checkBox.setVisibility(View.GONE);
                        }
                        final EditText name = dialog.findViewById(R.id.edit1);
                        FancyButton dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String notaText = name.getText().toString();
                                if (!notaText.isEmpty()) {
                                    Nota ultimaNota = new Nota();
                                    ultimaNota.setValue(Integer.valueOf(notaText));
                                    ultimaNota.setMaterieId(materie.getMaterieId());
                                    ultimaNota.setElevId(elev.getElevId());
                                    ultimaNota.setData(Calendar.getInstance().getTime());
                                    ultimaNota.setTeza(checkBox.isChecked());
                                    ArrayList<Nota> notas = elev.getNote();
                                    notas.add(ultimaNota);
                                    elev.setNote(notas);
                                    dbHelper.insertOrUpdateNota(ultimaNota, AddManager.getInstance().getClasa());
                                    /*SmsHandlerManager.getInstance().sendSms(SmsHandlerManager.getInstance()
                                            .createNotaSmsText(elev.getName(), String.valueOf(ultimaNota.getValue()), materie.getName()), elev.getPhoneNumber());*/
                                    NotificationManager.getInstance().sendNotification(elev.getPhoneNumber(),
                                            NotificationManager.getInstance().createNotaNotificationText(elev.getName(),
                                                    String.valueOf(ultimaNota.getValue()), materie.getName()));
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ElevDetailsActivity.class);
                        intent.putExtra(Constants.ELEV_ID, elev.getElevId());
                        intent.putExtra(Constants.CLASA_ID, elev.getClasaId());
                        intent.putExtra(Constants.MATERIE_ID, materie.getMaterieId());
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else {
            viewHolder.adaugaAbsenta.setVisibility(View.GONE);
            viewHolder.adaugaNota.setVisibility(View.GONE);
            viewHolder.alertLayout.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private boolean checkIfNotaTezaExists(Elev elev, long materieId) {
        ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materieId);
        for (Nota nota : notas) {
            if (nota.isTeza()) {
                return true;
            }
        }
        return false;
    }

    private void showAlertIcon(Elev elev, ViewHolder viewHolder) {
        ArrayList<Absenta> absentas = Utils.getAbsenteByMaterieId(elev, materie.getMaterieId());
        ArrayList<Absenta> absenteNemotivate = new ArrayList<>();
        for (Absenta absenta : absentas) {
            if (!absenta.isMotivata()) {
                absenteNemotivate.add(absenta);
            }
        }
        ArrayList<Nota> notas = Utils.getNoteByMaterieId(elev, materie.getMaterieId());
        double medie = Double.valueOf(Utils.computeMedie(notas));
        boolean medieIssues = medie > 0.0 && medie < 5.0;
        String mesaj = "";

        if (absenteNemotivate.size() > Constants.LIMITA_ABSENTE) {
            mesaj = mesaj.concat(mContext.getResources().getString(R.string.limita_absenta));
        }

        if (medieIssues) {
            mesaj = mesaj.isEmpty() ? mesaj.concat(mContext.getResources().getString(R.string.medie_low)) : mesaj.concat(System.lineSeparator()
                    + mContext.getResources().getString(R.string.medie_low));
        }
        String noteMesaj = notEnoughNotes(elev);
        if (!noteMesaj.isEmpty()) {
            mesaj = mesaj.isEmpty() ? mesaj.concat(noteMesaj) : mesaj.concat(System.lineSeparator() + noteMesaj);
        }

        if (absenteNemotivate.size() > 2 || medieIssues || !noteMesaj.isEmpty()) {
            viewHolder.alertText.setText(mesaj);
            viewHolder.alertLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.alertText.setText("");
            viewHolder.alertLayout.setVisibility(View.GONE);
        }
    }

    private String notEnoughNotes(Elev elev) {
        ArrayList<Nota> notasPtElev = Utils.getNoteByMaterieId(elev, materie.getMaterieId());
        if (notasPtElev.size() == 0) {
            int counter = 0;
            for (Elev elevRestul : dataSet) {
                if (elevRestul.getElevId() != elev.getElevId()) {
                    ArrayList<Nota> notasPtRestul = Utils.getNoteByMaterieId(elevRestul, materie.getMaterieId());
                    if (notasPtRestul.size() > 0) {
                        counter++;
                    }
                }
            }
            if (counter > dataSet.size() / 2) {
                return mContext.getResources().getString(R.string.note_una);
            }

        }
        if (notasPtElev.size() == 1) {
            int counter = 0;
            for (Elev elevRestul : dataSet) {
                if (elevRestul.getElevId() != elev.getElevId()) {
                    ArrayList<Nota> notasPtRestul = Utils.getNoteByMaterieId(elevRestul, materie.getMaterieId());
                    if (notasPtRestul.size() > 1) {
                        counter++;
                    }
                }
            }
            if (counter > dataSet.size() / 2) {
                return mContext.getResources().getString(R.string.note_doua);
            }

        }
        return "";
    }
}