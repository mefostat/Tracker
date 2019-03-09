package com.example.kupin.maintrack;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    SharedPreferences sPref;
    String Accuracy;
    String minTime,minDistance;
    Boolean ServiceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
      LoadSettings();
    }

    void OnClick(View view) {
        EditText mTime = (EditText) findViewById(R.id.minTime);
        EditText mDistance = (EditText) findViewById(R.id.minDistance);
        EditText accuracy = (EditText) findViewById(R.id.Accuracy);
        Switch CaseService = (Switch)findViewById(R.id.switch1);
        minDistance = mDistance.getText().toString();
        minTime = mTime.getText().toString();
        Accuracy = accuracy.getText().toString();
        ServiceStatus = CaseService.isChecked();
        SaveSettings(minTime, minDistance, Accuracy,ServiceStatus);

    }

    void SaveSettings(String mTime,String mDistance, String accuracy, Boolean serviceStatus) {
        sPref = getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("minTime",mTime);
        ed.putString("minDistance",mDistance);
        ed.putString("Accuracy",accuracy);
        ed.putBoolean("ServiceStatus",serviceStatus);
        ed.commit();
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
    }

  void LoadSettings() {
        sPref = getSharedPreferences("settings",MODE_PRIVATE);
        EditText mTime = (EditText) findViewById(R.id.minTime);
      Switch CaseService = (Switch)findViewById(R.id.switch1);
     EditText mDistance = (EditText) findViewById(R.id.minDistance);
       EditText accuracy = (EditText) findViewById(R.id.Accuracy);

     mTime.setText(sPref.getString("minTime",""));

      mDistance.setText(sPref.getString("minDistance",""));
        accuracy.setText(sPref.getString("Accuracy",""));
        if(sPref.getBoolean("ServiceStatus",false) != true) {
            CaseService.setChecked(false);
        }
        else {
            CaseService.setChecked(true);
        }

    }
}
