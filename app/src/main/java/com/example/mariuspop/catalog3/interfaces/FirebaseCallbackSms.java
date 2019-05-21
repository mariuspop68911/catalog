package com.example.mariuspop.catalog3.interfaces;

import com.example.mariuspop.catalog3.models.SMSGateway;

public interface FirebaseCallbackSms {

    void onSmsGatewayReceived(SMSGateway smsGateway);

}
