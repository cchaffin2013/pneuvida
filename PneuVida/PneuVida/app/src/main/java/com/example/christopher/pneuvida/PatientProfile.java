package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PatientProfile extends AppCompatActivity {

    //db hanlder
    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

         myDBHandler = DBHandler.getDBHandler(this);

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final int patientID = profileData.getInt("patientID");

        //text fields
        TextView nameDisplay = (TextView) findViewById(R.id.name_display);
        TextView dobDisplay = (TextView) findViewById(R.id.dob_display);
        TextView sexDisplay = (TextView) findViewById(R.id.sex_display);
        TextView heightDisplay = (TextView) findViewById(R.id.height_display);
        TextView weightDisplay = (TextView) findViewById(R.id.weight_display);
        TextView medsDisplay = (TextView) findViewById(R.id.meds_display);
        TextView allergiesDisplay = (TextView) findViewById(R.id.allergies_display);
        TextView notesDisplay = (TextView) findViewById(R.id.notes_display);

        //get text field values from database
        nameDisplay.setText(myDBHandler.nameToString(patientID));
        dobDisplay.setText(myDBHandler.dobToString(patientID));
        sexDisplay.setText(myDBHandler.sexToString(patientID));
        heightDisplay.setText(myDBHandler.heightToString(patientID));
        weightDisplay.setText(myDBHandler.weightToString(patientID));
        medsDisplay.setText(myDBHandler.medsToString(patientID));
        allergiesDisplay.setText(myDBHandler.allergiesToString(patientID));
        notesDisplay.setText(myDBHandler.notesToString(patientID));

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
    }
}
