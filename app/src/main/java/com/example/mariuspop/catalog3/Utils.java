package com.example.mariuspop.catalog3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mariuspop.catalog3.client.ElevManager;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.MediiWrapper;
import com.example.mariuspop.catalog3.models.Nota;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import mehdi.sakout.fancybuttons.FancyButton;

public class Utils {

    public static long generateRandomId() {
        return Calendar.getInstance().getTimeInMillis() + new Random().nextInt(1000);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTime(Date date){
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date.getTime());
    }

    public static String computeMedie(ArrayList<Nota> notas) {
        if (notas != null && notas.size() > 1) {
            double sumaNote = 0;
            int divider = 0;
            Nota notaTeza = null;
            for (int i = 0; i < notas.size(); i++) {
                if (!notas.get(i).isTeza()) {
                    divider = divider + 1;
                    sumaNote = sumaNote + notas.get(i).getValue();
                } else {
                    notaTeza = notas.get(i);
                }
            }
            double medie = sumaNote / divider;
            if (notaTeza != null) {
                medie = ((medie * 3) + notaTeza.getValue()) / 4;
            }
            DecimalFormat df2 = new DecimalFormat(".##");
            return df2.format(medie);
        } else {
            return "0.0";
        }
    }

    public static ArrayList<Nota> getNoteByMaterieId(Elev elev, long materieId) {
        ArrayList<Nota> noteByMaterie = new ArrayList<>();
        for (Nota nota : elev.getNote()) {
            if (materieId == nota.getMaterieId()) {
                noteByMaterie.add(nota);
            }
        }
        return noteByMaterie;
    }

    public static ArrayList<Absenta> getAbsenteByMaterieId(Elev elev, long materieId) {
        ArrayList<Absenta> absenteByMaterie = new ArrayList<>();
        for (Absenta absenta : elev.getAbsente()) {
            if (materieId == absenta.getMaterieId()) {
                absenteByMaterie.add(absenta);
            }
        }
        return absenteByMaterie;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getMedieGenerala(Clasa clasa, Elev elev) {

        ArrayList<MediiWrapper> mediiWrappers = new ArrayList<>();
        for (Materie materie : clasa.getMaterii()) {
            String media = Utils.computeMedie(Utils.getNoteByMaterieId(elev, materie.getMaterieId()));
            MediiWrapper mediiWrapper = new MediiWrapper();
            mediiWrapper.setMedie(Double.valueOf(media));
            mediiWrapper.setNumeMaterie(materie.getName());
            mediiWrappers.add(mediiWrapper);
        }

        Comparator<MediiWrapper> compareByMedie = new Comparator<MediiWrapper>() {
            @Override
            public int compare(MediiWrapper o1, MediiWrapper o2) {
                return o1.getMedie().compareTo(o2.getMedie());
            }
        };
        ArrayList<MediiWrapper> mediiWrappersOnlyNonZeros = new ArrayList<>();
        for (MediiWrapper mediiWrapper : mediiWrappers) {
            if (mediiWrapper.getMedie() > 0.0) {
                mediiWrappersOnlyNonZeros.add(mediiWrapper);
            }
        }
        Collections.sort(mediiWrappersOnlyNonZeros, compareByMedie);


        double medieGeneralaSuma = 0.0;
        int counter = 0;
        for (MediiWrapper mediiWrapper : mediiWrappersOnlyNonZeros) {
            counter++;
            medieGeneralaSuma = medieGeneralaSuma + mediiWrapper.getMedie();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(medieGeneralaSuma / counter);
    }

    public static Dialog createDialog(Activity activity, int layout, String title){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);
        dialog.setTitle(title);
        return dialog;
    }

    public static boolean isToday(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return true;
        } else {
            return false;
        }
    }

}
