package com.example.mariuspop.catalog3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.AbsentaMessageWrapper;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.wizard.AddManager;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AbsentaMessageWrapper absentaMessageWrapper = (AbsentaMessageWrapper) intent.getExtras().getSerializable(Constants.ABS_SERIALISABE);

        NotificationManager.getInstance().sendNotificationAbsenta(absentaMessageWrapper.getNrTel(),
                NotificationManager.getInstance().createAbsentaNotificationText(absentaMessageWrapper.getElevName(),
                        Utils.getTime(absentaMessageWrapper.getAbsenta().getData()), absentaMessageWrapper.getAbsenta().getMaterieNume()));

        Clasa clasa = AddManager.getInstance().getClasa();
        for (Elev elev : clasa.getElevi()) {
            if(elev.getElevId() == absentaMessageWrapper.getElevId()){
                for (Absenta absenta : Utils.getAbsenteByYear(elev)) {
                    if (absenta.getAbsentaId() == absentaMessageWrapper.getAbsenta().getAbsentaId()) {
                        absenta.setPending(false);
                    }
                }
            }
        }
        AddManager.getInstance().setClasa(clasa);
        FirebaseDb.saveClasa(clasa);

        Toast.makeText(context, "Alarm Triggered" + absentaMessageWrapper.getAbsenta().getMaterieNume(), Toast.LENGTH_SHORT).show();
    }
}
