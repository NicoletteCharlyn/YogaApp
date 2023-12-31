package com.androidproject.yogafitnessapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidproject.yogafitnessapp.Database.YogaAndroidDB;

import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private Button saveButton;

    private RadioButton easyButton, mediumButton, hardButton;

    private RadioGroup radioGroup;

    private YogaAndroidDB yogaAndroidDB;

    private ToggleButton switchAlarm;

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDifficultyMode();

                saveAlarm(switchAlarm.isChecked());

                Toast.makeText(SettingsActivity.this, "Difficulty Level Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        easyButton = (RadioButton) findViewById(R.id.easy_button);
        mediumButton = (RadioButton) findViewById(R.id.medium_button);
        hardButton = (RadioButton) findViewById(R.id.hard_button);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        switchAlarm = (ToggleButton) findViewById(R.id.switch_alarm);

        timePicker = (TimePicker) findViewById(R.id.time_picker);

        yogaAndroidDB = new YogaAndroidDB(this);

        int mode = yogaAndroidDB.getSettingsMode();

        setRadioButton(mode);
    }

    private void saveAlarm(boolean checked) {
        if (checked) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent;

            PendingIntent pendingIntent;

            intent = new Intent(SettingsActivity.this, AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            // Set time for te alarm
            Calendar calendar = Calendar.getInstance();

            Date today = Calendar.getInstance().getTime();

            calendar.set(today.getYear(), today.getMonth(), today.getDay(), timePicker.getHour(), timePicker.getMinute());

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // Cancel alarm
            Intent intent = new Intent(SettingsActivity.this, AlarmNotificationReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    private void saveDifficultyMode() {
        int selectId = radioGroup.getCheckedRadioButtonId();

        if (selectId == easyButton.getId()) {
            yogaAndroidDB.saveSettingsMode(0);
        } else if (selectId == mediumButton.getId()) {
            yogaAndroidDB.saveSettingsMode(1);
        } else if (selectId == hardButton.getId()) {
            yogaAndroidDB.saveSettingsMode(2);
        }
    }

    private void setRadioButton(int mode) {
        if (mode == 0) {
            radioGroup.check(R.id.easy_button);
        } else if (mode == 1) {
            radioGroup.check(R.id.medium_button);
        } else if (mode == 2) {
            radioGroup.check(R.id.hard_button);
        }
    }
}
