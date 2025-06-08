package com.example.notepadapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notepadapp.bean.Notepad;

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

    public void insertNote(Notepad notepad){
        String insertSql = "insert into notepad(content,time) values (?,?)";
        getWritableDatabase().execSQL(insertSql,new String[]{notepad.getContent(),notepad.getTime()});
    }
}
