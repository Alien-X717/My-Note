package com.example.mynote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mynote.constants.Constants;
import com.example.mynote.models.Note;

import java.util.ArrayList;
import java.util.List;

public class DBhandler extends SQLiteOpenHelper {
    SQLiteDatabase wdb;
    SQLiteDatabase rdb;
    public DBhandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);


    }

    public void addNote(Note note){
        SQLiteDatabase wdb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",note.getNoteTitle());
        contentValues.put("content",note.getNotecontent());
        contentValues.put("datetime",note.getNoteDateTime());
        wdb.insert("notes", null, contentValues);
        Log.d("DB","new note \ntitle" +note.getNoteTitle() + " \ncontent: " +
                note.getNotecontent() + "\ndate" + note.getNoteDateTime()+"\n" + contentValues.toString() );

    }
    public boolean updateNote(Note note){
        SQLiteDatabase wdb = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",note.getNoteTitle());
        contentValues.put("content",note.getNotecontent());
        contentValues.put("datetime",Constants.getTime());
        int res = wdb.update("notes",contentValues, "id = ? ",
                new String[]{Integer.toString(note.getNoteId())});

        if(res>0){
            Log.d("DB","updated ,rows affected : " + res);
            return true;}
        else
            return false;
    }
    public void deleteNote(int id){
    wdb = getWritableDatabase();
    wdb.delete("notes","id = ? ",new String[]{Integer.toString(id)});

    }
    public Note getNote(int id){
        SQLiteDatabase rdb = this.getReadableDatabase();
        String qry = "SELECT * FROM notes WHERE id ="+id;
        Cursor cursor = rdb.rawQuery(qry,null);
        Note note = new Note();
        if(cursor.moveToFirst()) {
            note.setId(id);
            note.setNoteTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setNotecontent(cursor.getString(cursor.getColumnIndex("content")));
            note.setNoteDateTime(cursor.getString(cursor.getColumnIndex("datetime")));
            cursor.close();
        }
        return note;
    }
    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase rdb = this.getReadableDatabase();
        String select = "SELECT * FROM notes";
        Cursor cursor = rdb.rawQuery(select,null);
        if(cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("id")));
                note.setNoteTitle(cursor.getString(cursor.getColumnIndex("title")));
                note.setNotecontent(cursor.getString(cursor.getColumnIndex("content")));
                note.setNoteDateTime(cursor.getString(cursor.getColumnIndex("datetime")));
                notes.add(note);
                Log.d("DB", "record readed:" + note.getNoteId() + note.getNoteTitle() + note.getNotecontent() + note.getNoteDateTime());

            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (id INTEGER PRIMARY KEY, title TEXT, content TEXT, datetime TEXT)");
        Log.d("DB","table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
