package com.example.m4hmmd.bilancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewPost extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    EditText newTitle;
    EditText newDescription;
    EditText newDate;
    EditText newTime;
    EditText newPlace;
    EditText newQuota;

    // List of all categories (e.g MATH101)
    static List<String> categories;

    Spinner spinner;

    public void submitPost (View view) {
        // Dashboard.addPostDemo(newTitle.getText().toString(), newDescription.getText().toString() //@update
        //  , newDate.getText().toString(), newTime.getText().toString(), newPlace.getText().toString(), spinner.getSelectedItem().toString());

        //Dashboard.addPost(newTitle.getText().toString(), newDescription.getText().toString()
        //      , newDate.getText().toString(), newTime.getText().toString(), newPlace.getText().toString(), spinner.getSelectedItem().toString()); // @update
        Dashboard.addPost(newTitle.getText().toString()
                , newDescription.getText().toString()
                , newDate.getText().toString()
                , newTime.getText().toString()
                , newPlace.getText().toString()
                , spinner.getSelectedItem().toString()
                , Integer.parseInt(newQuota.getText().toString())
                , Dashboard.uID);
        // go to Dashboard
        Intent intent = new Intent(NewPost.this, Dashboard.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        database = FirebaseDatabase.getInstance();
        categories = new ArrayList<>();
        // fill up Categories from the database
        myRef = database.getReference("Categories");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cat : dataSnapshot.getChildren()){
                    categories.add(cat.getValue().toString());
                }
                fillSpinner();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        newTitle = (EditText) findViewById(R.id.titleField);
        newDescription = (EditText) findViewById(R.id.descriptionField);
        newDate = (EditText) findViewById(R.id.dateField);
        newTime = (EditText) findViewById(R.id.timeField);
        newPlace = (EditText) findViewById(R.id.placeField);
        newQuota = (EditText) findViewById(R.id.quotaField);

        spinner = (Spinner) findViewById(R.id.subjectSpinner);
    }

    public void fillSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setNotifyOnChange(true);

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
    }


}