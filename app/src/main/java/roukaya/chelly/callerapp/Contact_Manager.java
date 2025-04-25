package roukaya.chelly.callerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Contact_Manager {
    SQLiteDatabase db = null;
    Context con;
    Contact_Helper contactHelper;

    // Fix the constructor to accept any Context
    public Contact_Manager(Context con) {
        this.con = con;
    }

    public void Ouvrir() {
        if (db == null || !db.isOpen()) {
            contactHelper = new Contact_Helper(con, "mabase.db", null, 1);
            db = contactHelper.getWritableDatabase();
        }
    }

    public long ajout(String fn, String ln, String phone) {
        long a = 0;
        ContentValues values = new ContentValues();
        values.put(Contact_Helper.col_firstname, fn);
        values.put(Contact_Helper.col_lastname, ln);
        values.put(Contact_Helper.col_phone, phone);
        a = db.insert(Contact_Helper.table_Contact, null, values);
        return a;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> list = new ArrayList<>();
        Cursor cr = db.query(Contact_Helper.table_Contact, new String[]{
                        Contact_Helper.col_firstname,
                        Contact_Helper.col_lastname,
                        Contact_Helper.col_phone},
                null, null, null, null, null);

        // Check if cursor has any results
        if (cr != null && cr.getCount() > 0) {
            cr.moveToFirst();
            while (!cr.isAfterLast()) {
                String firstname = cr.getString(0);
                String lastname = cr.getString(1);
                String phone = cr.getString(2);
                list.add(new Contact(firstname, lastname, phone));
                cr.moveToNext();
            }
        }

        if (cr != null) {
            cr.close();
        }
        return list;
    }

    public void supprimer(String phone) {
        db.delete(Contact_Helper.table_Contact, Contact_Helper.col_phone + " = ?", new String[]{phone});
    }

    public void fermer() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}