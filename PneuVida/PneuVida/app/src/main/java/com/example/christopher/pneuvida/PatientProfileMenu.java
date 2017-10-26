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

        myDBHandler.destroy();
        //temporary patients for testing, this will be deleted later
        final Patient patient1 = new Patient("1 works");
        //patient1.set_id(1);
        patient1.set_dob("05/25/71");
        patient1.set_sex("male");
        patient1.set_height("6 ft");
        patient1.set_weight("160");
        patient1.set_meds("pneumonia meds");
        patient1.set_allergies("penicillin");
        patient1.set_notes("This is a test");
        myDBHandler.addPatient(patient1);
        //temporary patients for testing, this will be deleted later
        final Patient patient2 = new Patient("2 works");
        //patient2.set_id(1);
        patient2.set_dob("05/25/72");
        patient2.set_sex("male");
        patient2.set_height("6 ft");
        patient2.set_weight("160");
        patient2.set_meds("pneumonia meds");
        patient2.set_allergies("penicillin");
        patient2.set_notes("This is a test");
        myDBHandler.addPatient(patient2);
        //temporary patients for testing, this will be deleted later
        final Patient patient3 = new Patient("3 works");
        //patient3.set_id(1);
        patient3.set_dob("05/25/73");
        patient3.set_sex("male");
        patient3.set_height("6 ft");
        patient3.set_weight("160");
        patient3.set_meds("pneumonia meds");
        patient3.set_allergies("penicillin");
        patient3.set_notes("This is a test");
        myDBHandler.addPatient(patient3);

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
                        profileStart.putExtra("patientName", patient1.get_name());//change to id later
                        startActivity(profileStart);
                }
            }
        );

        profile2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        profileStart.putExtra("patientName", patient2.get_name());
                        startActivity(profileStart);
                    }
                }
        );

        profile3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent profileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);
                        profileStart.putExtra("patientName", patient3.get_name());
                        startActivity(profileStart);
                    }
                }
        );

    }
}
