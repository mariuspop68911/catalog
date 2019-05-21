package com.example.mariuspop.catalog3;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

import com.example.mariuspop.catalog3.client.MVP.ClientHomeActivity;
import com.example.mariuspop.catalog3.client.MVP.ClientMaterieDetailsActivity;
import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.main.ElevDetailsActivity;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;
import com.example.mariuspop.catalog3.wizard.AddManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.CATEGORY_SERVICE;

public class FMService extends FirebaseMessagingService implements FirebaseCallbackClasaById {

    public static final String CHANNEL_ONE_ID = "notifications.channel.ONE";

    private MessageForTeacher messageForTeacher;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        //Log.v("TESTLOGG", "FIREBASE From: " + remoteMessage.getData().get("text"));
        if (Constants.IS_SMS_GATEWAY) {
            //SmsManager sms = SmsManager.getDefault();
            //sms.sendTextMessage(remoteMessage.getData().get("phoneNo"), null, remoteMessage.getData().get("text"), null, null);
            if (remoteMessage.getData().get("text").length() > 160) {
                String half1 = remoteMessage.getData().get("text").substring(0, remoteMessage.getData().get("text").length() / 2);
                String half2 = remoteMessage.getData().get("text").substring(remoteMessage.getData().get("text").length() / 2);
                ArrayList<String> parts = new ArrayList<>();
                parts.add(half1);
                parts.add(half2);
                SmsManager.getDefault().sendMultipartTextMessage(remoteMessage.getData().get("phoneNo"), null, parts, null, null);
            } else {
                SmsManager.getDefault().sendTextMessage(remoteMessage.getData().get("phoneNo"), null, remoteMessage.getData().get("text"), null, null);
            }
        } else if (data.get("text") != null && !data.get("text").isEmpty()) {
            Intent notificationIntent = new Intent(this, ClientHomeActivity.class);
            PendingIntent pendingIn = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            sendNotification("Notificare de la liceu", data.get("text"), pendingIn);
        } else {
            messageForTeacher = new MessageForTeacher();
            messageForTeacher.setClasaId(data.get("clasaId"));
            messageForTeacher.setElevId(data.get("elevId"));
            messageForTeacher.setElevName(data.get("elevName"));
            messageForTeacher.setTeacherToken(data.get("teacherToken"));
            messageForTeacher.setMessage(data.get("message"));
            messageForTeacher.setMaterieName(data.get("materieName"));
            messageForTeacher.setMaterieId(data.get("materieId"));
            messageForTeacher.setProf(data.get("prof"));
            messageForTeacher.setClientToken(data.get("clientToken"));

            String title = "";
            PendingIntent pendingIn;
            if (Objects.requireNonNull(data.get("prof")).equals(Constants.MESSAGE_TEACHER_IS_FROM_CLIENT)) {
                Intent notificationIntent = new Intent(this, ElevDetailsActivity.class);
                notificationIntent.putExtra(Constants.MATERIE_ID, Long.valueOf(Objects.requireNonNull(data.get("materieId"))));
                notificationIntent.putExtra(Constants.ELEV_ID, Long.valueOf(Objects.requireNonNull(data.get("elevId"))));
                notificationIntent.putExtra(Constants.CLASA_ID, Long.valueOf(Objects.requireNonNull(data.get("clasaId"))));
                pendingIn = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                title = "Mesaj de la parintele elevului " + remoteMessage.getData().get("elevName")
                        + " pentru profesorul de " + remoteMessage.getData().get("materieName") + ": ";
                FirebaseDb.getClasaById(this, Long.valueOf(remoteMessage.getData().get("clasaId")));
            } else {
                Intent notificationIntent = new Intent(this, ClientMaterieDetailsActivity.class);
                notificationIntent.putExtra(Constants.MATERIE_ID, Long.valueOf(Objects.requireNonNull(data.get("materieId"))));
                notificationIntent.putExtra(Constants.ELEV_ID, Long.valueOf(Objects.requireNonNull(data.get("elevId"))));
                notificationIntent.putExtra(Constants.CLASA_ID, Long.valueOf(Objects.requireNonNull(data.get("clasaId"))));
                notificationIntent.putExtra(Constants.MATERIE_NUME, Objects.requireNonNull(data.get("materieName")));
                pendingIn = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                title = "Mesaj de la profesorul de " + remoteMessage.getData().get("materieName") + ": ";
                FirebaseDb.getClasaById(this, Long.valueOf(remoteMessage.getData().get("clasaId")));
            }
            sendNotification(title, remoteMessage.getData().get("message"), pendingIn);
        }

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        /*if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String notificationTitle, String notificationBody, PendingIntent pendingIn) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationManager notifyManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            createChannel(Objects.requireNonNull(notifyManager));

            android.support.v4.app.NotificationCompat.Builder notificationCompat =
                    new android.support.v4.app.NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                            .setContentTitle(notificationTitle)
                            .setSmallIcon(R.drawable.alert_icon)
                            .setContentText(notificationBody)
                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                            .setContentIntent(pendingIn)
                            .setCategory(CATEGORY_SERVICE)
                            .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_MAX)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notificationBody))
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            notifyManager.notify(0, notificationCompat.build());
        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notificationBody))
                    .setContentIntent(pendingIn);

            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Objects.requireNonNull(notificationManager).notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

    @TargetApi(26)
    private void createChannel(android.app.NotificationManager notificationManager) {
        String name = "Application activities";
        String description = "Notifications for activities";
        int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onClasaReceived(Clasa clasa) {
        if (messageForTeacher.getProf().equals("1")) {
            for (Elev elev : clasa.getElevi()) {
                if (elev.getElevId() == Long.valueOf(messageForTeacher.getElevId())) {
                    ElevManager.getInstance().setElev(elev);
                }
            }
        } else {
            AddManager.getInstance().setClasa(clasa);
        }
    }
}
