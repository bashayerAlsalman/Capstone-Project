package net.bashayer.mygym;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import static net.bashayer.mygym.common.Constants.APPLICATION_MAIN_COLOR;
import static net.bashayer.mygym.common.Constants.SHARED;

public class MyGymApplication extends AppCompatActivity {

    private Boolean isSecondTheme = false;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                isSecondTheme = mFirebaseRemoteConfig.getBoolean(APPLICATION_MAIN_COLOR);
                saveIsSecondTheme();
            }
        });
    }

    private void saveIsSecondTheme() {
        preferences = getSharedPreferences(SHARED, MODE_PRIVATE);
        preferences.edit().putBoolean(APPLICATION_MAIN_COLOR, isSecondTheme).commit();
    }

    private boolean getIsSecondTheme() {
        preferences = getSharedPreferences(SHARED, MODE_PRIVATE);
        return preferences.getBoolean(APPLICATION_MAIN_COLOR, false);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        if (getIsSecondTheme()) {
            theme.applyStyle(R.style.SecondAppTheme, true);
        } else {
            theme.applyStyle(R.style.AppTheme, true);
        }
        // you could also use a switch if you have many themes that could apply
        return theme;
    }
}
