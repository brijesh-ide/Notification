package com.example.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;
import com.example.myapplication.utils.SharePreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    SharePreferences preferences;
    Notification notification;
    int id;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new SharePreferences(getApplicationContext());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        preferences.setFCMToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Random random = new Random();
        id = random.nextInt(10000);

        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String content = message.getNotification().getBody();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = new Notification.Builder(this, BuildConfig.APPLICATION_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOnlyAlertOnce(true)
                        .setAutoCancel(true)
                        .build();
            } else {
                belowOreo(title, content);
            }

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(id, notification);

        }


    }

    private void belowOreo(String title, String content) {
        notification = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .build();
    }
}
