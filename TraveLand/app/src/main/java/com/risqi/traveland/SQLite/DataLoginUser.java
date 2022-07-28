package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataLoginUser extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland_user.db";
    public static final String TABLE_NAME = "table_user_login";
    public static final String COL_1 = "nik";
    public static final String COL_2 = "nama";
    public static final String COL_3 = "foto";
    public static final String COL_4 = "gender";
    public static final String COL_5 = "katasandi";
    public static final int DATABASE_VERTION = 1;

    public DataLoginUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_user_login(id text null,nik text null,nama text null,foto text null,gender text null,katasandi text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String Nik, String Nama, String foto, String gender,String katasandi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id","1");
        contentValues.put(COL_1,Nik);
        contentValues.put(COL_2,Nama);
        contentValues.put(COL_3,foto);
        contentValues.put(COL_4,gender);
        contentValues.put(COL_5,katasandi);
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
        Cursor res = db.rawQuery("select * from table_user_login",null);
        return  res;
    }
    public Cursor getDataOne()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select * from table_user_login WHERE id = '"+1+"'";
        Cursor res = db.rawQuery(sql,null);
        return  res;
    }
    public boolean updateData(String Nik, String Nama, String foto, String gender,String katasandi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Nama);
        contentValues.put(COL_3,foto);
        contentValues.put(COL_4,gender);
        contentValues.put(COL_5,katasandi);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{Nik});

        return  true;
    }
    public boolean updateDataFoto( String foto)
    {
        String ID="1";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,foto);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{ID});

        return  true;
    }
    public Boolean deleteDataAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        return  true;
    }

}
