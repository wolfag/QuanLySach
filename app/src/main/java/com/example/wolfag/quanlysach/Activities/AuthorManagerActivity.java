package com.example.wolfag.quanlysach.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wolfag.quanlysach.DataHelper.DatabaseHelper;
import com.example.wolfag.quanlysach.Models.Author;
import com.example.wolfag.quanlysach.R;

import java.util.ArrayList;
import java.util.List;

public class AuthorManagerActivity extends Activity {

    ListView lvAuthorManager;
    Button btnBack;
    DatabaseHelper mDatabaseHelper = null;
    List<Author> authorList = null;
    ArrayAdapter<Author> authorArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_manager);

        setVar();

        addEvents();

        loadData();
    }

    private void loadData() {
        authorList.addAll(mDatabaseHelper.getAllAuthors());
        authorArrayAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setVar() {
        btnBack = (Button) findViewById(R.id.btnBack);
        lvAuthorManager = (ListView) findViewById(R.id.lvAuthorManager);
        mDatabaseHelper = DatabaseHelper.getInstance(this);

        authorList = new ArrayList<>();
        authorArrayAdapter = new ArrayAdapter<Author>(AuthorManagerActivity.this, android.R.layout.simple_list_item_1, authorList);
        lvAuthorManager.setAdapter(authorArrayAdapter);
    }
}
