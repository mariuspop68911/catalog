package com.example.mariuspop.catalog3;

import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUser;
import com.example.mariuspop.catalog3.models.ClientUser;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;

public class NotificationManager implements FirebaseCallbackClientUser {

    private static NotificationManager ourInstance;
    private String phoneNo;
    private String textMessage;

    public static NotificationManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new NotificationManager();
        }
        return ourInstance;
    }

    public void sendMessageToTeacher(MessageForTeacher messageForTeacher) {
        textMessage = "Mesaj de la parintele elevului " + messageForTeacher.getElevName() + " pentru profesorul de " + messageForTeacher.getMaterieName() + ": " + messageForTeacher.getMessage();
        FirebaseDb.sendMessageTeaacher(messageForTeacher);
    }

    public void sendNotification(String phoneNo, String textMessage) {
        this.phoneNo = phoneNo;
        this.textMessage = textMessage;
        FirebaseDb.getClientUserByPhoneNumber(this, phoneNo);
    }

    public void sendNotificationAbsenta(String phoneNo, String textMessage) {
        this.phoneNo = phoneNo;
        this.textMessage = textMessage;
        FirebaseDb.getClientUserByPhoneNumber(this, phoneNo);
    }

    public String createNotaNotificationText(String numeElev, String nota, String materie) {
        return "Elevul " + numeElev + " a primit nota " + nota + " la materia " + materie + ".";
    }

    public String createAbsentaNotificationText(String numeElev, String ora, String materie) {
        return "Elevul " + numeElev + " a lipsit la materia " + materie + ", ora: " + ora + ".";
    }

    @Override
    public void onClientUserReceived(ClientUser clientUser) {
        if (clientUser != null) {
            FirebaseDb.sendSms(phoneNo, textMessage, clientUser.getToken());
        }
    }
}
