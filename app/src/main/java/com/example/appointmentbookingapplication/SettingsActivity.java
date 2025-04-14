package com.example.appointmentbookingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationSwitch, darkModeSwitch;
    private ImageView notificationIcon, darkModeIcon;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply theme before layout inflation
        sharedPreferences = getSharedPreferences(BaseApplication.PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(BaseApplication.DARK_MODE_KEY, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_settings);

        notificationSwitch = findViewById(R.id.switchNotification);
        darkModeSwitch = findViewById(R.id.switchDarkMode);
        notificationIcon = findViewById(R.id.notificationIcon);
        darkModeIcon = findViewById(R.id.darkModeIcon);

        // Load preferences
        boolean notificationsEnabled = sharedPreferences.getBoolean(BaseApplication.NOTIFICATIONS_KEY, true);
        notificationSwitch.setChecked(notificationsEnabled);
        darkModeSwitch.setChecked(isDarkMode);
        updateIcons(notificationsEnabled, isDarkMode);

        // Notification Switch Listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(BaseApplication.NOTIFICATIONS_KEY, isChecked).apply();
            updateIcons(isChecked, darkModeSwitch.isChecked());

            if (isChecked && !NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Toast.makeText(this, "Enable notifications in system settings", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        });

        // Dark Mode Switch Listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(BaseApplication.DARK_MODE_KEY, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate(); // Apply new theme immediately
        });
    }

    private void updateIcons(boolean notificationsOn, boolean darkModeOn) {
        notificationIcon.setImageResource(notificationsOn ?
                R.drawable.ic_notifications_on : R.drawable.ic_notifications_off);
        darkModeIcon.setImageResource(darkModeOn ?
                R.drawable.ic_dark_mode : R.drawable.ic_light_mode);
    }
}