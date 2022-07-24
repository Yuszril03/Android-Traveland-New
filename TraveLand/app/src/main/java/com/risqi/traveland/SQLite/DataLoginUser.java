package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataLoginUser extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland.db";
    public static final String TABLE_NAME = "table_user_login";
    public static final String COL_NIK = "nik";
    public static final String COL_NAMA = "nama";
    public static final String COL_FOTO = "foto";
    public static final String COL_GENDER = "gender";
    public static final int DATABASE_VERTION = 1;

    public DataLoginUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_user(nik text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String Nik, String Nama, String foto, String gender){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NIK,Nik);
        contentValues.put(COL_NAMA,Nama);
        contentValues.put(COL_FOTO,foto);
        contentValues.put(COL_GENDER,gender);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) {
            return false;
        }else
        {
            return true;
        }
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from table_user",null);
        return  res;
    }
    public boolean updateData(String Nik, String Nama, String foto, String gender)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMA,Nama);
        contentValues.put(COL_FOTO,foto);
        contentValues.put(COL_GENDER,gender);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{Nik});

        return  true;
    }
    public Boolean deleteDataAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        return  true;
    }

}
