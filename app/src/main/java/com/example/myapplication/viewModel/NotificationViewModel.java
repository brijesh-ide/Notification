package com.example.myapplication.viewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.myapplication.utils.SharePreferences;
import com.example.myapplication.model.NotificationBody;
import com.example.myapplication.model.RequestNotification;
import com.example.myapplication.service.RetroClient;
import com.example.myapplication.service.RetroModule;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends AndroidViewModel {

    private static final String TAG = "NotificationViewModel";
    RetroModule retroModule;
    SharePreferences preferences;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        preferences = new SharePreferences(application.getApplicationContext());
        retroModule = RetroClient.getRetroClient().create(RetroModule.class);
    }

    public void sendNotification(NotificationBody body){
        RequestNotification notification = new RequestNotification(preferences.getFCMToken(), body);

        retroModule.sendNotification(notification).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful())
                    Toast.makeText(getApplication(), "Notification sent.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

}
