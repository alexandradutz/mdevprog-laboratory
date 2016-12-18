package com.example.dutzi.snowy;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerActivity extends AppCompatActivity {

    private DatePicker datePickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        datePickerView = (DatePicker) findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePickerView.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int day = datePickerView.getDayOfMonth();
                        int month = datePickerView.getMonth();
                        int myyear = datePickerView.getYear();
                        Intent retIntent = new Intent(getApplicationContext(), DatePickerActivity.class);
                        retIntent.putExtra("day", day);
                        retIntent.putExtra("year", myyear);
                        retIntent.putExtra("month", month);
                        setResult(Activity.RESULT_OK, retIntent);
                        finish();

                    }
                });
    }
}
