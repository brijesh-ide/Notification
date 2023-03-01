package com.example.myapplication.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.utils.SharePreferences;
import com.example.myapplication.model.NotificationBody;
import com.example.myapplication.viewModel.NotificationViewModel;
import com.pesonal.adsdk.AppManage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button hello_world;
    NotificationViewModel viewModel;
    SharePreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        preferences = new SharePreferences(this);

        AppManage.getInstance(MainActivity.this).loadInterstitialAd(this);
        AppManage.getInstance(MainActivity.this).showNative((ViewGroup) findViewById(R.id.native_ads), ADMOB_N[0], FACEBOOK_N[0]);
        AppManage.getInstance(MainActivity.this).showBanner((ViewGroup) findViewById(R.id.banner_ads), ADMOB_N[0], FACEBOOK_N[0]);
        hello_world = findViewById(R.id.hello_world);
        hello_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        Intent intent = new Intent(MainActivity.this, FinalActivity.class);
                        startActivity(intent);
                    }
                }, AppManage.ADMOB, AppManage.app_mainClickCntSwAd);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: "+preferences.getFCMToken() );
        NotificationBody body = new NotificationBody("Main Screen", "Hello Main Activity!");
        viewModel.sendNotification(body);

    }
}