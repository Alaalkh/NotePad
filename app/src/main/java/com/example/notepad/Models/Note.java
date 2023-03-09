package com.example.notepad.Models;

public class Note {
    String id;
    String title;
    String note;
    private  Note(){}
    public Note(String id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String gettitle() {
        return title;
    }
    public String getNote() {
        return note;
    }

}
