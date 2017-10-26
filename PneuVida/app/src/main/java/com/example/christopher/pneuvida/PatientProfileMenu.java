package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PatientProfileMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_menu);

        //database handler
        final DBHandler myDBHandler = DBHandler.getDBHandler(this);

        //temporary patients for testing this will be deleted later
        final Patient patient1 = new Patient("profile 1");
        patient1.set_id(1);
        patient1.set_dob("05/25/72");
        patient1.set_sex("male");
        patient1.set_height("6 ft");
        patient1.set_weight("160");
        patient1.set_meds("pneumonia meds");
        patient1.set_allergies("penicillin");
        patient1.set_notes("This is a test");
        myDBHandler.addPatient(patient1);

        //buttons
        final Button profile1 = (Button) findViewById(R.id.profile_1);
        final Button profile2 = (Button) findViewById(R.id.profile_2);
        final Button profile3 = (Button) findViewById(R.id.profile_3);
        final Button addProfile = (Button) findViewById(R.id.add_profile_button);
        final Button deleteProfile = (Button) findViewById(R.id.delete_profile_button);

        //button click listeners
        addProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

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
                        //profileStart.putExtra("patientName", patient1.get_name());//change to id later
                        String dob = "05/25/1995";
                        String name = "Profile 1";
                        profileStart.putExtra("dob", dob);
                        profileStart.putExtra("patientName", name);
                        startActivity(profileStart);
                }
            }
        );

        profile2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        String dob = "02/25/2005";
                        String name = "Profile 2";
                        profileStart.putExtra("dob", dob);
                        profileStart.putExtra("patientName", name);
                        startActivity(profileStart);
                    }
                }
        );

        profile3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        String dob = "03/17/1950";
                        String name = "Profile 3";
                        profileStart.putExtra("dob", dob);
                        profileStart.putExtra("patientName", name);
                        startActivity(profileStart);
                    }
                }
        );

    }
}
