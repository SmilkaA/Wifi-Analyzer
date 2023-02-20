package com.example.wifi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;

import com.example.wifi.ui.access_points.AccessPointMainView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.wifi.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private WifiManager wifi;
    private BroadcastReceiver wifiScanReceiver;
    private List<ScanResult> scanResultsList;
    private SharedPreferences sharedPreferences;
    private String theme;
    public String refreshingTimer;
    private final static String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        initBottomNavigation();
        initDrawerLayout();
        checkSettings();
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_access_points, R.id.navigation_channels_rate,
                R.id.navigation_channel_graph, R.id.navigation_time_graph)
                .build();

        NavController navController1 = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController1, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController1);
    }

    private void initDrawerLayout() {
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView drawerNavigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_access_points, R.id.nav_channels_rate, R.id.nav_channels_graph,
                R.id.nav_time_graph, R.id.nav_export, R.id.nav_available_channels,
                R.id.nav_vendors, R.id.nav_port_authority, R.id.nav_settings, R.id.nav_about_app)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(drawerNavigationView, navController);
    }

    public void setTheme() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getString(getString(R.string.theme_key), "");
        refreshingTimer = sharedPreferences.getString(getString(R.string.scan_interval_key), "5");
        switch (theme) {
            case "Dark":
                setTheme(android.R.style.Theme_Material_NoActionBar);
                break;
            case "Light":
                setTheme(android.R.style.Theme_Material_Light_NoActionBar);
                break;
            case "System":
                setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        setTheme();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

    private void requestScan() {
        long delay = (long) Math.max(0, Long.parseLong(refreshingTimer) * 1000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                wifi.startScan();
            }
        }, delay);
    }

    public void checkSettings() {
        if (!wifi.isWifiEnabled()) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!lm.isLocationEnabled()) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }
    }

    public void handlePermissions() {
        if (android.os.Build.VERSION.SDK_INT < 25) {
            return;
        }

        List<String> permissionsToRequest = new ArrayList<>();
        for (String p : permissions) {
            if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(p);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()])
                    , 111);
        }
    }

    public List<ScanResult> getData() {
        wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                List<ScanResult> scanResults = wifi.getScanResults();
                scanResultsList.addAll(scanResults);
                requestScan();
            }
        };
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        requestScan();
        handlePermissions();
        scanResultsList = wifi.getScanResults();
        return scanResultsList;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void fillCurrentlyConnectedAccessPoint(AccessPointMainView accessPointMainView) {
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        List<ScanResult> scanResults = scanResultsList;
        for (ScanResult result : scanResults) {
            if (info.getSSID().replace("\"", "").equals(result.SSID)) {
                accessPointMainView.setSsidView(result.SSID + " (" + result.BSSID + ")");
                accessPointMainView.setLevelView(String.valueOf(result.level));
                String channel = "";
                int[] frequencies = Utils.getFrequencies(result);
                if (frequencies.length == 1) {
                    channel = String.valueOf(Utils.getChannel(frequencies[0]));
                } else if (frequencies.length == 2) {
                    channel = Utils.getChannel(frequencies[0]) + "+" + Utils.getChannel(frequencies[1]);
                }
                accessPointMainView.setChannelView(channel);
                accessPointMainView.setPrimaryFrequencyView(Utils.getChannelWidth(result) + " MHz");
                Drawable picture;
                if (result.level > -35) {
                    picture = getResources().getDrawable(getResources().getIdentifier("@drawable/ic_signal_wifi_4_bar", null, getPackageName()));
                } else if (result.level > -55) {
                    picture = getResources().getDrawable(getResources().getIdentifier("@drawable/ic_signal_wifi_3_bar", null, getPackageName()));
                } else if (result.level > -80) {
                    picture = getResources().getDrawable(getResources().getIdentifier("@drawable/ic_signal_wifi_2_bar", null, getPackageName()));
                } else if (result.level > -90) {
                    picture = getResources().getDrawable(getResources().getIdentifier("@drawable/ic_signal_wifi_1_bar", null, getPackageName()));
                } else {
                    picture = getResources().getDrawable(getResources().getIdentifier("@drawable/ic_signal_wifi_0_bar", null, getPackageName()));
                }
                accessPointMainView.setLevelImageView(picture);
                Utils.FrequencyBand fBand = Utils.getFrequencyBand(result);
                String frequencyItemText = "";
                if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ) {
                    frequencyItemText = "2.4";
                } else if (fBand == Utils.FrequencyBand.FIVE_GHZ) {
                    frequencyItemText = "5";
                } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
                    frequencyItemText = "6";
                }
                double exp = (27.55 - (20 * Math.log10(Double.parseDouble(frequencyItemText))) + Math.abs(result.level)) / 20.0;
                double distanceM = Math.pow(10.0, exp);
                accessPointMainView.setDistanceView("~" + String.valueOf(distanceM / 1000).substring(0, 5) + "m");
                accessPointMainView.setSpeedView(info.getLinkSpeed() + "Mbps");
                accessPointMainView.setIPView(Formatter.formatIpAddress(info.getIpAddress()));
                accessPointMainView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    public void showMainAccessPoint(String status,AccessPointMainView accessPointMainView) {
        if (status.equals("Complete")) {
            accessPointMainView.getLevelImageInMain().setVisibility(View.VISIBLE);
            accessPointMainView.setVisibility(View.VISIBLE);
        } else if (status.equals("Compact")) {
            accessPointMainView.getLevelImageInMain().setVisibility(View.GONE);
            accessPointMainView.setVisibility(View.VISIBLE);
        } else {
            accessPointMainView.setVisibility(View.GONE);
        }
    }
}