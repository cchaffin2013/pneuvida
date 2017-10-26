package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {

    //db handler
    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        myDBHandler = DBHandler.getDBHandler(this);

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final int patientID = profileData.getInt("patientID");

        //edit text fields
        EditText nameDisplay = (EditText) findViewById(R.id.name_edit_text);
        EditText dobDisplay = (EditText) findViewById(R.id.dob_edit_text);
        EditText sexDisplay = (EditText) findViewById(R.id.sex_edit_text);
        EditText heightDisplay = (EditText) findViewById(R.id.height_edit_text);
        EditText weightDisplay = (EditText) findViewById(R.id.weight_edit_text);
        EditText medsDisplay = (EditText) findViewById(R.id.meds_edit_text);
        EditText allergiesDisplay = (EditText) findViewById(R.id.allergies_edit_text);
        EditText notesDisplay = (EditText) findViewById(R.id.note_edit_text);

        //get edit text field values from database
        nameDisplay.setText(myDBHandler.nameToString(patientID));
        dobDisplay.setText(myDBHandler.dobToString(patientID));
        sexDisplay.setText(myDBHandler.sexToString(patientID));
        heightDisplay.setText(myDBHandler.heightToString(patientID));
        weightDisplay.setText(myDBHandler.weightToString(patientID));
        medsDisplay.setText(myDBHandler.medsToString(patientID));
        allergiesDisplay.setText(myDBHandler.allergiesToString(patientID));
        notesDisplay.setText(myDBHandler.notesToString(patientID));

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
