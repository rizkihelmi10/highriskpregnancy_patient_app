package com.kamus.highriskpregnancy_patient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    MySQLiteHelper dbhelper;
    dbActivities db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbActivities(); // Initialize dbActivities
        dbhelper = new MySQLiteHelper(this);
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        setContentView(R.layout.activity_main);
        checkConfigDB();
        savetoken();

    }
    public void savetoken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FCM", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tokens");
                        String userId = db.getResult(dbhelper,"name",null); // Assuming you are using Firebase Authentication
                        String Uid = String.valueOf(userId);
                        Log.d("tyeso", "sendRegistrationToServer: " + Uid);
                        Log.d("tyeso", "sendRegistrationToServer: " + token);
                        // Store the token in the database under the user's node
                        databaseReference.child(Uid).setValue(token);
                        Log.d("tyeso", "sendRegistrationToServer: " + Uid);
                        Log.d("tyeso", "sendRegistrationToServer: " + token);
                    }
                });
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

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

    public void checkConfigDB() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

        int iCount = db.countConfigDB(dbhelper);
        if (isFirstRun) {
            if (iCount == 0) {
                callclassExecute("Config", "yes");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstRun", false);
                editor.apply();
            } else {
                Intent intent = new Intent(MainActivity.this, loginPage.class);
                startActivity(intent);
            }

        }
    }

    public void callclassExecute(String clss, String sfromMain) {
        Log.i("callclassExecute", "execute");


        if (clss.equals("Config")) {
            Intent intent = new Intent(this, signUpAct.class);
            intent.putExtra("sfromMain", sfromMain);
            startActivity(intent);

            if (sfromMain.contains("yes"))
                finish();
        }

    }
}