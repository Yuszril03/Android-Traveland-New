package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataFirstApp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland.db";
    public static final String TABLE_NAME = "table_first_app";
    public static final String COL_ID = "id";
    public static final String COL_Status = "status";
    public static final int DATABASE_VERTION = 3;

    public DataFirstApp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_first_app(id text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean insertData(String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,"1");
        contentValues.put(COL_Status,status);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) {
            return false;
        }else
        {
            return true;
        }
    }
    public boolean updateData(String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_Status,status);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{"1"});

        return  true;
    }
    public Cursor getDataOne()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select * from table_first_app WHERE id = '"+1+"'";
        Cursor res = db.rawQuery(sql,null);
        return  res;
    }
}
