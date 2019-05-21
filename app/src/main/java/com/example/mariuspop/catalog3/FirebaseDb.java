package com.example.mariuspop.catalog3;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClase;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientElev;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUser;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClientUserByPhoneNumber;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackInstitutie;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackSms;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.ClientUser;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.mesaje.FirebaseMessage;
import com.example.mariuspop.catalog3.models.Institutie;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;
import com.example.mariuspop.catalog3.models.SMSGateway;
import com.example.mariuspop.catalog3.models.ScUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseDb {

    public static void saveSmsGateway(SMSGateway smsGateway) {
        PreferencesManager.saveStringToPrefs(Constants.SMSGATWAY_PHONE_NUMBER, smsGateway.getPhoneNo());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("smsgatway/" + smsGateway.getPhoneNo());
        myRef.setValue(smsGateway);
    }

    public static void getSmsGatewayByPhoneNumber(final FirebaseCallbackSms firebaseCallbackSms, final String phoneNumber) {
        final SMSGateway smsGateway = null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("smsgatway");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SMSGateway value = snapshot.getValue(SMSGateway.class);
                    if (Objects.requireNonNull(value).getPhoneNo().equals(phoneNumber)) {
                        firebaseCallbackSms.onSmsGatewayReceived(value);
                    }
                    Log.v("TESTLOGG", " from db " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void removeSmsGatewayByPhoneNumber(final String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("smsgatway");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SMSGateway value = snapshot.getValue(SMSGateway.class);
                    if (Objects.requireNonNull(value).getPhoneNo().equals(phoneNumber)) {
                        snapshot.getRef().removeValue();
                        PreferencesManager.removeStringToPrefs(Constants.SMSGATWAY_PHONE_NUMBER);
                    }
                    Log.v("TESTLOGG", " from db " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void sendSms(String phoneNo, String textMessage, String toFirebaseToken) {
        FirebaseMessage firebaseMessage = new FirebaseMessage();
        firebaseMessage.setText(textMessage);
        firebaseMessage.setToken(toFirebaseToken);
        firebaseMessage.setPhoneNo(phoneNo);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("send");
        myRef.setValue(firebaseMessage);
    }

    public static void sendMessageTeaacher(MessageForTeacher messageForTeacher) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sendTeacher");
        myRef.setValue(messageForTeacher);
    }

    public static void saveUser(ScUser scUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + scUser.getUserId());
        myRef.setValue(scUser);
    }

    public static void saveInstitute(Institutie institutie) {
        PreferencesManager.saveStringToPrefs(Constants.INSTITUTE_NAME, String.valueOf(institutie.getNume()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("institutions/" + institutie.getUserId());
        newMyRef.setValue(institutie);
    }

    public static void getInstituteByUserId(final FirebaseCallbackInstitutie firebaseCallbackInstitutie, final String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("institutions");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Institutie value = snapshot.getValue(Institutie.class);
                    if (Objects.requireNonNull(value).getUserId().equals(userId)) {
                        firebaseCallbackInstitutie.onInstitutieReceived(value);
                        return;
                    }
                    Log.v("TESTLOGG", " from db Inst " + value);
                }
                firebaseCallbackInstitutie.onInstitutieReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void getClaseByUserId(final FirebaseCallbackClase firebaseCallbackClase, final String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clase");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Clasa> clase = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clasa value = snapshot.getValue(Clasa.class);
                    if (Objects.requireNonNull(value).getUserId().equals(userId)) {
                        clase.add(value);
                    }
                    Log.v("TESTLOGG", " from db clasa " + value);
                }
                firebaseCallbackClase.onClaseReceived(clase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void getClasaById(final FirebaseCallbackClasaById firebaseCallbackClasaById, final long id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clase");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clasa value = snapshot.getValue(Clasa.class);
                    if (Objects.requireNonNull(value).getClasaId() == id) {
                        firebaseCallbackClasaById.onClasaReceived(value);
                        return;
                    }
                    Log.v("TESTLOGG", " from db clasa " + value);
                }
                firebaseCallbackClasaById.onClasaReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void saveClasa(Clasa clasa) {
        PreferencesManager.saveStringToPrefs(Constants.CURRENT_CLASS, String.valueOf(clasa.getClasaId()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clase/" + clasa.getClasaId());
        myRef.setValue(clasa);
    }

    public static void getElevByPhoneNumber(final FirebaseCallbackClientElev firebaseCallbackClientElev, final String phoneNo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clase");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Clasa> clase = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clasa value = snapshot.getValue(Clasa.class);
                    clase.add(value);
                    Log.v("TESTLOGG", " from db clasa " + value);
                }
                for (Clasa clasa : clase) {
                    for (Elev elev : clasa.getElevi()) {
                        if (elev.getPhoneNumber().equals(phoneNo)) {
                            firebaseCallbackClientElev.onElevReceived(elev);
                            return;
                        }
                    }
                }
                firebaseCallbackClientElev.onElevReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void getElevByCodeNumber(final FirebaseCallbackClientElev firebaseCallbackClientElev, final String codeNo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clase");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Clasa> clase = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Clasa value = snapshot.getValue(Clasa.class);
                    clase.add(value);
                    Log.v("TESTLOGG", " from db clasa " + value);
                }
                for (Clasa clasa : clase) {
                    for (Elev elev : clasa.getElevi()) {
                        if (elev.getElevCode().equals(codeNo)) {
                            firebaseCallbackClientElev.onElevReceived(elev);
                            return;
                        }
                    }
                }
                firebaseCallbackClientElev.onElevReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void saveClientUser(ClientUser clientUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("clientUser/" + clientUser.getPhoneNumber());
        myRef.setValue(clientUser);
    }

    public static void getClientUserByPhoneNumber(final FirebaseCallbackClientUser firebaseCallbackClientUser, final String phoneNo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clientUser");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClientUser value = snapshot.getValue(ClientUser.class);
                    if (Objects.requireNonNull(value).getPhoneNumber().equals(phoneNo)) {
                        firebaseCallbackClientUser.onClientUserReceived(value);
                        return;
                    }
                    Log.v("TESTLOGG", " from db ClientUser " + value);
                }
                firebaseCallbackClientUser.onClientUserReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void getClientUserByPhoneNumber(final FirebaseCallbackClientUserByPhoneNumber firebaseCallbackClientUserByPhoneNumber, final String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clientUser");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClientUser value = snapshot.getValue(ClientUser.class);
                    if (Objects.requireNonNull(value).getPhoneNumber().equals(phoneNumber)) {
                        firebaseCallbackClientUserByPhoneNumber.onClientUserPhoneReceived(value);
                        return;
                    }
                    Log.v("TESTLOGG", " from db ClientUser " + value);
                }
                firebaseCallbackClientUserByPhoneNumber.onClientUserPhoneReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

    public static void removeClientUserByPhoneNumber(final String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newMyRef = database.getReference("clientUser");
        newMyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClientUser value = snapshot.getValue(ClientUser.class);
                    if (Objects.requireNonNull(value).getPhoneNumber().equals(phoneNumber)) {
                        snapshot.getRef().removeValue();
                    }
                    Log.v("TESTLOGG", " from db " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("TESTLOGG", " ERROR ");
            }
        });
    }

}
