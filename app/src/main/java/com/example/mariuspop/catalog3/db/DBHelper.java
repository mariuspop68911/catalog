package com.example.mariuspop.catalog3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.Utils;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.models.Materie;
import com.example.mariuspop.catalog3.models.Nota;

import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catalog.db";

    //-----------------------------------------------------------------------------------
    private static final String CLASA_TABLE_NAME = "clasa";

    private static final String CLASA_COLUMN_ID = "clasaId";
    private static final String CLASA_COLUMN_INSTITUTIE_ID = "clasaInstitutieId";
    private static final String CLASA_COLUMN_NUME = "clasaNume";
    private static final String CLASA_COLUMN_INSTITUTIE_NAME = "institutieName";

    //-----------------------------------------------------------------------------------

    private static final String MATERIE_TABLE_NAME = "materie";

    private static final String MATERIE_COLUMN_ID = "materieId";
    private static final String MATERIE_COLUMN_CLASA_ID = "materieClasaId";
    private static final String MATERIE_COLUMN_NUME = "materieNume";

    //-----------------------------------------------------------------------------------

    private static final String ELEV_TABLE_NAME = "elev";

    private static final String ELEV_COLUMN_ID = "elevId";
    private static final String ELEV_COLUMN_CLASA_ID = "clasaId";
    private static final String ELEV_COLUMN_NUME = "name";
    private static final String ELEV_COLUMN_PHONE = "phoneNumber";

    //-----------------------------------------------------------------------------------

    private static final String NOTA_TABLE_NAME = "nota";

    private static final String NOTA_COLUMN_ID = "notaId";
    private static final String NOTA_COLUMN_ELEV_ID = "elevId";
    private static final String NOTA_COLUMN_MATERIE_ID = "materieId";
    private static final String NOTA_COLUMN_DATA = "data";
    private static final String NOTA_COLUMN_VALUE = "value";

    //-----------------------------------------------------------------------------------

    private static final String ABSENTA_TABLE_NAME = "absenta";

    private static final String ABSENTA_COLUMN_ID = "absentaId";
    private static final String ABSENTA_COLUMN_ELEV_ID = "absentaElevId";
    private static final String ABSENTA_COLUMN_MATERIE_ID = "absentaMaterieId";
    private static final String ABSENTA_COLUMN_DATA = "absentaData";
    private static final String ABSENTA_COLUMN_MOTIVATA = "absentaMotivata";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CLASA_TABLE_NAME + " " + "(" + CLASA_COLUMN_ID + " long primary key, " + CLASA_COLUMN_INSTITUTIE_ID
                + " long," + CLASA_COLUMN_NUME + " text," + CLASA_COLUMN_INSTITUTIE_NAME + " text)");
        db.execSQL("create table " + MATERIE_TABLE_NAME + " " + "(" + MATERIE_COLUMN_ID + " long primary key, " + MATERIE_COLUMN_CLASA_ID
                + " long," + MATERIE_COLUMN_NUME + " text)");
        db.execSQL("create table " + ELEV_TABLE_NAME + " " + "(" + ELEV_COLUMN_ID + " long primary key, " + ELEV_COLUMN_CLASA_ID
                + " long," + ELEV_COLUMN_NUME + " text, " + ELEV_COLUMN_PHONE + " text)");
        db.execSQL("create table " + NOTA_TABLE_NAME + " " + "(" + NOTA_COLUMN_ID + " long primary key, " + NOTA_COLUMN_ELEV_ID
                + " long," + NOTA_COLUMN_MATERIE_ID + " long, " + NOTA_COLUMN_DATA + " long, " + NOTA_COLUMN_VALUE + " text)");
        db.execSQL("create table " + ABSENTA_TABLE_NAME + " " + "(" + ABSENTA_COLUMN_ID + " long primary key, " + ABSENTA_COLUMN_ELEV_ID
                + " long," + ABSENTA_COLUMN_MATERIE_ID + " long, " + ABSENTA_COLUMN_DATA + " long, " + ABSENTA_COLUMN_MOTIVATA + " text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CLASA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MATERIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ELEV_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ABSENTA_TABLE_NAME);
        onCreate(db);
    }

    //CLASA-----------------------------------------------------------------------------------

    public long insertOrUpdateClasa(Clasa clasa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASA_COLUMN_INSTITUTIE_ID, clasa.getInstitutieId());
        contentValues.put(CLASA_COLUMN_NUME, clasa.getName());
        contentValues.put(CLASA_COLUMN_INSTITUTIE_NAME, clasa.getInstitutieName());
        if (clasa.getClasaId() != 0 && getClasaById(clasa.getClasaId()) != null) {
            Log.v("TESTLOGG", " UPDATE ");
            contentValues.put(CLASA_COLUMN_ID, clasa.getClasaId());
            db.update(CLASA_TABLE_NAME, contentValues, CLASA_COLUMN_ID + "= ? ", new String[]{String.valueOf(clasa.getClasaId())});
            return clasa.getClasaId();
        }
        long randomId = Utils.generateRandomId();
        contentValues.put(CLASA_COLUMN_ID, randomId);
        Log.v("TESTLOGG", " INSERT ");
        db.insert(CLASA_TABLE_NAME, null, contentValues);
        return randomId;
    }

    public long saveClasaAndDependencies(Clasa clasa) {
        long clasaId = insertOrUpdateClasa(clasa);
        clasa.setClasaId(clasaId);
        for(Materie materie : Utils.getMateriiByThisYear(clasa)){
            materie.setClasaId(clasaId);
            insertOrUpdateMaterie(materie);
        }
        for(Elev elev : clasa.getElevi()){
            elev.setClasaId(clasaId);
            insertOrUpdateElev(elev);
        }
        return clasaId;
    }

    public Clasa getClasaById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CLASA_TABLE_NAME + " where " + CLASA_COLUMN_ID + "=" + id + "", null);
        Clasa clasa = null;
        if (cursor.moveToFirst()) {
            clasa = new Clasa(cursor.getString(cursor.getColumnIndex(CLASA_COLUMN_NUME)));
            clasa.setClasaId(cursor.getLong(cursor.getColumnIndex(CLASA_COLUMN_ID)));
            clasa.setInstitutieId(cursor.getLong(cursor.getColumnIndex(CLASA_COLUMN_INSTITUTIE_ID)));
            clasa.setInstitutieName(cursor.getString(cursor.getColumnIndex(CLASA_COLUMN_INSTITUTIE_NAME)));
        }
        cursor.close();
        return clasa;
    }

    public Integer deleteClasa(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CLASA_TABLE_NAME,
                CLASA_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Clasa> getAllClases() {
        ArrayList<Clasa> clase = new ArrayList<Clasa>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CLASA_TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Clasa clasa = new Clasa(cursor.getString(cursor.getColumnIndex(CLASA_COLUMN_NUME)));
            clasa.setClasaId(cursor.getLong(cursor.getColumnIndex(CLASA_COLUMN_ID)));
            clasa.setInstitutieId(cursor.getLong(cursor.getColumnIndex(CLASA_COLUMN_INSTITUTIE_ID)));
            clasa.setInstitutieName(cursor.getString(cursor.getColumnIndex(CLASA_COLUMN_INSTITUTIE_NAME)));
            clase.add(clasa);
            cursor.moveToNext();
        }
        cursor.close();
        return clase;
    }

    //MATERIE-----------------------------------------------------------------------------------

    public boolean insertOrUpdateMaterie(Materie materie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MATERIE_COLUMN_CLASA_ID, materie.getClasaId());
        contentValues.put(MATERIE_COLUMN_NUME, materie.getName());
        if (materie.getMaterieId() != 0 && getMaterieById(materie.getMaterieId()) != null) {
            Log.v("TESTLOGG", " UPDATE MATERIE ");
            contentValues.put(MATERIE_COLUMN_ID, materie.getMaterieId());
            db.update(MATERIE_TABLE_NAME, contentValues, MATERIE_COLUMN_ID + "= ? ", new String[]{String.valueOf(materie.getMaterieId())});
            return true;
        }
        contentValues.put(MATERIE_COLUMN_ID, Utils.generateRandomId());
        Log.v("TESTLOGG", " INSERT MATERIE ");
        db.insert(MATERIE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Materie getMaterieById(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MATERIE_TABLE_NAME + " where " + MATERIE_COLUMN_ID + "=" + id + "", null);
        Materie materie = null;
        if (cursor.moveToFirst()) {
            materie = new Materie(cursor.getString(cursor.getColumnIndex(MATERIE_COLUMN_NUME)));
            materie.setMaterieId(cursor.getLong(cursor.getColumnIndex(MATERIE_COLUMN_ID)));
            materie.setClasaId(cursor.getLong(cursor.getColumnIndex(MATERIE_COLUMN_CLASA_ID)));
        }
        cursor.close();
        return materie;
    }

    public Integer deleteMaterie(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MATERIE_TABLE_NAME,
                MATERIE_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Materie> getAllMateriiByClasaId(long clasaId) {
        ArrayList<Materie> materii = new ArrayList<Materie>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MATERIE_TABLE_NAME + " where " + MATERIE_COLUMN_CLASA_ID + "=" + clasaId + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Materie materie = new Materie(cursor.getString(cursor.getColumnIndex(MATERIE_COLUMN_NUME)));
            materie.setMaterieId(cursor.getLong(cursor.getColumnIndex(MATERIE_COLUMN_ID)));
            materie.setClasaId(cursor.getLong(cursor.getColumnIndex(MATERIE_COLUMN_CLASA_ID)));
            materii.add(materie);
            cursor.moveToNext();
        }
        cursor.close();
        return materii;
    }

    //ELEV-----------------------------------------------------------------------------------

    public long insertOrUpdateElev(Elev elev) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ELEV_COLUMN_CLASA_ID, elev.getClasaId());
        contentValues.put(ELEV_COLUMN_NUME, elev.getName());
        contentValues.put(ELEV_COLUMN_PHONE, elev.getPhoneNumber());
        if (elev.getElevId() != 0 && getElevById(elev.getElevId()) != null) {
            Log.v("TESTLOGG", " UPDATE ELEV ");
            contentValues.put(ELEV_COLUMN_ID, elev.getElevId());
            db.update(ELEV_TABLE_NAME, contentValues, ELEV_COLUMN_ID + "= ? ", new String[]{String.valueOf(elev.getElevId())});
            return elev.getElevId();
        }
        long randomId = Utils.generateRandomId();
        contentValues.put(ELEV_COLUMN_ID, randomId);
        Log.v("TESTLOGG", " INSERT ELEV ");
        db.insert(ELEV_TABLE_NAME, null, contentValues);
        return randomId;
    }

    public Elev getElevById(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ELEV_TABLE_NAME + " where " + ELEV_COLUMN_ID + "=" + id + "", null);
        Elev elev = null;
        if (cursor.moveToFirst()) {
            elev = new Elev(cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_NUME)), cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_PHONE)));
            elev.setElevId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_ID)));
            elev.setClasaId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_CLASA_ID)));
        }
        cursor.close();
        return elev;
    }

    public Integer deleteElev(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ELEV_TABLE_NAME,
                ELEV_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Elev> getAllEleviByClasaId(long clasaId) {

        ArrayList<Elev> elevi = new ArrayList<Elev>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ELEV_TABLE_NAME + " where " + ELEV_COLUMN_CLASA_ID + "=" + clasaId + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Elev elev = new Elev(cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_NUME)), cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_PHONE)));
            elev.setElevId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_ID)));
            elev.setClasaId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_CLASA_ID)));
            elevi.add(elev);
            cursor.moveToNext();
        }
        cursor.close();
        return elevi;
    }

    public ArrayList<Elev> getAllEleviAndDependenciesByClasaId(long clasaId, long materieId) {

        ArrayList<Elev> elevi = new ArrayList<Elev>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ELEV_TABLE_NAME + " where " + ELEV_COLUMN_CLASA_ID + "=" + clasaId + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Elev elev = new Elev(cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_NUME)), cursor.getString(cursor.getColumnIndex(ELEV_COLUMN_PHONE)));
            elev.setElevId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_ID)));
            elev.setClasaId(cursor.getLong(cursor.getColumnIndex(ELEV_COLUMN_CLASA_ID)));
            elev.setNote(getAllNotaByElevId(elev.getElevId(), materieId));
            elev.setAbsente(getAllAbsentaByElevIdAndMaterieId(elev.getElevId(), materieId));
            elevi.add(elev);
            cursor.moveToNext();
        }
        cursor.close();
        return elevi;
    }

    //NOTA-----------------------------------------------------------------------------------

    public boolean insertOrUpdateNota(Nota nota, Clasa clasa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTA_COLUMN_ELEV_ID, nota.getElevId());
        contentValues.put(NOTA_COLUMN_MATERIE_ID, nota.getMaterieId());
        contentValues.put(NOTA_COLUMN_DATA, nota.getData().getTime());
        contentValues.put(NOTA_COLUMN_VALUE, String.valueOf(nota.getValue()));

        if (nota.getNotaId() != 0 && getNotaById(nota.getNotaId()) != null) {
            Log.v("TESTLOGG", " UPDATE NOTA ");
            contentValues.put(NOTA_COLUMN_ID, nota.getNotaId());
            db.update(NOTA_TABLE_NAME, contentValues, NOTA_COLUMN_ID + "= ? ", new String[]{String.valueOf(nota.getNotaId())});
            return true;
        }
        contentValues.put(NOTA_COLUMN_ID, Utils.generateRandomId());
        Log.v("TESTLOGG", " INSERT NOTA ");
        db.insert(NOTA_TABLE_NAME, null, contentValues);
        FirebaseDb.saveClasa(clasa);
        return true;
    }

    public Nota getNotaById(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + NOTA_TABLE_NAME + " where " + NOTA_COLUMN_ID + "=" + id + "", null);
        Nota nota = null;
        if (cursor.moveToFirst()) {
            nota = new Nota();
            nota.setNotaId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_ID)));
            nota.setElevId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_ELEV_ID)));
            nota.setMaterieId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_MATERIE_ID)));
            nota.setData(new Date(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_DATA))));
            nota.setValue(Integer.valueOf(cursor.getString(cursor.getColumnIndex(NOTA_COLUMN_VALUE))));
        }
        cursor.close();
        return nota;
    }

    public Integer deleteNota(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTA_TABLE_NAME,
                NOTA_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Nota> getAllNotaByElevId(long elevId, long materieId) {

        ArrayList<Nota> note = new ArrayList<Nota>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + NOTA_TABLE_NAME + " where " + NOTA_COLUMN_ELEV_ID + "=" + elevId
                + " AND " + NOTA_COLUMN_MATERIE_ID + "=" + materieId, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Nota nota = new Nota();
            nota.setNotaId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_ID)));
            nota.setElevId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_ELEV_ID)));
            nota.setMaterieId(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_MATERIE_ID)));
            nota.setData(new Date(cursor.getLong(cursor.getColumnIndex(NOTA_COLUMN_DATA))));
            nota.setValue(Integer.valueOf(cursor.getString(cursor.getColumnIndex(NOTA_COLUMN_VALUE))));
            note.add(nota);
            cursor.moveToNext();
        }
        cursor.close();
        return note;
    }

    //ABSENTA-----------------------------------------------------------------------------------

    public boolean insertOrUpdateAbsenta(Absenta absenta, Clasa clasa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ABSENTA_COLUMN_ELEV_ID, absenta.getElevId());
        contentValues.put(ABSENTA_COLUMN_MATERIE_ID, absenta.getMaterieId());
        contentValues.put(ABSENTA_COLUMN_DATA, absenta.getData().getTime());
        contentValues.put(ABSENTA_COLUMN_MOTIVATA, absenta.isMotivata()? "1":"0");
        if (absenta.getAbsentaId() != 0 && getAbsentaById(absenta.getAbsentaId()) != null) {
            Log.v("TESTLOGG", " UPDATE ABSENTA ");
            contentValues.put(ABSENTA_COLUMN_ID, absenta.getAbsentaId());
            db.update(ABSENTA_TABLE_NAME, contentValues, ABSENTA_COLUMN_ID + "= ? ", new String[]{String.valueOf(absenta.getAbsentaId())});
            return true;
        }
        contentValues.put(ABSENTA_COLUMN_ID, Utils.generateRandomId());
        Log.v("TESTLOGG", " INSERT ABSENTA ");
        db.insert(ABSENTA_TABLE_NAME, null, contentValues);
        FirebaseDb.saveClasa(clasa);
        return true;
    }

    public Absenta getAbsentaById(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ABSENTA_TABLE_NAME + " where " + ABSENTA_COLUMN_ID + "=" + id + "", null);
        Absenta absenta = null;
        if (cursor.moveToFirst()) {
            absenta = new Absenta();
            absenta.setAbsentaId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_ID)));
            absenta.setElevId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_ELEV_ID)));
            absenta.setMaterieId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_MATERIE_ID)));
            absenta.setData(new Date(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_DATA))));
            absenta.setMotivata(cursor.getString(cursor.getColumnIndex(ABSENTA_COLUMN_MOTIVATA)).equals("1"));
        }
        cursor.close();
        return absenta;
    }

    public Integer deleteAbsenta(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ABSENTA_TABLE_NAME,
                ABSENTA_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public ArrayList<Absenta> getAllAbsentaByElevIdAndMaterieId(long elevId, long materieId) {

        ArrayList<Absenta> absente = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ABSENTA_TABLE_NAME + " where " + ABSENTA_COLUMN_ELEV_ID + "=" + elevId
                + " AND " + ABSENTA_COLUMN_MATERIE_ID + "=" + materieId, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Absenta absenta = new Absenta();
            absenta.setAbsentaId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_ID)));
            absenta.setElevId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_ELEV_ID)));
            absenta.setMaterieId(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_MATERIE_ID)));
            absenta.setData(new Date(cursor.getLong(cursor.getColumnIndex(ABSENTA_COLUMN_DATA))));
            absenta.setMotivata(cursor.getString(cursor.getColumnIndex(ABSENTA_COLUMN_MOTIVATA)).equals("1"));
            absente.add(absenta);
            cursor.moveToNext();
        }
        cursor.close();
        return absente;
    }
}
