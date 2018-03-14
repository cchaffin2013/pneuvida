package com.example.christopher.pneuvida;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.christopher.pneuvida.R.string.start_record_button;
import static com.example.christopher.pneuvida.R.string.stop_record_button;

public class Vitals extends AppCompatActivity {
    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // MAC-address of Bluetooth module
    private static String address = "00:14:03:06:41:DD";

    //Bluetooth handler
    Handler h;

    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

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

        //find better way to do this
        final int[] vitalsArray = new int[4];

        //Bluetooth handler
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        int vitalValue;
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear
                            switch (sbprint.substring(0,1)) {
                                case "o":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    osButton.setText("Oxygen Saturation\n" + vitalValue +"%");
                                    osColor(vitalValue, osButton);
                                    vitalsArray[0] = vitalValue;
                                    break;
                                case "r":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    rrButton.setText("Respiratory Rate\n" + vitalValue + " BPM");
                                    rrColor(vitalValue, rrButton);
                                    vitalsArray[1] = vitalValue;
                                    break;
                                case "h":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    hrButton.setText("Heart Rate\n" + vitalValue + " BPM");
                                    hrColor(vitalValue, hrButton);
                                    vitalsArray[2] = vitalValue;
                                    break;
                                case "t":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    temperatureButton.setText("Temperature\n" + vitalValue + " F");
                                    tempColor((double) vitalValue, temperatureButton);
                                    vitalsArray[3] = vitalValue;
                                    break;
                                default:
                                    overallDistress(osRating(vitalsArray[0]), rrRating(vitalsArray[1]), hrRating(vitalsArray[2]), temperatureRating(vitalsArray[3]), overallDistressText, distressValueText);
                                    break;
                            }
                        }
                        break;
                }
            }
        };

        // get Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

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
                        graphStart.putExtra("vital", "Respiratory Rate");//add extra intent data to indicate which vital value is being sent
                        startActivity(graphStart);

                    }
                }
        );
        osButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        graphStart.putExtra("vital", "Blood O2 Saturation");//add extra intent data to indicate which vital value is being sent
                        startActivity(graphStart);
                    }
                }
        );
        hrButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        graphStart.putExtra("vital", "Heart Rate");//add extra intent data to indicate which vital value is being sent
                        startActivity(graphStart);
                    }
                }
        );
        temperatureButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent graphStart = new Intent(Vitals.this, VitalsGraph.class);
                        graphStart.putExtra("vital", "Temperature");//add extra intent data to indicate which vital value is being sent
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
                            //start recording
                            mConnectedThread.write("1");

                        } else {
                            recordingButton.setText(start_record_button);
                            //end recording
                            mConnectedThread.write("0");
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

                            yButton.setOnClickListener( //confirms saving then closes dialog
                                    new Button.OnClickListener() {
                                        public void onClick(View v) {
                                            //save data to db
                                            dialog.dismiss();
                                        }
                                    }
                            );

                            nButton.setOnClickListener( //closes dialog without saving
                                    new Button.OnClickListener() {
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            //restore spinner
                            patientSelect.setEnabled(true);
                        }
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    //Bluetooth methods
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {

            }

        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {

            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {

            }
        }
    }

    //Vitals ratings
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
                rrButton.setBackgroundResource(R.color.severeDistress);//red
                break;

            case 6:                                   //mild distress
                rrButton.setBackgroundResource(R.color.mildDistress);//yellow
                break;

            case 10:                                  //no distress
                rrButton.setBackgroundResource(R.color.noDistress);//green
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
                osButton.setBackgroundResource(R.color.severeDistress);//red
                break;

            case 6:                                   //mild distress
                osButton.setBackgroundResource(R.color.mildDistress);//yellow
                break;

            case 10:                                  //no distress
                osButton.setBackgroundResource(R.color.noDistress);//green
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
                hrButton.setBackgroundResource(R.color.severeDistress);//red
                break;

            case 6:                                   //mild distress
                hrButton.setBackgroundResource(R.color.mildDistress);//yellow
                break;

            case 10:                                  //no distress
                hrButton.setBackgroundResource(R.color.noDistress);//green
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
                temperatureButton.setBackgroundResource(R.color.severeDistress);//red
                break;

            case 6:                                   //mild distress
                temperatureButton.setBackgroundResource(R.color.mildDistress);//yellow
                break;

            case 10:                                  //no distress
                temperatureButton.setBackgroundResource(R.color.noDistress);//green
                break;

            default:
                break;
        }
    }

    //determine overall respiratory distress level
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
