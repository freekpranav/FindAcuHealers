package com.hoperaiser.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
public class Database extends SQLiteOpenHelper {


    String table="create table user_location(id text,name text,longi text,lat text)";
    String tableEmp1="create table app_data1(id text,name text,longi text,lat text, landmark text, number text, doc_name text, distance text, label6 text,label7 text,label8 text,label9 text,label10 text,label11 text,label12 text,label13 text)";

    public Database(Context context)
    {
        super(context, "Database3.db", null, 1);
    }
    SQLiteDatabase s1;
    @Override
    public void onCreate(SQLiteDatabase db) {


        String tableEmp="create table app_data1(id text,name text,longi text,lat text, landmark text, number text, doc_name text, distance text, label6 text,label7 text,label8 text,label9 text,label10 text,label11 text,label12 text,label13 text)";

        db.execSQL(tableEmp);
        db.execSQL(table);



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void insertData(String id1,String name, String longi,String lat, String landmark, String number,String doc_name,String distance,String label6,String label7,String label8,String label9,String label10,String label11,String label12,String label13) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM app_data1 WHERE id=" + id1, null);
    if (c.getCount() != 0) {

    } else {
        ContentValues values = new ContentValues();

        values.put("id", id1);
        values.put("name", name);
        values.put("longi", longi);
        values.put("lat", lat);
        values.put("landmark", landmark);
//            values.put("time", time);
        values.put("doc_name", doc_name);
//            values.put("hos_name", hos_name);
        values.put("number", number);
        values.put("distance", distance);
        values.put("label6", label6);
        values.put("label7", label7);
        values.put("label8", label8);
        values.put("label9", label9);
        values.put("label10", label10);
        values.put("label11", label11);
        values.put("label12", label12);
        values.put("label13", label13);

//        sqLiteDatabase.execSQL("delete from app_data1 ");

        sqLiteDatabase.insert("app_data1", null, values);



//
}
    }



    public void insertDatalatlong(String id,String name, String longi,String lat) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM user_location WHERE id=" + id, null);
        if (c.getCount() != 0) {

        } else {
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("name", name);
            values.put("longi", longi);
            values.put("lat", lat);


            sqLiteDatabase.insert("user_location", null, values);
        }

        }




    public void updateData(String id,String name, String longi,String lat, String landmark, String number,String doc_name,String distance,String label6,String label7,String label8,String label9,String label10,String label11,String label12,String label13) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("name", name);
            values.put("longi", longi);
            values.put("lat", lat);
            values.put("landmark", landmark);
//            values.put("time", time);
            values.put("doc_name", doc_name);
//            values.put("hos_name", hos_name);
            values.put("number", number);
            values.put("distance", distance);
            values.put("label6", label6);
            values.put("label7", label7);
            values.put("label8", label8);
            values.put("label9", label9);
            values.put("label10", label10);
            values.put("label11", label11);
            values.put("label12", label12);
            values.put("label13", label13);
//            sqLiteDatabase.delete("app_data1",null,null);
//
////        sqLiteDatabase.update("app_data1", values,null,null );
//        sqLiteDatabase.execSQL("delete from app_data1 where id="+id);


            sqLiteDatabase.update("app_data1", values,"id= ?",new String[] {id} );
//            sqLiteDatabase.update("app_data1", values,null,null );
    }
    public void updateDatalonglat(String id,String name, String longi,String lat) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("longi", longi);
        values.put("lat", lat);


        sqLiteDatabase.update("user_location", values,"id= ?",new String[] {id} );


    }

    public Cursor fetchData()
    {
        String id="0";
        String fetchdata="select distinct * from app_data1 where id !="+id;
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);

        return cursor;
    }
    public Cursor fetchTitle()
    {
        String id="Name";
        String fetchdata="select * from app_data1 where id ==0";
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);

        return cursor;
    }
    public Cursor fetchlocalData()
    {
String id1="0";
        String fetchdata="select * from app_data1 where id!=0";
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);


        return cursor;
    }
}
