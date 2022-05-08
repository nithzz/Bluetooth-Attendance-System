package com.androidtut.qaifi.bluetoothchatapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class attendanceListFinal extends AppCompatActivity {
    private CheckBox mGoogleCheck,mBingCheck,mYahooCheck;
    private Button mWriteResultButton;
    private TextView mResultTextView;
    private ArrayList<String> mResult;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list_final);
        mGoogleCheck = findViewById(R.id.check_20pc01);
        mBingCheck = findViewById(R.id.check_20pc02);
        mYahooCheck =  findViewById(R.id.check_20pc03);

        mWriteResultButton = findViewById(R.id.write_result);
        mResultTextView = findViewById(R.id.result);
        mResult = new ArrayList<>();
        mResultTextView.setEnabled(false);

        mGoogleCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mGoogleCheck.isChecked())
                    mResult.add("20pc01");
                else
                    mResult.remove("20pc01");
            }
        });

        mBingCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mBingCheck.isChecked())
                    mResult.add("20pc02");
                else
                    mResult.remove("20pc02");
            }
        });

        mYahooCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mYahooCheck.isChecked())
                    mResult.add("20pc03");
                else
                    mResult.remove("20pc03");
            }
        });

        mWriteResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                for(String s : mResult)
                    stringBuilder.append(s).append("\n");

                mResultTextView.setText(stringBuilder.toString());
                mResultTextView.setEnabled(false);
            }
        });
    }
}