package com.example.christopher.pneuvida;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PatientProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        //db hanlder
        //DBHandler myDBHandler = DBHandler.getDBHandler(this);

        //profile menu intent receiver
        Bundle profileData = getIntent().getExtras();
        final String patientName = profileData.getString("patientName");
        final String dob = profileData.getString("dob");
        TextView nameDisplay = (TextView) findViewById(R.id.name_display);
        TextView dobDisplay = (TextView) findViewById(R.id.dob_display);

        nameDisplay.setText(patientName);
        dobDisplay.setText(dob);
        //dobDisplay.setText(myDBHandler.dobToString(patientName));

        //button to allow profile edit
        final ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);

        editButton.setOnClickListener(
                new ImageButton.OnClickListener() {
                    public void onClick(View v) {
                    Intent editStart = new Intent(PatientProfile.this, EditProfile.class); //allow text to be edited which will edit the contents of the database
                    editStart.putExtra("patientName", patientName);//replace with id later
                    editStart.putExtra("dob", dob);
                    startActivity(editStart);
                    }
                }
        );
    }
}
