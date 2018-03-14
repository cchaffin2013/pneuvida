package com.example.christopher.pneuvida;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final DBHandler myDBHandler = DBHandler.getDBHandler(Settings.this);

        Button deleteAllButton = (Button) findViewById(R.id.delete_all_profiles_button);
        Switch lightDarkSwitch = (Switch) findViewById(R.id.light_dark_switch);

        //delete all profiles in database
        deleteAllButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //opens alert dialog to confirm deletion of database
                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(Settings.this);
                        View confirmView = getLayoutInflater().inflate(R.layout.confirm, null);

                        //make the dialog box actually show up
                        myBuilder.setView(confirmView);
                        final AlertDialog dialog = myBuilder.create();
                        dialog.show();

                        Button yButton = (Button) confirmView.findViewById(R.id.yes_button);
                        Button nButton = (Button) confirmView.findViewById(R.id.no_button);
                        TextView dialogMessage = (TextView) confirmView.findViewById(R.id.dialog_message);

                        dialogMessage.setText(R.string.are_you_sure);

                        yButton.setOnClickListener( //confirms deletion then closes dialog
                                new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        myDBHandler.destroy();
                                        dialog.dismiss();
                                        Toast.makeText(Settings.this, "All profiles deleted", Toast.LENGTH_SHORT).show();
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
                    }
                }
        );

        //switch from light to dark theme
        lightDarkSwitch.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                    }
                }
        );
    }
}
