package com.sample.datetimepickeriossample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fadlifebr.datetimepickerios.SlideDateTimeListener;
import com.fadlifebr.datetimepickerios.SlideDateTimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date currentTime = Calendar.getInstance().getTime();
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .setMinDate(currentTime)
                .setHideTimeView(false) /*you can hide date by setting it to true*/
                .build()
                .show();
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            String stringDate = DateFormat.getDateTimeInstance().format(date);
            Toast.makeText(MainActivity.this, "Result : " + stringDate, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDateTimeCancel() {

        }
    };
}
