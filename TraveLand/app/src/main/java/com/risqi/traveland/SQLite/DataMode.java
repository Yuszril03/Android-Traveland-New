package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataMode extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland.db";
    public static final String TABLE_NAME = "table_mode";
    public static final String COL_ID = "id";
    public static final String COL_STATUS = "status";
    public static final int DATABASE_VERTION = 1;

    public DataMode(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_user(id text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String mode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,"1");
        contentValues.put(COL_STATUS,mode);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) {
            return false;
        }else
        {
            return true;
        }
    }

    public Cursor getData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select * from table_mode WHERE id = '"+id+"'";
        Cursor res = db.rawQuery(sql,null);
        return  res;
    }
    public boolean updateData(String mode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS,mode);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{"1"});

        return  true;
    }
}
