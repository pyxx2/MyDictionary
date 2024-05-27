package com.example.mydictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public MyDatabaseHelper(Context context){
        super(context,"db_test",null,1);
        db=getReadableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        //单词表
        db.execSQL("CREATE TABLE IF NOT EXISTS dictionary("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+"chinese TEXT,"+
                "english TEXT,"+"times INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dictionary");
        onCreate(db);
    }
    public void add(String chinese,String english ){
        db.execSQL("INSERT INTO dictionary(chinese,english)VALUES(?,?)",new Object[]{chinese,english});
    }

    public ArrayList<word> getAllDATA(){
        ArrayList<word> list = new ArrayList<word>();
        Cursor cursor = db.query("dictionary",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            @SuppressLint("Range") String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex("english"));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") int times = cursor.getInt(cursor.getColumnIndex("times"));
            list.add(new word(chinese,english,id,times));
        }
        return list;
    }

    //删除单词用的
    public void deleteCart(String english){
        db.delete("usercart","username=?",new String[]{english});
    }
    public void deleteSingleItem(String username, String goods_name) {
        // 先查询获取符合条件的记录的goods_id
        Cursor cursor = db.query("usercart", new String[]{"goods_id"}, "username=? AND goods_name=?", new String[]{username, goods_name}, null, null, null);
        if (cursor.moveToFirst()) {
            // 获取第一条记录的goods_id
            @SuppressLint("Range") int goodsId = cursor.getInt(cursor.getColumnIndex("goods_id"));
            // 使用goods_id删除记录
            int rowsDeleted = db.delete("usercart", "goods_id=?", new String[]{Integer.toString(goodsId)});
            cursor.close();
            // 如果至少有一行被删除，则返回true，否则返回false
        }
        cursor.close();
        // 如果没有记录匹配查询条件，则返回false
    }

}