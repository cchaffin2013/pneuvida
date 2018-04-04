package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    //db handler
    DBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //edit text fields
        final EditText nameDisplay = (EditText) findViewById(R.id.name_edit_text);
        final EditText dobDisplay = (EditText) findViewById(R.id.dob_edit_text);
        final EditText sexDisplay = (EditText) findViewById(R.id.sex_edit_text);
        final EditText heightDisplay = (EditText) findViewById(R.id.height_edit_text);
        final EditText weightDisplay = (EditText) findViewById(R.id.weight_edit_text);
        final EditText medsDisplay = (EditText) findViewById(R.id.meds_edit_text);
        final EditText allergiesDisplay = (EditText) findViewById(R.id.allergies_edit_text);
        final EditText notesDisplay = (EditText) findViewById(R.id.note_edit_text);

        //db handler
        myDBHandler = DBHandler.getDBHandler(this);

        //for if there is a new patient
        final Patient newPatient = new Patient("New Patient");

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final int patientID = profileData.getInt("patientID");

        //id of 0 means new patient not in db
        if(patientID == 0) { //if new patient, add new patient to db
            myDBHandler.addPatient(newPatient);
        } else { //if not, get edit text field values from database
            nameDisplay.setText(myDBHandler.getName(patientID));
            dobDisplay.setText(myDBHandler.getDOB(patientID));
            sexDisplay.setText(myDBHandler.getSex(patientID));
            heightDisplay.setText(myDBHandler.getHeight(patientID));
            weightDisplay.setText(myDBHandler.getWeight(patientID));
            medsDisplay.setText(myDBHandler.getMeds(patientID));
            allergiesDisplay.setText(myDBHandler.getAllergies(patientID));
            notesDisplay.setText(myDBHandler.getNotes(patientID));
        }

        //buttons
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        //button click listeners
        saveButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        int id;
                        if(patientID == 0) { //if new patient, get their id from
                                            // autoincremented id in database
                            id = myDBHandler.getID("New Patient");
                        } else { //if not just use the id sent from the intent
                            id = patientID;
                        }
                        //save data to the patient associated with id
                            myDBHandler.setName(nameDisplay.getText().toString(), id);
                            myDBHandler.setDob(dobDisplay.getText().toString(), id);
                            myDBHandler.setSex(sexDisplay.getText().toString(), id);
                            myDBHandler.setHeight(heightDisplay.getText().toString(), id);
                            myDBHandler.setWeight(weightDisplay.getText().toString(), id);
                            myDBHandler.setMeds(medsDisplay.getText().toString(), id);
                            myDBHandler.setAllergies(allergiesDisplay.getText().toString(), id);
                            myDBHandler.setNotes(notesDisplay.getText().toString(), id);
                            myDBHandler.close();
                        Toast.makeText(EditProfile.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                        finish();//close activity after saving
                    }
                }
        );

        cancelButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        //opens alert dialog to confirm deletion of database
                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(EditProfile.this);
                        View confirmView = getLayoutInflater().inflate(R.layout.confirm, null);

                        //make the dialog box actually show up
                        myBuilder.setView(confirmView);
                        final AlertDialog dialog = myBuilder.create();
                        dialog.show();

                        Button yButton = (Button) confirmView.findViewById(R.id.yes_button);
                        Button nButton = (Button) confirmView.findViewById(R.id.no_button);
                        TextView dialogMessage = (TextView) confirmView.findViewById(R.id.dialog_message);

                        dialogMessage.setText(R.string.cancel_without_saving);

                        yButton.setOnClickListener( //confirms deletion then closes dialog
                                new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        //if new patient, delete new patient from the database
                                        myDBHandler.deletePatient("New Patient");

                                        myDBHandler.close();
                                        dialog.dismiss();
                                        finish();//close activity without saving changes
                                    }
                                }
                        );

                        nButton.setOnClickListener( //closes dialog without deleting
                                new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        dialog.hide();
                                    }
                                }
                        );
                    }
                }
        );

    }
}
