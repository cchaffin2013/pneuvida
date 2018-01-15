package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.christopher.pneuvida.R.string.start_record_button;
import static com.example.christopher.pneuvida.R.string.stop_record_button;

public class Vitals extends AppCompatActivity {

    //buttons
    final Button recordingButton = (Button) findViewById(R.id.recordButton);
    final Button rrButton = (Button) findViewById((R.id.rr_button));
    final Button osButton = (Button) findViewById((R.id.os_button));
    final Button hrButton = (Button) findViewById((R.id.hr_button));
    final Button temperatureButton = (Button) findViewById((R.id.temperature_button));

    //text views
    final TextView overallDistressText = (TextView) findViewById(R.id.overall_distress_text);
    final TextView distressValueText = (TextView) findViewById(R.id.overall_distress_value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        //bluetooth setup

        //database handler
        DBHandler myDBHandler = DBHandler.getDBHandler(Vitals.this);

        //creates list used by spinner
        final List<Integer> patientIDs = myDBHandler.toList();
        List<String> patientNames = new ArrayList<String>();

        for(int i = 0; i < patientIDs.size(); i++) {
            patientNames.add(myDBHandler.getName(patientIDs.get(i)));
        }

        //sets up drop down menu called a spinner to get patients from database
        final Spinner patientSelect = (Spinner) findViewById(R.id.patient_select);
        ArrayAdapter<String> patientAdapter = new ArrayAdapter<String>(Vitals.this,
                android.R.layout.simple_list_item_1, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSelect.setAdapter(patientAdapter);


        //click listeners
        rrButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        //add extra intent data to indicate which vital value is being sent
                        startActivity(graphStart);
                    }
                }
        );
        osButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        startActivity(graphStart);
                    }
                }
        );
        hrButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        startActivity(graphStart);
                    }
                }
        );
        temperatureButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        startActivity(graphStart);
                    }
                }
        );
        recordingButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v){
                        if(recordingButton.getText().equals("Start Recording")) { //changes button text when clicked
                            recordingButton.setText(stop_record_button);
                            //change icon
                            //grey out spinner
                            patientSelect.setEnabled(false);
                            //record data for for currently selected profile
                        } else {
                            recordingButton.setText(start_record_button);
                            //change icon
                            //confirm data save
                            final AlertDialog.Builder myBuilder = new AlertDialog.Builder(Vitals.this);
                            View confirmView = getLayoutInflater().inflate(R.layout.confirm, null);

                            myBuilder.setView(confirmView);
                            final AlertDialog dialog = myBuilder.create();
                            dialog.show();

                            Button yButton = (Button) confirmView.findViewById(R.id.yes_button);
                            Button nButton = (Button) confirmView.findViewById(R.id.no_button);
                            TextView dialogMessage = (TextView) confirmView.findViewById(R.id.dialog_message);

                            dialogMessage.setText(R.string.save_confirm);

                            yButton.setOnClickListener( //confirms deletion then closes dialog
                                    new Button.OnClickListener() {
                                        public void onClick(View v) {
                                            //save data to db
                                            dialog.dismiss();
                                        }
                                    }
                            );

                            nButton.setOnClickListener( //closes dialog without deleting
                                    new Button.OnClickListener() {
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            //restore spinner
                            patientSelect.setEnabled(true);
                            //end recording
                        }
                    }
                }
        );
    }

    //make these able to vary with patient age
    //rating for respiratory rate
    private int rrRating(int rr) {
        if(rr < 12 || rr >= 25) {                  //severe distress
            return 2;
        } else if(rr > 18) {                       //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //rating for blood oxygen saturation
    private int osRating(int os) {
        if(os <= 91) {                            //severe distress
            return 2;
        } else if(os > 91 && os <= 95) {          //mild distress
            return 6;
        } else {                                  //no distress
            return 10;
        }
    }

    //rating for heart rate
    private int hrRating(int hr) {
        if(hr >= 100) {                            //severe distress
            return 2;
        } else if(hr > 76 && hr < 100) {           //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //rating for temperature
    private int temperatureRating(double temp) {
        if(temp >= 102.0) {                        //severe distress
            return 2;
        } else if(temp >= 98.6 && temp <= 101.9) { //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //color for respiratory rate
    private void rrColor(int rr) {
        int rating = rrRating(rr);
        switch (rating) {
            case 2:                                   //severe distress
                rrButton.setBackgroundColor(0xed0d0d);//red
                break;

            case 6:                                   //mild distress
                rrButton.setBackgroundColor(0xf7f713);//yellow
                break;

            case 10:                                  //no distress
                rrButton.setBackgroundColor(0x23df0e);//green
                break;

            default:
                break;
        }
    }

    //color for blood oxygen saturation
    private void osColor(int os) {
        int rating = osRating(os);
        switch (rating) {
            case 2:                                   //severe distress
                osButton.setBackgroundColor(0xed0d0d);//red
                break;

            case 6:                                   //mild distress
                osButton.setBackgroundColor(0xf7f713);//yellow
                break;

            case 10:                                  //no distress
                osButton.setBackgroundColor(0x23df0e);//green
                break;

            default:
                break;
        }
    }

    //color for heart rate
    private void hrColor(int hr) {
        int rating = hrRating(hr);
        switch (rating) {
            case 2:                                   //severe distress
                hrButton.setBackgroundColor(0xed0d0d);//red
                break;

            case 6:                                   //mild distress
                hrButton.setBackgroundColor(0xf7f713);//yellow
                break;

            case 10:                                  //no distress
                hrButton.setBackgroundColor(0x23df0e);//green
                break;

            default:
                break;
        }
    }

    //color for temperature
    private void tempColor(double temp) {
        int rating = temperatureRating(temp);
        switch (rating) {
            case 2:                                   //severe distress
                temperatureButton.setBackgroundColor(0xed0d0d);//red
                break;

            case 6:                                   //mild distress
                temperatureButton.setBackgroundColor(0xf7f713);//yellow
                break;

            case 10:                                  //no distress
                temperatureButton.setBackgroundColor(0x23df0e);//green
                break;

            default:
                break;
        }
    }

    //determine respiratory distress level
    private void overallDistress(int rr, int os, int hr, int temperature) {
        //determine overall distress level by calculating the weighted average of the vitals
        double overall = (rr * .4) + (os * .3) + (hr * .2) + (temperature * .1);
        //change color
        //add color gradients
        //temporary values, work on getting these nailed down
        if (overall > 7.6) {
            overallDistressText.setBackgroundColor(0x23df0e); //green
            distressValueText.setBackgroundColor(0x23df0e);
        } else if (overall > 4) {
            overallDistressText.setBackgroundColor(0xf7f713); //yellow
            distressValueText.setBackgroundColor(0xf7f713);
        } else {
            overallDistressText.setBackgroundColor(0xed0d0d); //red
            distressValueText.setBackgroundColor(0xed0d0d);
        }
        //display overall rating number
        distressValueText.setText(String.valueOf(overall));
    }
}
