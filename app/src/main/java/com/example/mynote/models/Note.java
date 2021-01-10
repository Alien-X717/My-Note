package com.example.mynote.models;

import java.util.Calendar;

public class Note {
    private int id;
    private String noteTitle;
    private String notecontent;
    private String noteDateTime;

    public Note(int id, String noteTitle, String notecontent, String noteDateTime)
    {
        this.id = id;
        this.noteTitle = noteTitle;
        this.notecontent = notecontent;
        this.noteDateTime = noteDateTime;
    }

    public Note(String noteTitle, String notecontent, String noteDateTime) {
        this.noteTitle = noteTitle;
        this.notecontent = notecontent;
        this.noteDateTime = noteDateTime;
    }
    public Note(){}
    public void setId(int id){
        this.id = id;
    }
    public int getNoteId(){
        return id;
    }
    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotecontent() {
        return notecontent;
    }

    public void setNotecontent(String notecontent) {
        this.notecontent = notecontent;
    }

    public String getNoteDateTime() {
        return noteDateTime;
    }

    public void setNoteDateTime(String noteDateTime) {
        this.noteDateTime = noteDateTime;
    }
}
