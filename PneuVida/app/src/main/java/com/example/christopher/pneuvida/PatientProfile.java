package com.example.christopher.pneuvida;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PatientProfile extends AppCompatActivity {

    //db handler
    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //updates to display changes in database made in edit activity
        //database handler
        myDBHandler = DBHandler.getDBHandler(this);

        //text fields
        TextView nameDisplay = (TextView) findViewById(R.id.name_display);
        TextView dobDisplay = (TextView) findViewById(R.id.dob_display);
        TextView sexDisplay = (TextView) findViewById(R.id.sex_display);
        TextView heightDisplay = (TextView) findViewById(R.id.height_display);
        TextView weightDisplay = (TextView) findViewById(R.id.weight_display);
        TextView medsDisplay = (TextView) findViewById(R.id.meds_display);
        TextView allergiesDisplay = (TextView) findViewById(R.id.allergies_display);
        TextView notesDisplay = (TextView) findViewById(R.id.notes_display);

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final int patientID = profileData.getInt("patientID");

        //get text field values from database
        nameDisplay.setText(myDBHandler.getName(patientID));
        dobDisplay.setText(myDBHandler.getDOB(patientID));
        sexDisplay.setText(myDBHandler.getSex(patientID));
        heightDisplay.setText(myDBHandler.getHeight(patientID));
        weightDisplay.setText(myDBHandler.getWeight(patientID));
        medsDisplay.setText(myDBHandler.getMeds(patientID));
        allergiesDisplay.setText(myDBHandler.getAllergies(patientID));
        notesDisplay.setText(myDBHandler.getNotes(patientID));

        //button to allow profile edit
        final ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);

        editButton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent editStart = new Intent(PatientProfile.this, EditProfile.class); //allow text to be edited which will edit the contents of the database
                        editStart.putExtra("patientID", patientID);
                        startActivity(editStart);
                    }
                }
        );

        //button to see stored vital values
        final ImageButton vitalsDataButton = (ImageButton) findViewById(R.id.vitals_data_button);

        vitalsDataButton.setOnClickListener(
            new ImageButton.OnClickListener() {
                public void onClick(View v) {
                    Intent vitalDataStart = new Intent(PatientProfile.this, VitalsData.class);
                    vitalDataStart.putExtra("patientID", patientID);
                    startActivity(vitalDataStart);
                }
            }
        );

    }
}
