package com.example.notepadapp.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notepadapp.bean.Notepad;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final int version =1;
    private static final String name = "notepad.db";

    public MyDbHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "create table notepad(id integer primary key autoincrement,content text,time text)";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // 插入记录
    public void insertNote(Notepad notepad){
        String insertSql = "insert into notepad(content,time) values (?,?)";
        getWritableDatabase().execSQL(insertSql,new String[]{notepad.getContent(),notepad.getTime()});
    }

    // 查询所有记录
    @SuppressLint("Range")
    public List<Notepad> findAll(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from notepad";
        Cursor cursor = db.rawQuery(sql, null);

        List<Notepad> notepads=new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String item = cursor.getString(cursor.getColumnIndex("time"));
            Notepad notepad=new Notepad(id,content,item);
            notepads.add(notepad);
        }

        return notepads;
    }

    // 删除记录
    public void delete(Notepad notepad){
        String sql = "delete from notepad where id=?";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql,new Object[]{notepad.getId()});
    }
}
