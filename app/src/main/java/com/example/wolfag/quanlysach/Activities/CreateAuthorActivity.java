package com.example.wolfag.quanlysach.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.wolfag.quanlysach.DataHelper.DatabaseHelper;
import com.example.wolfag.quanlysach.Models.Author;
import com.example.wolfag.quanlysach.R;

import java.util.Calendar;
import java.util.Date;

public class CreateAuthorActivity extends Activity {

    Button btnClear, btnSave;
    EditText edtAuthorBirthday, edtAuthorName;
    DatabaseHelper mDatabaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_author);

        setVar();

        addEvents();

        getData();
    }

    private void getData() {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("data");
//        if (bundle != null && bundle.getInt("key") == MainActivity.OPEN_AUTHOR_DIALOG) {
//            edtAuthorBirthday.setText(bundle.getString("code"));
//            edtAuthorName.setText(bundle.getString("name"));
//            btnSave.setText(R.string.btn_update);
//        }
    }

    private void addEvents() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAuthorName.setText("");
                edtAuthorName.requestFocus();
            }
        });

        edtAuthorBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBirthday();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Author author = new Author();
                author.setName(edtAuthorName.getText().toString());
                author.setBirthday(getDate(edtAuthorBirthday.getText().toString()));
                mDatabaseHelper.addAuthor(author);
                CreateAuthorActivity.this.finish();
            }
        });
    }

    private void handleBirthday() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                setDate(dayOfMonth, monthOfYear, year);
            }
        };

        Date date = getDate(edtAuthorBirthday.getText().toString());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.setTitle(getString(R.string.title_dialog_author_birthday));
        datePickerDialog.show();
    }

    private void setVar() {
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSaveAuthor);

        edtAuthorBirthday = (EditText) findViewById(R.id.edtAuthorBirthday);
        edtAuthorName = (EditText) findViewById(R.id.edtAuthorName);

        Calendar calendar = Calendar.getInstance();
        setDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        mDatabaseHelper = DatabaseHelper.getInstance(this);
    }

    private Date getDate(String str) {
        String[] dates = str.split("/");
        int day = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, day);
        return calendar.getTime();
    }

    private void setDate(int day, int month, int year) {
        edtAuthorBirthday.setText(day + "/" + month + "/" + year);
    }
}
