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
        super(context,"db_test",null,4);
        db=getReadableDatabase();
    }
    public void onCreate(SQLiteDatabase db) {
        //单词表
        db.execSQL("CREATE TABLE IF NOT EXISTS dictionary("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+"chinese TEXT,"+
                "english TEXT,"+"times INTEGER)");
        //收藏单词表
        db.execSQL("CREATE TABLE IF NOT EXISTS myWords("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+"chinese TEXT,"+
                "english TEXT,"+"times INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dictionary");
        db.execSQL("DROP TABLE IF EXISTS myWords");
        onCreate(db);
    }

    //添加到我的单词本
    public void add(String chinese, String english, int times){
        db.execSQL("INSERT INTO dictionary(chinese,english,times) VALUES(?,?,?)", new Object[]{chinese, english, times});
    }
    //添加至收藏列表
    public void add2(String chinese, String english, int times){
        db.execSQL("INSERT INTO myWords(chinese,english,times) VALUES(?,?,?)", new Object[]{chinese, english, times});
    }

    public boolean checkWordExists(String english) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM dictionary WHERE english = ?", new String[]{english});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    //搜索单词
    public ArrayList<Word> searchWords(String keyword) {
        ArrayList<Word> resultList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM dictionary WHERE chinese LIKE ? OR english LIKE ?",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
            @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex("english"));
            @SuppressLint("Range") int times = cursor.getInt(cursor.getColumnIndex("times"));
            resultList.add(new Word(chinese, english, times));
        }
        cursor.close();
        return resultList;
    }

    //删除单词
    public void deleteWord(String chinese,String english){
        db.delete("dictionary","chinese=? AND english=?",new String[]{chinese,english});
    }

    public ArrayList<Word> getAllwords() {
        ArrayList<Word> wordList = new ArrayList<>();
        // 打开数据库进行查询
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("dictionary", null, null, null, null, null, null);
        // 遍历查询结果
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex("english"));
                @SuppressLint("Range") int times = cursor.getInt(cursor.getColumnIndex("times"));
                Word word = new Word(chinese, english, times);
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wordList;
    }
    public ArrayList<Word> getMyWords() {
        ArrayList<Word> wordList = new ArrayList<>();
        // 打开数据库进行查询
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("myWords", null, null, null, null, null, null);
        // 遍历查询结果
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex("english"));
                @SuppressLint("Range") int times = cursor.getInt(cursor.getColumnIndex("times"));
                Word word = new Word(chinese, english, times);
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wordList;
    }
}