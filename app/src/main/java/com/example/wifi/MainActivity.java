package com.example.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wifi.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static WifiManager wifi;
    private BroadcastReceiver wifiScanReceiver;
    private ArrayList<ScanResult> scanResultsList;
    private final static String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    private boolean wlanEnabledByApp;
    private boolean scanTimerIsRunning = false;
    private long lastScanResultReceivedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        initBottomNavigation();
        initDrawerLayout();

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        scanResultsList = new ArrayList<>();
        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                receiveScanResults();
            }
        };
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                receiveScanResults();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        requestScan();
        handlePermissions();
    }

    private void receiveScanResults() {

        List<ScanResult> scanResults = null;
        scanResults = wifi.getScanResults();
        scanResultsList.clear();

        for (ScanResult sr : scanResults) {
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                long age = ((SystemClock.elapsedRealtime() * 1000) - sr.timestamp) / 1000000;
                // if the wlan was last seen more than MAX_SCAN_RESULT_AGE seconds ago, do not add it to the list
                if (age > 35) {
                    continue;
                }
            }
            scanResultsList.add(sr);
        }
        lastScanResultReceivedTime = System.currentTimeMillis();
        requestScan();
    }

    private void requestScan() {
        setWLANEnabled(true);
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        float scanDelay = sharedPrefs.getFloat("PREF_SETTING_SCAN_DELAY", getDefaultScanDelay());
        long delay = (long) Math.max(0, scanDelay - (System.currentTimeMillis() - lastScanResultReceivedTime));
        scanTimerIsRunning = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scanTimerIsRunning = false;
                wifi.startScan();
            }
        }, delay);
    }

    public static int getDefaultScanDelay() {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            // since android 9 each foreground app can scan four times in a 2-minute period
            return 30500;
        } else {
            return 500;
        }
    }

    private void setWLANEnabled(boolean enable) {
        if (enable && wifi.isWifiEnabled() == false) {
            showToast("Enabling WLAN...");
            wifi.setWifiEnabled(true);
            wlanEnabledByApp = true;
        } else if (!enable && wifi.isWifiEnabled() && wlanEnabledByApp) {
            showToast("Disabling WLAN...");
            wifi.setWifiEnabled(false);
            wlanEnabledByApp = false;
        }
    }

    public void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 180);
        toast.show();
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

    public void handlePermissions() {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            return;
        }

        List<String> permissionsToRequest = new ArrayList<String>();

        for (int i = 0; i < permissions.length; i++) {
            String p = permissions[i];
            if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(p);
            }
        }
    }
    public void requestPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 111);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

    public ArrayList<ScanResult> getData() {
        return scanResultsList;
    }

}