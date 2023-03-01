package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {

    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private NotificationBody notificationBody;

    public RequestNotification(String token, NotificationBody notificationBody) {
        this.token = token;
        this.notificationBody = notificationBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NotificationBody getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(NotificationBody notificationBody) {
        this.notificationBody = notificationBody;
    }

}
