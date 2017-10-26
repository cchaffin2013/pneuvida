package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //database handler
        //DBHandler myDBHandler = DBHandler.getDBHandler(this);

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final String patientName = profileData.getString("patientName");
        final String dob = profileData.getString("dob");
        EditText nameDisplay = (EditText) findViewById(R.id.name_edit_text);
        EditText dobDisplay = (EditText) findViewById(R.id.dob_edit_text);

        nameDisplay.setText(patientName);
        dobDisplay.setText(dob);
        //dobDisplay.setText(myDBHandler.dobToString(patientName));

        //buttons
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        //button click listeners
        saveButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //save data to database

                        finish();//close activity after saving
                    }
                }
        );

        cancelButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();//close activity without saving changes
                    }
                }
        );

    }
}
