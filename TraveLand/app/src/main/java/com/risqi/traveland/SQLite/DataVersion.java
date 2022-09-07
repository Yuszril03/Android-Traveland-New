package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataVersion extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland_version_two.db";
    public static final String TABLE_NAME = "table_version";
    public static final String COL_1 = "versi";
    public static final String COL_2 = "waktu";
    public static final int DATABASE_VERTION = 2;

    public DataVersion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_version(id text null,versi text null,waktu text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String versi,String waktu){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id","1");
        contentValues.put(COL_1,versi);
        contentValues.put(COL_2,waktu);
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
        Cursor res = db.rawQuery("select * from table_version",null);
        return  res;
    }
    public boolean updateData(String versi,String waktu)
    {
        String ID="1";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,versi);
        contentValues.put(COL_2,waktu);
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
