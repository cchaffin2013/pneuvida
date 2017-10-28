package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PatientProfileMenu extends AppCompatActivity {

    //database handler
    DBHandler myDBHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_menu);

        //database handler
         myDBHandler = DBHandler.getDBHandler(this);

        //temporary patients for testing, this will be deleted later
        myDBHandler.destroy();
        final Patient patient1 = new Patient("John Doe");
        patient1.set_id(1);
        patient1.set_dob("05/25/51");
        patient1.set_sex("male");
        patient1.set_height("6 ft");
        patient1.set_weight("160 lbs");
        patient1.set_meds("pneumonia meds");
        patient1.set_allergies("penicillin");
        patient1.set_notes("This is a test");
        myDBHandler.addPatient(patient1);
        final Patient patient2 = new Patient("Jane Doe");
        patient2.set_id(2);
        patient2.set_dob("03/18/2012");
        patient2.set_sex("female");
        patient2.set_height("3 ft");
        patient2.set_weight("50 lbs");
        patient2.set_meds("pneumonia meds");
        patient2.set_allergies("none");
        patient2.set_notes("This is a test");
        myDBHandler.addPatient(patient2);
        final Patient patient3 = new Patient("Jack doe");
        patient3.set_id(3);
        patient3.set_dob("05/25/85");
        patient3.set_sex("male");
        patient3.set_height("6 ft");
        patient3.set_weight("180");
        patient3.set_meds("pneumonia meds");
        patient3.set_allergies("none");
        patient3.set_notes("This is still a test");
        myDBHandler.addPatient(patient3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //buttons
        final Button profile1 = (Button) findViewById(R.id.profile_1);
        final Button profile2 = (Button) findViewById(R.id.profile_2);
        final Button profile3 = (Button) findViewById(R.id.profile_3);
        final Button addProfile = (Button) findViewById(R.id.add_profile_button);
        final Button deleteProfile = (Button) findViewById(R.id.delete_profile_button);

        //temporary for testing
        profile1.setText(myDBHandler.getName(1));
        profile2.setText(myDBHandler.getName(2));
        profile3.setText(myDBHandler.getName(3));

        //button click listeners
        addProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent newProfileStart = new Intent(PatientProfileMenu.this, EditProfile.class);
                        newProfileStart.putExtra("patientID", 0);
                        startActivity(newProfileStart);
                    }
                }
        );

        deleteProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                    }
                }
        );

        profile1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        profileStart.putExtra("patientID", 1);
                        startActivity(profileStart);
                    }
                }
        );

        profile2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        profileStart.putExtra("patientID", 2);
                        startActivity(profileStart);
                    }
                }
        );

        profile3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        profileStart.putExtra("patientID", 3);
                        startActivity(profileStart);
                    }
                }
        );
    }
}
