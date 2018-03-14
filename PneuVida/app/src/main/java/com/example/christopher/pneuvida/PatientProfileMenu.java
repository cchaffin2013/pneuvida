package com.example.christopher.pneuvida;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
        final Button deleteProfiles = (Button) findViewById(R.id.delete_profile_button);

        //creates list view that pulls data from database
        //find better way to fix same name problem
        final List<Integer> patientIDs = myDBHandler.toList();
        List<String> patientNames = new ArrayList<String>();

        for(int i = 0; i < patientIDs.size(); i++) {
            patientNames.add(myDBHandler.getName(patientIDs.get(i)));
        }

        //sets up list view to get patients from database
        final ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patientNames);
        ListView myListView = (ListView) findViewById(R.id.prof_list_view);
        myListView.setAdapter(myAdapter);

        //for delete profiles dialog
        final String[] names = patientNames.toArray(new String[patientNames.size()]);
        final boolean[] checkedProfiles = new boolean[names.length];
        final ArrayList<Integer> toBeDeleted = new ArrayList<Integer>();

        //click listeners
        addProfile.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent newProfileStart = new Intent(PatientProfileMenu.this, EditProfile.class);

                        newProfileStart.putExtra("patientID", 0);//tells edit profile activity that this is a new profile

                        startActivity(newProfileStart);
                    }
                }
        );

        deleteProfiles.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(PatientProfileMenu.this);
                        myBuilder.setTitle("Select profiles to delete");

                        myBuilder.setMultiChoiceItems(names, checkedProfiles, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if(isChecked) {
                                    if(!toBeDeleted.contains(position)){
                                        toBeDeleted.add(position);
                                    } else {
                                        toBeDeleted.remove(position);
                                    }
                                }
                            }
                        });
                        myBuilder.setCancelable(false);

                        //deletes selected patients
                        myBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(PatientProfileMenu.this);
                                View confirmView = getLayoutInflater().inflate(R.layout.confirm, null);

                                //make the dialog box actually show up
                                confirmBuilder.setView(confirmView);
                                final AlertDialog confirmation = confirmBuilder.create();
                                confirmation.show();

                                Button yButton = (Button) confirmView.findViewById(R.id.yes_button);
                                Button nButton = (Button) confirmView.findViewById(R.id.no_button);
                                TextView dialogMessage = (TextView) confirmView.findViewById(R.id.dialog_message);

                                dialogMessage.setText(R.string.are_you_sure);

                                yButton.setOnClickListener( //confirms deletion then closes dialog
                                        new Button.OnClickListener() {
                                            public void onClick(View v) {
                                                int id;

                                                for (int i = 0; i < toBeDeleted.size(); i++) {
                                                    id = patientIDs.get(toBeDeleted.get(i));
                                                    myDBHandler.deletePatient(id);
                                                }
                                                confirmation.dismiss();
                                                onResume();
                                                Toast.makeText(PatientProfileMenu.this, "Selected profiles deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );

                                nButton.setOnClickListener( //closes dialog without deleting
                                        new Button.OnClickListener() {
                                            public void onClick(View v) {
                                                confirmation.dismiss();
                                            }
                                        }
                                );
                            }
                        });

                        myBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog deleteProfileDialog = myBuilder.create();
                        deleteProfileDialog.show();
                    }
                }
        );


        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent patientProfileStart = new Intent(PatientProfileMenu.this, PatientProfile.class);

                        int patientId = patientIDs.get(position);

                        patientProfileStart.putExtra("patientID", patientId);
                        startActivity(patientProfileStart);
                    }
                }
        );
    }
}
