package com.example.mariuspop.catalog3;

import com.example.mariuspop.catalog3.models.mesaje.MesajProf;
import com.example.mariuspop.catalog3.models.mesaje.MessageForTeacher;

import java.util.Calendar;
import java.util.Date;

public class MessageManager {

    public static MesajProf convertFromMessageForTeacher(MessageForTeacher messageForTeacher) {
        MesajProf mesajProf = new MesajProf();
        mesajProf.setMesajId(Calendar.getInstance().getTimeInMillis());
        mesajProf.setElevName(messageForTeacher.getElevName());
        mesajProf.setMaterieName(messageForTeacher.getMaterieName());
        mesajProf.setElevId(Long.valueOf(messageForTeacher.getElevId()));
        mesajProf.setMaterieId(Long.valueOf(messageForTeacher.getMaterieId()));
        mesajProf.setMessage(messageForTeacher.getMessage());
        mesajProf.setProf(!messageForTeacher.getProf().equals(Constants.MESSAGE_TEACHER_IS_FROM_CLIENT));
        mesajProf.setDate(new Date());
        return mesajProf;
    }

}
