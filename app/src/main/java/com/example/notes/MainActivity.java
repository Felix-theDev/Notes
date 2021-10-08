package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;

    //ArrayList that stores the titles of all saved note
    static ArrayList<String> titlesArray = new ArrayList<>();
    static ArrayList<String> detailsArray = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(), Notes.class);
                intent.putExtra("Task", "new");
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        titlesArray.clear();
        detailsArray.clear();
        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            titlesArray =(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("titlesArray", ObjectSerializer.serialize(new ArrayList<>())));
            detailsArray =(ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("detailsArray", ObjectSerializer.serialize(new ArrayList<>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titlesArray);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("INFO", String.valueOf(position));
                Intent intent = new Intent(getApplicationContext(), Notes.class);
                intent.putExtra("Position", position);
                startActivityForResult(intent, 1);
            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                titlesArray.remove(position);
                                detailsArray.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                try {
                                    sharedPreferences.edit().putString("titlesArray", ObjectSerializer.serialize(MainActivity.titlesArray)).apply();
                                    sharedPreferences.edit().putString("detailsArray", ObjectSerializer.serialize(MainActivity.detailsArray)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });


    }

}