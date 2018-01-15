package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //buttons
        final Button vitalsButton = (Button) findViewById(R.id.vitals_display_button);
        final Button patientButton = (Button) findViewById(R.id.patient_profiles_button);
        final Button settingsButton = (Button) findViewById(R.id.settings_button);
        final Button helpButton = (Button) findViewById(R.id.help_button);

        //button click listeners
        vitalsButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent vitalStart = new Intent(Home.this, Vitals.class);
                        startActivity(vitalStart);
                    }
                }
        );

        patientButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick (View v) {
                        Intent patientMenuStart = new Intent(Home.this, PatientProfileMenu.class);
                        startActivity(patientMenuStart);
                    }
                }
        );

        settingsButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent settignsStart = new Intent(Home.this, Settings.class);
                        startActivity(settignsStart);
                    }
                }
        );

        helpButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent helpStart = new Intent(Home.this, HelpAbout.class);
                        startActivity(helpStart);
                    }
                }
        );
    }
}
