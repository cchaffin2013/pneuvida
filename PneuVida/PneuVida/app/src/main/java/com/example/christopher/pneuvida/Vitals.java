package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import static com.example.christopher.pneuvida.R.string.start_record_button;
import static com.example.christopher.pneuvida.R.string.stop_record_button;

public class Vitals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        //drop down menu
        Spinner patientSelect = (Spinner) findViewById(R.id.patient_select);
        ArrayAdapter<String> patientAdapter = new ArrayAdapter<String>(Vitals.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.patients));
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSelect.setAdapter(patientAdapter);

        //buttons
        final Button recordingButton = (Button) findViewById(R.id.recordButton);

        //button click listeners
        recordingButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v){
                        if(recordingButton.getText().equals("Start Recording")) { //changes button text when clicked
                            recordingButton.setText(stop_record_button);
                            //change icon
                            //record data for for currently selected profile
                        } else {
                            recordingButton.setText(start_record_button);
                            //change icon
                            //end recording
                        }
                    }
                }
        );
    }
}
