package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.List;

public class PatientProfileMenu extends AppCompatActivity {

    //database handler
    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_menu);

        //database handler
        myDBHandler = DBHandler.getDBHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //buttons
        final Button addProfile = (Button) findViewById(R.id.add_profile_button);
        final Button deleteProfile = (Button) findViewById(R.id.delete_profile_button);

        //creates list view that pulls data from database
        List<String> patientList = myDBHandler.toList();
        ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientList);
        ListView myListView = (ListView) findViewById(R.id.prof_list_view);
        myListView.setAdapter(myAdapter);

        //button click listeners
        addProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent newProfileStart = new Intent(PatientProfileMenu.this, EditProfile.class);

                        newProfileStart.putExtra("patientID", 0);//tells edit profile activity that this is a new profile

                        startActivity(newProfileStart);
                    }
                }
        );

        deleteProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        myDBHandler.destroy();
                        onResume();
                    }
                }
        );

        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent patientProfileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);

                        String patientName = String.valueOf(parent.getItemAtPosition(position));
                        int patientId = myDBHandler.getID(patientName);

                        patientProfileStart.putExtra("patientID", patientId);
                        startActivity(patientProfileStart);
                    }
                }
        );
    }
}
