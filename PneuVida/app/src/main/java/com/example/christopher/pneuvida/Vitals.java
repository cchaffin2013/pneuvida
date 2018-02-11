package com.example.christopher.pneuvida;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.christopher.pneuvida.R.string.start_record_button;
import static com.example.christopher.pneuvida.R.string.stop_record_button;

public class Vitals extends Bluetooth {

    //bluetooth handler
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)  {
            super.handleMessage(msg);
            switch(msg.what){
                case Bluetooth.SUCCESS_CONNECT:
                    Bluetooth.connectedThread = new Bluetooth.ConnectedThread((BluetoothSocket)msg.obj);
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    String s = "successfully connected";
                    Bluetooth.connectedThread.start();
                    break;
                case Bluetooth.MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf, 0, 5);                 // create string from bytes array
                    Toast.makeText(getApplicationContext(),strIncom, Toast.LENGTH_LONG);     //display message received

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        //go to bluetooth activity to enable bt and connect to device
        Intent bluetoothStart = new Intent(Vitals.this, Bluetooth.class);
        startActivity(bluetoothStart);

        Bluetooth.getHandler(mHandler);

        //buttons
        final Button recordingButton = (Button) findViewById(R.id.recordButton);
        final Button rrButton = (Button) findViewById((R.id.rr_button));
        final Button osButton = (Button) findViewById((R.id.os_button));
        final Button hrButton = (Button) findViewById((R.id.hr_button));
        final Button temperatureButton = (Button) findViewById((R.id.temperature_button));

        //text views
        final TextView overallDistressText = (TextView) findViewById(R.id.overall_distress_text);
        final TextView distressValueText = (TextView) findViewById(R.id.overall_distress_value);

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

        //tset overall distress gradient
        overallDistress(2,6,10,6, overallDistressText,distressValueText);

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
                            //grey out spinner
                            patientSelect.setEnabled(false);
                            //record data for for currently selected profile
                        } else {
                            recordingButton.setText(start_record_button);
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
    private void rrColor(int rr, Button rrButton) {
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
    private void osColor(int os, Button osButton) {
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
    private void hrColor(int hr, Button hrButton) {
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
    private void tempColor(double temp, Button temperatureButton) {
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
    private void overallDistress(int os, int rr, int hr, int temperature, TextView overallDistressText, TextView distressValueText) {
        //determine overall distress level by calculating the weighted average of the vitals
        double overall = (os * .4) + (rr * .3) + (hr * .2) + (temperature * .1);
        //change color
        //add color gradients
        //temporary values, work on getting these nailed down
        if (overall > 7.6) {
            overallDistressText.setBackgroundResource(R.color.noDistress); //green
            distressValueText.setBackgroundResource(R.color.noDistress);
        } else if (overall > 4) {
            overallDistressText.setBackgroundResource(R.color.mildDistress); //yellow
            distressValueText.setBackgroundResource(R.color.mildDistress);
        } else {
            overallDistressText.setBackgroundResource(R.color.severeDistress); //red
            distressValueText.setBackgroundResource(R.color.severeDistress);
        }
        //display overall rating number
        distressValueText.setText(String.format("%.2f", overall));
    }

}
