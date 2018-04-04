package com.example.christopher.pneuvida;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VitalsData extends AppCompatActivity {

    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals_data);

        //set orientation based on screen size
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            default:
        }

        //database handler
        myDBHandler = DBHandler.getDBHandler(this);

        TextView rrData = (TextView) findViewById(R.id.rr_data_text);
        TextView osData = (TextView) findViewById(R.id.os_data_text);
        TextView hrData = (TextView) findViewById(R.id.hr_data_text);
        TextView tempData = (TextView) findViewById(R.id.temp_data_text);

        Bundle profileData = getIntent().getExtras();
        final int patientID = profileData.getInt("patientID");

        String rr = myDBHandler.getRR(patientID);
        String os = myDBHandler.getOS(patientID);
        String hr = myDBHandler.getHR(patientID);
        String temp = myDBHandler.getTemp(patientID);

        rrData.setText(rr.substring(0, rr.length()-1));
        osData.setText(os.substring(0, os.length()-1));
        hrData.setText(hr.substring(0, hr.length()-1));
        tempData.setText(temp.substring(0, temp.length()-1));

    }
}
