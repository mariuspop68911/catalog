package com.example.mariuspop.catalog3.interfaces;

import com.example.mariuspop.catalog3.models.Announcement;

import java.util.ArrayList;

public interface FirebaseCallbackNotifications {

    void onNotificationsReceived(ArrayList<Announcement> announcements);

}
