package com.kamus.highriskpregnancy_patient;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    MySQLiteHelper dbhelper;
    dbActivities db;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming FCM messages here
        // You can display notifications or perform other actions
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
           /* Intent intent = new Intent(this, NotificationActionActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_patient)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(false) // Automatically dismiss the notification when clicked
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            int notificationId = 1; // Use a unique notification ID.
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(notificationId, builder.build());

            // Customize the notification based on the FCM message content

        }
    }





    @Override
    public void onNewToken(String token) {
        // This method is called when the FCM token is refreshed
        // You can send the new token to your server here
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        db = new dbActivities(); // Initialize dbActivities
        dbhelper = new MySQLiteHelper(this);
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


}