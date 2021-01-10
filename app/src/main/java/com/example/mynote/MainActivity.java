package com.example.mynote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.example.mynote.adapter.RecyclerViewAdapter;
import com.example.mynote.dao.DBhandler;
import com.example.mynote.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{


    private TextView textView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainMenu start = new mainMenu();
        start.execute((String) null);
        fab = findViewById(R.id.newbtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,noteEditor.class);
                intent.putExtra("isnew",true);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    finish();
    }

    public class mainMenu extends AsyncTask<String, Void, String>{
        private RecyclerView recyclerView;
        private RecyclerViewAdapter recyclerViewAdapter;
        private ArrayList<Note> noteArrayList = new ArrayList<>();

        DBhandler db;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noteArrayList.clear();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            db = new DBhandler(MainActivity.this);
            noteArrayList.addAll(db.getAllNotes());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            db.close();
            if(noteArrayList.isEmpty()){
               textView = findViewById(R.id.textView);
               textView.setText("Tap '+' to Create a New Note");
            }
            else {
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, noteArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        }
    }
}
