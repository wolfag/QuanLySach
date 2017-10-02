package com.example.wolfag.quanlysach.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wolfag on 29/09/2017.
 */

public class Author extends Info implements Serializable {
    private static long serialVersionUID = 1L;
    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Author() {
        super();
    }

    public Author(int code, String name, Date birthday) {
        super(code, name);
        this.birthday = birthday;
    }
}
