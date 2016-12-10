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
        final TextView month = (TextView) findViewById(R.id.month);
        final TextView visitors = (TextView) findViewById(R.id.visitors);

        Intent i = getIntent();
        // getting attached intent data
        String name = i.getStringExtra(getResources().getString(R.string.id));
        String coordinates = i.getStringExtra(getResources().getString(R.string.coords));
        String country = i.getStringExtra(getResources().getString(R.string.country));
        double slopes = i.getDoubleExtra(getResources().getString(R.string.slopes), 0);
        final int id = i.getIntExtra(getResources().getString(R.string.id), 0);
        txtName.setText(name);
        txtCountry.setText(country);
        txtSlopes.setText(Double.toString(slopes));
        txtCoord.setText(coordinates);
        month.setText(getResources().getString(R.string.emptyString));
        visitors.setText(getResources().getString(R.string.emptyString));

        Button submit = (Button) findViewById(R.id.resortSubmit);
        Button send = (Button) findViewById(R.id.resortSendMail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resortName = txtName.getText().toString();
                Intent retIntent = new Intent(getApplicationContext(), DetailActivity.class);
                retIntent.putExtra(getResources().getString(R.string.name), txtName.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.coords), txtCoord.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.country), txtCountry.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.slopes), txtSlopes.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.id), id);
                setResult(Activity.RESULT_OK, retIntent);
                finish();
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