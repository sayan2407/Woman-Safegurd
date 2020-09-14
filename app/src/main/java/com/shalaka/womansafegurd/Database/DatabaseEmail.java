package com.shalaka.womansafegurd.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseEmail extends SQLiteOpenHelper {
    public static final String DB_NAME="Emaill.db";
    public static final String TABLE_NAME="email_data";
    public static final String COL_2="EMAIL";
    public DatabaseEmail(Context context) {
        super(context,DB_NAME,null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

    }
    public boolean insertData(String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(COL_2,email);
        long result =db.insert(TABLE_NAME,null,c);
        if (result==-1)
            return false;
        else
            return true;
    }
    public boolean emailUpdate(String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(COL_2,email);
        db.update(TABLE_NAME,c,"ID=?",new String[]{"1"});
        return true;
    }
    public Cursor getEmail()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT EMAIL FROM "+TABLE_NAME,null);
        return  res ;
    }
}
