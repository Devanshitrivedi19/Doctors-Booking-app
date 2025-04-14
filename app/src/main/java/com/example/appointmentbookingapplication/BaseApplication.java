package com.example.appointmentbookingapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseApplication extends Application {

    public static final String CHANNEL_ID = "appointment_channel";
    public static final String PREFS_NAME = "AppSettingsPrefs";
    public static final String DARK_MODE_KEY = "dark_mode";
    public static final String NOTIFICATIONS_KEY = "notifications";

    @Override
    public void onCreate() {
        super.onCreate();

        // Set dark mode theme
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Create notification channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Appointment Notifications",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Channel for appointment booking updates");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}