package com.example.wolfag.quanlysach.Models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wolfag on 29/09/2017.
 */

public class Book extends Info implements Serializable {
    private static long serialVersionUID = 1L;
    private Date publishDate;
    private Author author;

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book() {
    }

    public Book(int code, String name, Date publishDate, Author author) {
        super(code, name);
        this.publishDate = publishDate;
        this.author = author;
    }

    @Override
    public String toString() {
        return super.getName() + " - " + formatDate(this.publishDate);
    }

    private String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
}
