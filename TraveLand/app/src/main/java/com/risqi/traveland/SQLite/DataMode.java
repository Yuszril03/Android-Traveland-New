package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataMode extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland_mode_two.db";
    public static final String TABLE_NAME = "table_mode";
    public static final String COL_1 = "id";
    public static final String COL_2 = "mode";
    public static final int DATABASE_VERTION = 2;

    public DataMode(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_mode(id text null,mode text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean insertData(String mode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,"1");
        contentValues.put(COL_2,mode);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) {
            return false;
        }else
        {
            return true;
        }
    }
    public boolean updateData(String mode)
    {
        String idd="1";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,mode);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{idd});

        return  true;
    }
    public Cursor getDataOne()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select * from table_mode WHERE id = '"+1+"'";
        Cursor res = db.rawQuery(sql,null);
        return  res;
    }
}
