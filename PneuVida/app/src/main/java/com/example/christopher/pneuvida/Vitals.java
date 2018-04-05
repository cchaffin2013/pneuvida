package com.example.christopher.pneuvida;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.example.christopher.pneuvida.R.string.start_record_button;
import static com.example.christopher.pneuvida.R.string.stop_record_button;

public class Vitals extends AppCompatActivity {
    final int RECEIVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    // MAC-address of Bluetooth module
    private static String address = "98:D3:31:90:4E:2E";//"00:14:03:06:41:DD";
    //Bluetooth handler
    Handler h;

    //signal arrays
    int sampleSize = 2000;
    double[] redSignal = new double[sampleSize];
    double[] irSignal = new double[sampleSize];
    int count = 0;

    //vital values
    int totalHR = 0;
    int rr = 0;
    double spo2 = 0;
    double temp = 0;
    int hRating;
    int oRating;
    int rRating;
    int tRating;

    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

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
        final DBHandler myDBHandler = DBHandler.getDBHandler(Vitals.this);

        //creates list used by spinner
        final List<Integer> patientIDs = myDBHandler.toList();
        final List<String> patientNames = new ArrayList<String>();

        for(int i = 0; i < patientIDs.size(); i++) {
            patientNames.add(myDBHandler.getName(patientIDs.get(i)));
        }

        //lists for storing vital values
        final List<String> rrData = new LinkedList<>();
        final List<String> osData = new LinkedList<>();
        final List<String> hrData = new LinkedList<>();
        final List<String> tempData = new LinkedList<>();

        //Bluetooth handler
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);           // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end of line
                        if (endOfLineIndex > 0) {                                           // if end of line
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // clear string builder
                            if (count < sampleSize) {
                                redSignal[count] = Double.parseDouble(sbprint);
                                count++;
                                mConnectedThread.write("2");                                 // tell arduino message was received and processed
                            } else if (count < sampleSize * 2) {
                                irSignal[count - sampleSize] = Double.parseDouble(sbprint);
                                count++;
                                mConnectedThread.write("2");                                 // tell arduino message was received and processed
                            } else {
                                //reset counter
                                count = 0;
                                //get temperature value
                                temp = Double.parseDouble(sbprint);
                                //calculate remaining vitals
                                totalHR = calculateHR(redSignal) + calculateHR(irSignal);
                                rr = calculateRR(totalHR);
                                spo2 = calculateOS(redSignal, irSignal);
                                //calculate ratings
                                hRating = hrRating(totalHR);
                                rRating = rrRating(rr);
                                oRating = osRating(spo2);
                                tRating = temperatureRating(temp);
                                //change colors and text based on ratings
                                hrButton.setText("Heart Rate\n" + totalHR + " BPM");
                                hrColor(hRating, hrButton);
                                rrButton.setText("Respiratory Rate\n" + rr + " BPM");
                                rrColor(rRating, rrButton);
                                osButton.setText(String.format("Oxygen Saturation\n %.2f", spo2));
                                osColor(oRating, osButton);
                                temperatureButton.setText("Temperature\n" + temp + " F");
                                tempColor(tRating, temperatureButton);
                                overallDistress(oRating, rRating, hRating, tRating, overallDistressText, distressValueText);
                                //add data to lists to be potentially stored in db
                                hrData.add(Integer.toString(totalHR));
                                rrData.add(Integer.toString(rr));
                                osData.add(Double.toString(spo2));
                                tempData.add(Double.toString(temp));

                                mConnectedThread.write("1");
                            }
                        }
                    break;
                            /*switch (sbprint.substring(0,1)) {
                                case "o":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    vitalsArray[0] = vitalValue;
                                    osData.add(Integer.toString(vitalValue));
                                    break;
                                case "r":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    vitalsArray[1] = vitalValue;
                                    rrData.add(Integer.toString(vitalValue));
                                    break;
                                case "h":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    vitalsArray[2] = vitalValue;
                                    hrData.add(Integer.toString(vitalValue));
                                    break;
                                case "t":
                                    vitalValue = Integer.parseInt(sbprint.substring(1));
                                    vitalsArray[3] = vitalValue;
                                    tempData.add(Integer.toString(vitalValue));
                                    break;
                                default:
                                    //update button text and colors
                                    osButton.setText("Oxygen Saturation\n" + vitalsArray[0] +"%");
                                    osColor(vitalsArray[0], osButton);
                                    rrButton.setText("Respiratory Rate\n" + vitalsArray[1] + " BPM");
                                    rrColor(vitalsArray[1], rrButton);
                                    hrButton.setText("Heart Rate\n" + vitalsArray[2] + " BPM");
                                    hrColor(vitalsArray[2], hrButton);
                                    temperatureButton.setText("Temperature\n" + vitalsArray[3] + " F");
                                    tempColor((double) vitalsArray[3], temperatureButton);
                                    overallDistress(osRating(vitalsArray[0]), rrRating(vitalsArray[1]), hrRating(vitalsArray[2]), temperatureRating(vitalsArray[3]), overallDistressText, distressValueText);
                                    break;
                            }*/
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
                                            int id =  patientIDs.get(patientNames.indexOf(patientSelect.getSelectedItem().toString()));
                                            myDBHandler.setRR(rrData, id);
                                            myDBHandler.setOS(osData, id);
                                            myDBHandler.setHR(hrData, id);
                                            myDBHandler.setTemp(tempData, id);
                                            Toast.makeText(getApplicationContext(),"Data Saved", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                    }
                            );

                            nButton.setOnClickListener( //closes dialog without saving
                                    new Button.OnClickListener() {
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(),"Data Not Saved", Toast.LENGTH_LONG).show();
                                            rrData.clear();
                                            osData.clear();
                                            hrData.clear();
                                            tempData.clear();
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

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        btAdapter.cancelDiscovery();

        // Establish connection
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

        // Create stream
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        try     {
            mConnectedThread.write("0");
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
        // Check for Bluetooth support, then tell user to turn it on if it is not already
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {

            } else {
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
                    h.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
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
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


    //for calculating vitals from ppg signal
    private static int calculateRR(int hr) {
        return hr/4;
    }

    private static double calculateOS(double[] signalArray1, double[] signalArray2) {
        int st = 60;
        double[] img = new double[signalArray1.length];
        FFT f = new FFT(1024);
        f.fft(signalArray1, img);
        img = new double[signalArray2.length];
        f.fft(signalArray2, img);
        double[] subArray = new double[1024];
        int localMaxI;
        double localMax;
        int pkRedI;
        int pkIrI;
        double rRed;
        double rIR;
        double r;

        //find red local max
        for(int i = st; i < st + subArray.length; i++) {
            subArray[i-st] = Math.abs(signalArray1[i]);
        }
        localMaxI = 0;
        localMax = subArray[0];
        for(int i = 19; i < subArray.length - 1; i++) {
            if(localMax < subArray[i]) {
                localMaxI = i;
                localMax = subArray[i];
            }
        }
        pkRedI = st + localMaxI;

        //find ir local max
        for(int i = st; i < st + subArray.length; i++) {
            subArray[i-st] = Math.abs(signalArray2[i]);
        }
        localMaxI = 0;
        localMax = subArray[0];
        for(int i = 19; i < subArray.length - 1; i++) {
            if(localMax < subArray[i]) {
                localMaxI = i;
                localMax = subArray[i];
            }
        }
        pkIrI = st + localMaxI;

        //SpO2 calculation
        rRed = Math.abs(signalArray1[pkRedI]/signalArray1[1]);
        rIR = Math.abs(signalArray2[pkIrI]/signalArray2[1]);
        r = rRed/rIR;
        double spo2 = (110 - (25*r));

        if(spo2 > 100) {
            spo2 = 99;
        }

        return spo2;
    }

    private static int calculateHR(double[] sa) {

        double[] signalArray = sa.clone();
        //heart rate
        int hr = 0;

        //local max
        int localIMax;
        double localMax;

        //peaks array list
        ArrayList<Integer> pk = new ArrayList<Integer>();

        //endpoint of data
        /*int endPoint = 0;
        //detect end of data
        for (int i = 2000; i < signalArray.length-2; i++) {

            if(signalArray[i] == 0 && signalArray[i+1] == 0 && signalArray[i+2] == 0) {
                endPoint = i;
            }
        }*/

        //sampling rate
        short fs = 250;

        //Heart Beat
        //******moving average filter
        for(int i = 0; i < signalArray.length - (fs/50); i++) {
            double localSum = 0;
            for(int j = 0; j < (fs/50); j++) {
                localSum = localSum + signalArray[i+j];
            }
            signalArray[i] = localSum/(fs/5);
        }

        //******find peaks
        int leap = 0;
        int pkI = 0;
        double[] sampleArray = new double[fs];


        while(leap <= signalArray.length - fs) {
            for (int i = 0; i < fs; i++) {
                sampleArray[i] = signalArray[i+leap];
            }

            localIMax = 1;
            localMax = sampleArray[localIMax];

            for(int i = 1; i < fs; i++) {
                if(localMax < sampleArray[i]) {
                    localIMax = i;
                    localMax = sampleArray[i];
                }
            }

            pk.add(pkI, leap+localIMax);
            pkI++;
            leap = leap + fs;
        }

        int beat = 0;
        int beatI = 1;

        for(int i = 0; i < pk.size() - 1; i++) {
            if(pk.get(i) < pk.get(i+1) - 100) {
                beat += (fs/(pk.get(i+1) - pk.get(i))) * 60;
                beatI++;
            }
        }

        if(beatI > 1) {
            hr = beat/(beatI - 2);
        }

        return hr;
    }

    //Vitals ratings
    //make these able to vary with patient age
    //rating for respiratory rate
    private int rrRating(int rr) {
        if(rr < 8) {
            return 0;
        } else if(rr < 12 || rr >= 25) {           //severe distress
            return 2;
        } else if(rr > 18) {                       //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //rating for blood oxygen saturation
    private int osRating(double os) {
        if(os < 88) {
            return 0;
        } else if(os <= 91.0) {                   //severe distress
            return 2;
        } else if(os > 91.0 && os <= 95.0) {      //mild distress
            return 6;
        } else {                                  //no distress
            return 10;
        }
    }

    //rating for heart rate
    private int hrRating(int hr) {
        if(hr < 50) {
            return 0;
        } else if(hr >= 100) {                     //severe distress
            return 2;
        } else if(hr > 76 && hr < 100) {           //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //rating for temperature
    private int temperatureRating(double temp) {
        if (temp < 94) {
            return 0;
        }else if(temp >= 102.0) {                  //severe distress
            return 2;
        } else if(temp >= 98.8 && temp <= 101.9) { //mild distress
            return 6;
        } else {                                   //no distress
            return 10;
        }
    }

    //color for respiratory rate
    private void rrColor(int rating, Button rrButton) {
        //int rating = rrRating(rr);
        switch (rating) {
            case 2:                                   //severe distress
                rrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.severeDistress), PorterDuff.Mode.MULTIPLY);//red
                break;

            case 6:                                   //mild distress
                rrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.mildDistress), PorterDuff.Mode.MULTIPLY);//yellow
                break;

            case 10:                                  //no distress
                rrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.noDistress), PorterDuff.Mode.MULTIPLY);;//green
                break;

            default:
                rrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.invalid), PorterDuff.Mode.MULTIPLY);;//grey
                break;
        }
    }

    //color for blood oxygen saturation
    private void osColor(int rating, Button osButton) {
        //int rating = osRating(os);
        switch (rating) {
            case 2:                                   //severe distress
                osButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.severeDistress), PorterDuff.Mode.MULTIPLY);//red
                break;

            case 6:                                   //mild distress
                osButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.mildDistress), PorterDuff.Mode.MULTIPLY);//yellow
                break;

            case 10:                                  //no distress
                osButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.noDistress), PorterDuff.Mode.MULTIPLY);//green
                break;

            default:
                osButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.invalid), PorterDuff.Mode.MULTIPLY);//grey
                break;
        }
    }

    //color for heart rate
    private void hrColor(int rating, Button hrButton) {
        //int rating = hrRating(hr);
        switch (rating) {
            case 2:                                   //severe distress
                hrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.severeDistress), PorterDuff.Mode.MULTIPLY);//red
                break;

            case 6:                                   //mild distress
                hrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.mildDistress), PorterDuff.Mode.MULTIPLY);//yellow
                break;

            case 10:                                  //no distress
                hrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.noDistress), PorterDuff.Mode.MULTIPLY);//green
                break;

            default:
                hrButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.invalid), PorterDuff.Mode.MULTIPLY);//grey
                break;
        }
    }

    //color for temperature
    private void tempColor(int rating, Button temperatureButton) {
        //int rating = temperatureRating(temp);
        switch (rating) {
            case 2:                                   //severe distress
                temperatureButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.severeDistress), PorterDuff.Mode.MULTIPLY);//red
                break;

            case 6:                                   //mild distress
                temperatureButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.mildDistress), PorterDuff.Mode.MULTIPLY);//yellow
                break;

            case 10:                                  //no distress
                temperatureButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.noDistress), PorterDuff.Mode.MULTIPLY);//green
                break;

            default:
                temperatureButton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.invalid), PorterDuff.Mode.MULTIPLY);//grey
                break;
        }
    }

    //determine overall respiratory distress level
    private void overallDistress(int os, int rr, int hr, int temperature, TextView overallDistressText, TextView distressValueText) {
        if(os == 0 || rr == 0 || hr == 0 || temperature ==0) {
            overallDistressText.setBackgroundResource(R.color.invalid);
            distressValueText.setBackgroundResource(R.color.invalid);
            distressValueText.setText("N/A");
        } else {
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
            distressValueText.setText(String.format("%.2f\n", overall));
        }
    }

}
