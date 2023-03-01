package com.example.myapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.myapplication.utils.SharePreferences;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    public static final String SERVER_KEY = "AAAARN0RU5Q:APA91bH-g3VITmwvfFMtzC7BbUK0xg-SnZDPAsKHOPe1BHdoD6_XOLlERSsdkeFzN32vP0M0oi7lrI56YGCuOIqbY5HBHn-wiK4cqsC7X3nd00XqfjLbaXEoh8NKhClJz3_RMiJCKI_f";
    SharePreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        preferences = new SharePreferences(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(BuildConfig.APPLICATION_ID, "FCM", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        new Handler().post(()-> FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
            Log.e(TAG, "onCreate: "+s );
            preferences.setFCMToken(s);
        }));

    }
}
