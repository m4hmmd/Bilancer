package com.example.m4hmmd.bilancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editInfo;
    private EditText editContactInfo;
    private Button goToMyProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        goToMyProfile = (Button) findViewById(R.id.goToMyProfile);
        editInfo = (EditText) findViewById(R.id.editInfo);
        editContactInfo = (EditText) findViewById(R.id.editContactInfo);
        goToMyProfile.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        if (view == goToMyProfile){
            Intent intent = new Intent(EditProfileActivity.this, MyProfile.class);
            startActivity(intent);
        }
    }

    public String getInfo(){
        String info = editInfo.getText().toString().trim();
        return info;
    }
    public String getContactInfo() {
        String contactInfo = editContactInfo.getText().toString().trim();
        return contactInfo;
    }

}
