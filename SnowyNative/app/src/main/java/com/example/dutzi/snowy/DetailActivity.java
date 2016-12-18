package com.example.dutzi.snowy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by dutzi on 06.11.2016.
 */

public class DetailActivity extends Activity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICKER = 0;

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
        final TextView visitedOn = (TextView) findViewById(R.id.visitedOnDate);

        Intent i = getIntent();
        // getting attached intent data
        String name = i.getStringExtra(getResources().getString(R.string.name));
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
        visitedOn.setText(R.string.emptyString);

        Button submit = (Button) findViewById(R.id.resortSubmit);
        FloatingActionButton send = (FloatingActionButton) findViewById(R.id.fab);
        Button visitorsSubmit = (Button) findViewById(R.id.visitorsSubmit);
        Button setDateBtn = (Button) findViewById(R.id.setDate);

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
                retIntent.putExtra(getResources().getString(R.string.requestCode), 0);
                setResult(Activity.RESULT_OK, retIntent);
                finish();
            }
        });


        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickerIntent = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivityForResult(pickerIntent, REQUEST_CODE_PICKER);
            }
        });

        visitorsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = new Intent(getApplicationContext(), DetailActivity.class);
                retIntent.putExtra(getResources().getString(R.string.visitors), visitors.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.month), month.getText().toString());
                retIntent.putExtra(getResources().getString(R.string.id), id);
                retIntent.putExtra(getResources().getString(R.string.requestCode), 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_PICKER)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);
                int year = data.getIntExtra("year",0);
                TextView visitedOn = (TextView) findViewById(R.id.visitedOnDate);
                visitedOn.setText(day + " " + month + " " + year);
            }
        }
    }
}