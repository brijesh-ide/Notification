package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.model.NotificationBody;
import com.example.myapplication.viewModel.NotificationViewModel;

public class FinalActivity extends AppCompatActivity {

    NotificationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationBody body = new NotificationBody("Final Screen", "Hello Final Activity!");
        viewModel.sendNotification(body);

    }
}