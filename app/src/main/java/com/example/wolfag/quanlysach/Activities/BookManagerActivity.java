package com.example.wolfag.quanlysach.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wolfag.quanlysach.DataHelper.DatabaseHelper;
import com.example.wolfag.quanlysach.Models.Author;
import com.example.wolfag.quanlysach.Models.Book;
import com.example.wolfag.quanlysach.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookManagerActivity extends Activity {

    EditText edtBookName, edtPublishDate;
    Spinner spnAuthor;
    Button btnAddBook;
    ListView lvBook;

    Author mAuthor = null;
    Book mBook = null;
    List<Author> mAuthorList = null;
    ArrayAdapter<Author> mAuthorArrayAdapter = null;
    List<Book> mBookList = null;
    ArrayAdapter<Book> mBookArrayAdapter = null;
    Calendar mCalendar = null;

    DatabaseHelper mDatabaseHelper = null;

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (edtBookName.getText().toString().length() * edtPublishDate.getText().toString().length() == 0) {
                btnAddBook.setEnabled(false);
            } else {
                btnAddBook.setEnabled(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);

        setVar();

        loadData();

        addEvents();
    }

    private void loadData() {
        mAuthorList.addAll(mDatabaseHelper.getAllAuthors());
        mAuthorArrayAdapter.notifyDataSetChanged();

        mBookList.addAll(mDatabaseHelper.getAllBooks());
        mBookArrayAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
            }
        });

        edtPublishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePublishDate();
            }
        });

        edtPublishDate.addTextChangedListener(mTextWatcher);
        edtBookName.addTextChangedListener(mTextWatcher);


        spnAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAuthor = mAuthorList.get(i);
                loadAllBookByAuthorCode(mAuthor.getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAuthor = null;
            }
        });
    }

    private void loadAllBookByAuthorCode(int code) {
        mBookList.clear();
        mBookList.addAll(mDatabaseHelper.getAllBooks(code));
        mBookArrayAdapter.notifyDataSetChanged();
    }

    private void loadAllBook() {
        mBookList.clear();
        mBookList.addAll(mDatabaseHelper.getAllBooks());
        mBookArrayAdapter.notifyDataSetChanged();
    }

    private void handlePublishDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                setDate(dayOfMonth, monthOfYear, year);
            }
        };

        Date date = getDate(edtPublishDate.getText().toString());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.setTitle(getString(R.string.title_dialog_publish_date));
        datePickerDialog.show();
    }

    private void addBook() {
        if (mAuthor != null) {
            Book book = new Book();
            book.setName(edtBookName.getText().toString());
            book.setPublishDate(getDate(edtPublishDate.getText().toString()));
            book.setAuthor(mAuthor);
            mBookList.add(book);
            mBookArrayAdapter.notifyDataSetChanged();

            mDatabaseHelper.addBook(book);
        } else {
            Toast.makeText(this, R.string.msg_choose_author, Toast.LENGTH_SHORT).show();
        }
    }

    private void setVar() {
        btnAddBook = (Button) findViewById(R.id.btnAddBook);
        edtBookName = (EditText) findViewById(R.id.edtBookName);
        edtPublishDate = (EditText) findViewById(R.id.edtPubishDate);
        lvBook = (ListView) findViewById(R.id.lvBook);
        spnAuthor = (Spinner) findViewById(R.id.spnAuthor);

        mAuthorList = new ArrayList<>();
        mBookList = new ArrayList<>();

        mAuthorArrayAdapter = new ArrayAdapter<Author>(this, android.R.layout.simple_dropdown_item_1line, mAuthorList);
        spnAuthor.setAdapter(mAuthorArrayAdapter);

        mBookArrayAdapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, mBookList);
        lvBook.setAdapter(mBookArrayAdapter);

        mCalendar = Calendar.getInstance();
        setDate(mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));

        mDatabaseHelper = DatabaseHelper.getInstance(this);
    }

    private void setDate(int day, int month, int year) {
        edtPublishDate.setText(day + "/" + month + "/" + year);
    }


    private Date getDate(String str) {
        String[] dates = str.split("/");
        int day = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month , day);
        return calendar.getTime();
    }

}
