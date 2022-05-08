package com.example.trojan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private TextView authStatusTv;
    private Button authBtn;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authStatusTv = findViewById(R.id.authStatusTv);
        authBtn = findViewById(R.id.authBtn);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                authStatusTv.setText("Authentication Error"+errString);
                Toast.makeText(MainActivity.this,"Authentication Error"+errString,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authStatusTv.setText("Authentication Success");
                Intent intent = new Intent(MainActivity.this,page2.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Authentication Success",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authStatusTv.setText("Authentication Failed");
                Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using finger print authentication")
                .setNegativeButtonText("User App password")
                .build();

        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

}

