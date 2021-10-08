package com.example.notes;
/** A notbook app
 * @author Felix Ogbonnaya
 * @since 2020-06-20
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Notes extends AppCompatActivity {
ArrayList<String> titles;
ArrayList<String> details;
TextView title;
TextView detail;
boolean exist;
boolean progress;
int position;
int index;
int currentPosition;
SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent = getIntent();
        title = findViewById(R.id.title);
        detail = findViewById(R.id.details);
        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE );

        try {
            if(intent.getStringExtra("Task").equals("New")){
                exist = false;
                progress = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        position = intent.getIntExtra("Position", -1);

        if(position != -1){
            exist = true;
            Log.i("INFOS", MainActivity.titlesArray.get(position));
            title.setText(MainActivity.titlesArray.get(position));
            detail.setText(MainActivity.detailsArray.get(position));
        }

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!exist){
                    if(progress){
                        MainActivity.titlesArray.set(index, title.getText().toString());
                    }
                    else {
                        MainActivity.titlesArray.add(title.getText().toString());
                        MainActivity.detailsArray.add("");
                        index = MainActivity.titlesArray.size() -1;
                        Log.i("INFO", String.valueOf(index));

                        progress = true;
                    }
                }else{
                    MainActivity.titlesArray.set(position, title.getText().toString());
                }
                MainActivity.arrayAdapter.notifyDataSetChanged();
                savePreference();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
                @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!exist){
                    if(progress){
                        MainActivity.detailsArray.set(index, detail.getText().toString());
                    }else {
                        MainActivity.detailsArray.add(detail.getText().toString());
                        MainActivity.titlesArray.add("");
                        index = MainActivity.detailsArray.size() - 1;
                        progress = true;
                    }
                }
                else {
                    MainActivity.detailsArray.set(position, detail.getText().toString());
                }


                MainActivity.arrayAdapter.notifyDataSetChanged();
                savePreference();

            }
        });

    }
    public void savePreference(){
        try {
            sharedPreferences.edit().putString("titlesArray", ObjectSerializer.serialize(MainActivity.titlesArray)).apply();
            sharedPreferences.edit().putString("detailsArray", ObjectSerializer.serialize(MainActivity.detailsArray)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reverseArray(){
        Collections.reverse(MainActivity.titlesArray);
        Collections.reverse(MainActivity.detailsArray);
    }


}