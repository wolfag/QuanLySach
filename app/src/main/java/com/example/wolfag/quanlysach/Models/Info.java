package com.example.wolfag.quanlysach.Models;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Created by wolfag on 29/09/2017.
 */

public class Info implements Serializable {
    private int code;
    private String name;
    private static long serialVersionUID = 1L;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Info() {
    }

    public Info(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.name;
    }
}
