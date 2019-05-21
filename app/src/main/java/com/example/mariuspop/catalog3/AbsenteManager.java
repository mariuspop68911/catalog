package com.example.mariuspop.catalog3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.AbsentaMessageWrapper;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;

import java.util.ArrayList;
import java.util.HashMap;

public class AbsenteManager {

    private static final AbsenteManager ourInstance = new AbsenteManager();

    private AlarmManager alarmManager;

    private HashMap<Long, PendingIntent> pendingIntents = new HashMap<>();

    public static AbsenteManager getInstance() {
        return ourInstance;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public HashMap<Long, PendingIntent> getPendingIntents() {
        return pendingIntents;
    }

    ArrayList<Absenta> absenteDeTrimis = new ArrayList<>();

    public ArrayList<Absenta> getAbsenteDeTrimis() {
        return absenteDeTrimis == null? new ArrayList<Absenta>() : absenteDeTrimis;
    }

    public void setAbsenteDeTrimis(ArrayList<Absenta> absenteDeTrimis) {
        this.absenteDeTrimis = absenteDeTrimis;
    }

    public void scheduleAbsenta(Context context, Absenta absenta, Elev elev, Clasa clasa){
        Intent intent = new Intent(context, MyReceiver.class);
        AbsentaMessageWrapper absentaMessageWrapper = new AbsentaMessageWrapper();
        absentaMessageWrapper.setElevName(elev.getName());
        absentaMessageWrapper.setNrTel(elev.getPhoneNumber());
        absentaMessageWrapper.setAbsenta(absenta);
        absentaMessageWrapper.setElevId(elev.getElevId());
        absentaMessageWrapper.setClasa(clasa);
        intent.putExtra(Constants.ABS_SERIALISABE, absentaMessageWrapper);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) absenta.getAbsentaId(), intent, 0);
        pendingIntents.put(absenta.getAbsentaId(), pendingIntent);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (10 * 1000), pendingIntent);
        Toast.makeText(context, "Absenta va fi trimisa in 15 minute. Intre timp o poti anula facand tap pe ea.",Toast.LENGTH_LONG).show();
    }
}
