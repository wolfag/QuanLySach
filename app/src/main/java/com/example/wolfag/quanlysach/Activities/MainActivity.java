package com.example.wolfag.quanlysach.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wolfag.quanlysach.DataHelper.DatabaseHelper;
import com.example.wolfag.quanlysach.R;

public class MainActivity extends Activity {
    Button btnAddAuthor, btnBookMgr, btnViewAuthorList;

    public static final int OPEN_AUTHOR_DIALOG = 1;
    public static final int SEND_DATA_FROM_AUTHOR_ACTIVITY = 2;
    DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVar();

        addEvents();

    }

    private void addEvents() {
        btnAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAuthorActivity.class);
                startActivity(intent);
            }
        });

        btnViewAuthorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AuthorManagerActivity.class);
                startActivity(intent);
            }
        });

        btnBookMgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setVar() {
        btnAddAuthor = (Button) findViewById(R.id.btnAddAuthor);
        btnBookMgr = (Button) findViewById(R.id.btnBookMgr);
        btnViewAuthorList = (Button) findViewById(R.id.btnViewAuthorList);

        databaseHelper = DatabaseHelper.getInstance(this);
    }

}
