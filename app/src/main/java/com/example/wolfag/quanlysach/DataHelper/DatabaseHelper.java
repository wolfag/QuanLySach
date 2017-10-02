package com.example.wolfag.quanlysach.DataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wolfag.quanlysach.Models.Author;
import com.example.wolfag.quanlysach.Models.Book;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wolfag on 30/09/2017.
 * Ref: https://github.com/codepath/android_guides/wiki/Local-Databases-with-SQLiteOpenHelper
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    private static DatabaseHelper sInstance;

    //Database Info
    private static final int DATABASE_VERSION = 6;
    private static final String DB_NAME = "qls";

    //Table Names
    private static final String TABLE_AUTHOR = "author";
    private static final String TABLE_BOOK = "book";

    //Author table column
    private static final String KEY_AUTHOR_CODE = "code";
    private static final String KEY_AUTHOR_NAME = "name";
    private static final String KEY_AUTHOR_BIRTHDAY = "birthday";

    //Book table column
    private static final String KEY_BOOK_CODE = "code";
    private static final String KEY_BOOK_NAME = "name";
    private static final String KEY_BOOK_PUBLISH_DATE = "publishDate";
    private static final String KEY_BOOK_AUTHOR_CODE_FK = "authorCode";

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_AUTHOR_TABLE = "create table " + TABLE_AUTHOR + "" +
                "(" +
                KEY_AUTHOR_CODE + " integer primary key autoincrement, " +
                KEY_AUTHOR_NAME + " text, " +
                KEY_AUTHOR_BIRTHDAY + " integer " +
                ")";
        String CREATE_BOOK_TABLE = "create table " + TABLE_BOOK + "" +
                "(" +
                KEY_BOOK_CODE + " integer primary key autoincrement," +
                KEY_BOOK_NAME + " text, " +
                KEY_BOOK_PUBLISH_DATE + " text," +
                KEY_BOOK_AUTHOR_CODE_FK + " integer references " + TABLE_AUTHOR +
                ")";
        sqLiteDatabase.execSQL(CREATE_AUTHOR_TABLE);
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_AUTHOR);
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_BOOK);
            onCreate(sqLiteDatabase);
        }
    }

    //CRUD author and book

    public long addAuthor(Author author) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AUTHOR_NAME, author.getName());
            values.put(KEY_AUTHOR_BIRTHDAY, author.getBirthday().getTime());
            id = db.insertOrThrow(TABLE_AUTHOR, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public Author getAuthor(int code) {
        Author author = null;
        String query = String.format("select * from %s where %s=?", TABLE_AUTHOR, KEY_AUTHOR_CODE);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{code + ""});
        try {
            if (cursor.moveToFirst()) {
                author = new Author();
                author.setCode(code);
                author.setName(cursor.getString(cursor.getColumnIndex(KEY_AUTHOR_NAME)));
                author.setBirthday(new Date(cursor.getLong(cursor.getColumnIndex(KEY_AUTHOR_BIRTHDAY))));
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return author;
    }

    public boolean deleteAuthor(Author author) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int rows = db.delete(TABLE_AUTHOR, KEY_AUTHOR_CODE + "= ?", new String[]{author.getCode() + ""});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public boolean updateAuthor(Author author) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AUTHOR_NAME, author.getName());
            values.put(KEY_AUTHOR_BIRTHDAY, author.getBirthday().getTime());
            int rows = db.update(TABLE_AUTHOR, values, KEY_AUTHOR_CODE + "= ?", new String[]{author.getCode() + ""});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception ex) {
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String query = String.format("select * from %s", TABLE_AUTHOR);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Author author = new Author();
                    author.setCode(cursor.getInt(cursor.getColumnIndex(KEY_AUTHOR_CODE)));
                    author.setName(cursor.getString(cursor.getColumnIndex(KEY_AUTHOR_NAME)));
                    author.setBirthday(new Date(cursor.getLong(cursor.getColumnIndex(KEY_AUTHOR_BIRTHDAY))));
                    authors.add(author);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return authors;
    }

    public void deleteAllBooksAndAuthors() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_BOOK, null, null);
            db.delete(TABLE_AUTHOR, null, null);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public long addBook(Book book) {
        long id = -1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_BOOK_NAME, book.getName());
            values.put(KEY_BOOK_PUBLISH_DATE, book.getPublishDate().getTime());
            values.put(KEY_BOOK_AUTHOR_CODE_FK, book.getAuthor().getCode());
            id = db.insertOrThrow(TABLE_BOOK, null, values);
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public Book getBook(int code) {
        Book book = null;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("select * from %s where %s=?", TABLE_BOOK, KEY_BOOK_CODE);
        Cursor cursor = db.rawQuery(query, new String[]{code + ""});
        try {
            if (cursor.moveToFirst()) {
                book = new Book();
                book.setCode(code);
                book.setName(cursor.getString(cursor.getColumnIndex(KEY_BOOK_NAME)));
                book.setPublishDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_PUBLISH_DATE))));
                book.setAuthor(getAuthor(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_AUTHOR_CODE_FK))));
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return book;
    }

    public boolean deleteBook(Book book) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int rows = db.delete(TABLE_BOOK, KEY_BOOK_CODE + "= ?", new String[]{book.getCode() + ""});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public boolean updateBook(Book book) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_BOOK_NAME, book.getName());
            values.put(KEY_BOOK_PUBLISH_DATE, book.getPublishDate().getTime());
            values.put(KEY_BOOK_AUTHOR_CODE_FK, book.getAuthor().getCode());
            int rows = db.update(TABLE_BOOK, values, KEY_BOOK_CODE + "= ?", new String[]{book.getCode() + ""});
            if (rows == 1) {
                db.setTransactionSuccessful();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception ex) {
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = String.format("select * from %s left outer join %s on %s.%s = %s.%s",
                TABLE_BOOK,
                TABLE_AUTHOR,
                TABLE_BOOK, KEY_BOOK_AUTHOR_CODE_FK,
                TABLE_AUTHOR, KEY_AUTHOR_CODE);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Book book = new Book();
                    book.setCode(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_CODE)));
                    book.setPublishDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_PUBLISH_DATE))));
                    book.setName(cursor.getString(cursor.getColumnIndex(KEY_BOOK_NAME)));
                    book.setAuthor(this.getAuthor(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_AUTHOR_CODE_FK))));
                    books.add(book);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return books;
    }

    public List<Book> getAllBooks(int authorCode) {
        List<Book> books = new ArrayList<>();
        String query = String.format("select * from %s where %s=?", TABLE_BOOK, KEY_BOOK_AUTHOR_CODE_FK);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{authorCode + ""});
        try {
            if (cursor.moveToFirst()) {
                do {
                    Book book = new Book();
                    book.setCode(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_CODE)));
                    book.setPublishDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_PUBLISH_DATE))));
                    book.setName(cursor.getString(cursor.getColumnIndex(KEY_BOOK_NAME)));
                    book.setAuthor(this.getAuthor(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_AUTHOR_CODE_FK))));
                    books.add(book);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return books;
    }

}
