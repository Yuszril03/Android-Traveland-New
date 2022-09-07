package com.risqi.traveland.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBeforeLogin extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data_traveland_activity.db";
    public static final String TABLE_NAME = "table_before_activity";
    public static final String COL_1 = "id";
    public static final String COL_2 = "screen";
    public static final String COL_3 = "id_screen";
    public static final int DATABASE_VERTION = 2;

    public DataBeforeLogin(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERTION);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_before_activity(id text null,screen text null, id_screen text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
    public boolean insertData(String screen, String id_screen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,"1");
        contentValues.put(COL_2,screen);
        contentValues.put(COL_3,id_screen);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1) {
            return false;
        }else
        {
            return true;
        }
    }
    public boolean updateData(String screen)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,screen);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{"1"});

        return  true;
    }
    public Cursor getDataOne()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="select * from table_before_activity WHERE id = '1'";
        Cursor res = db.rawQuery(sql,null);
        return  res;
    }
    public Boolean deleteDataAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        return  true;
    }
}
