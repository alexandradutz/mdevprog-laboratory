package com.example.dutzi.snowy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dutzi on 06.11.2016.
 */

public class DetailActivity extends Activity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.detail_activity);

        final TextView txtName = (TextView) findViewById(R.id.resortName);
        final TextView txtCountry = (TextView) findViewById(R.id.resortCountry);
        final TextView txtCoord = (TextView) findViewById(R.id.resortCoordinates);
        final TextView txtSlopes = (TextView) findViewById(R.id.resortSlopes);
        final TextView myNotes = (TextView) findViewById(R.id.myNotes);

        Intent i = getIntent();
        // getting attached intent data
        String name = i.getStringExtra("name");
        String coordinates = i.getStringExtra("coordinates");
        String country = i.getStringExtra("country");
        String slopes = i.getStringExtra("slopes");
        // displaying selected resort name
        txtName.setText(name);
        txtCountry.setText(country);
        txtSlopes.setText(slopes);
        txtCoord.setText(coordinates);
        myNotes.setText("");

        Button submit = (Button) findViewById(R.id.resortSubmit);
        Button send = (Button) findViewById(R.id.resortSendMail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resortName = txtName.getText().toString();
                Log.v(TAG, "onClick: " + resortName);
                System.out.println(TAG + "onClick: " + resortName);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailer = new Intent(Intent.ACTION_SEND);
                mailer.setType("plain/text");
                mailer.putExtra(Intent.EXTRA_SUBJECT, txtName.getText().toString());
                mailer.putExtra(Intent.EXTRA_TEXT, txtName.getText().toString() + " in " +
                        txtCountry.getText().toString() + " has a ski area of " +
                        txtSlopes.getText().toString() + ".\n\n It can be found at the following coordinates " +
                        txtCoord.getText().toString() + " \n\n\n\n =======================\n Message sent from your Snowy account");
                startActivity(Intent.createChooser(mailer, ""));
            }
        });

    }
}