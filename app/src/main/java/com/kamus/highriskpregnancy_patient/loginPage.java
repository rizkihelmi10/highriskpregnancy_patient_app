package com.kamus.highriskpregnancy_patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class loginPage extends AppCompatActivity {
    TextView login;
    TextView Nama;
    EditText usrname;
    MySQLiteHelper dbhelper;
    dbActivities db;
    Button buttonlogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        login = findViewById(R.id.textlogin);
        Nama = findViewById(R.id.textNama);
        usrname = findViewById(R.id.editTextNama);
        db = new dbActivities(); // Initialize dbActivities
        dbhelper = new MySQLiteHelper(this);
        buttonlogin = findViewById(R.id.btnlogin);
        askNotificationPermission();

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usrname.getText().toString();
                Log.d("validate login", "onClick:  check db" + username);
                if (username.isEmpty()) {
                    Toast.makeText(loginPage.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbhelper.validateLogin(username)) {
                        Toast.makeText(loginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(loginPage.this, MainActivity.class);
                        startActivity(intent);

                        // Redirect to another activity or perform other actions upon successful login
                    } else {
                        Toast.makeText(loginPage.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });


}