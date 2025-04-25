package roukaya.chelly.callerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Contact_Helper  extends SQLiteOpenHelper {

    public static final String table_Contact="Contacts";
    public static final String col_id="ID";
    public static final String col_firstname="Firstname";
    public static final String col_lastname="Lastname";
    public static final String col_phone="Phone";
    String req="create table "+table_Contact+" ("+ col_id+" Integer Primary key autoincrement ,"+col_firstname+" text not null,"+ col_lastname+" text not null,"+col_phone+" text not null )";

    public Contact_Helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //lors de l'ouverture de la base pour la 1er fois
        db.execSQL(req);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //modification de la version
        db.execSQL("drop table "+table_Contact);
        db.execSQL(req);
    }
}
