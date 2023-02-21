package com.example.wifi.ui.access_points;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentAccessPointsBinding;
import com.example.wifi.ui.filter.FilterPopUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AccessPointsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentAccessPointsBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ScanResult> scanResultList;
    private AccessPointAdapter accessPointAdapter;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private Menu mainMenu;
    private SharedPreferences sharedPreferences;
    private String sortingOption;
    private String listViewDisplay;
    private String mainAccessPointViewStatus;
    private boolean isUpdating = true;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        initFromSharedPreferences();

        binding = FragmentAccessPointsBinding.inflate(inflater, container, false);
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);

        swipeRefreshLayout = binding.accessPointsRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        scanResultList = new ArrayList<>();

        ListView wifiListView = binding.accessPointsView;
        accessPointAdapter = new AccessPointAdapter(requireActivity(), scanResultList, listViewDisplay);
        wifiListView.setAdapter(accessPointAdapter);
        wifiListView.setOnItemClickListener((arg0, arg1, position, arg3) ->
                new AccessPointPopUp(requireActivity(), accessPointAdapter.getItem(position)).show(getChildFragmentManager(), "ok"));
        accessPointMainView = binding.viewOfCurrentlyConnectedWifi;
        accessPointMainView.setVisibility(View.GONE);
        accessPointMainView.setOnClickListener(view ->
                new AccessPointPopUp(requireActivity(), accessPointAdapter.getItem(0)).show(getChildFragmentManager(), "ok"));

        updateWiFiList(mainActivity.getData());
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        mainActivity.showMainAccessPoint(mainAccessPointViewStatus, accessPointMainView);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        initFromSharedPreferences();
        if (isUpdating) {
            updatePeriodically(true);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        updateWiFiList(scanResultList);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        mainMenu = menu;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wifi_band_2ghz:
                filterByFrequency(getString(R.string.wifi_band_2ghz), Utils.FrequencyBand.TWO_FOUR_GHZ);
                return true;
            case R.id.action_wifi_band_5ghz:
                filterByFrequency(getString(R.string.wifi_band_5ghz), Utils.FrequencyBand.FIVE_GHZ);
                return true;
            case R.id.action_wifi_band_6ghz:
                filterByFrequency(getString(R.string.wifi_band_6ghz), Utils.FrequencyBand.SIX_GHZ);
                return true;
            case R.id.action_filter:
                DialogFragment dialogFragment = new FilterPopUp(getContext(), scanResultList);
                dialogFragment.setTargetFragment(this, Utils.FILTER_FRAGMENT);
                if (getFragmentManager() != null) {
                    dialogFragment.show(getFragmentManager().beginTransaction(), getTag());
                }
                return true;
            case R.id.action_scanner:
                if (isUpdating) {
                    item.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    updatePeriodically(false);
                    isUpdating = false;
                } else {
                    item.getIcon().clearColorFilter();
                    updatePeriodically(true);
                    isUpdating = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initFromSharedPreferences() {
        sortingOption = sharedPreferences.getString(getString(R.string.sort_by_key), "");
        listViewDisplay = sharedPreferences.getString(getString(R.string.access_point_display_key), "");
        mainAccessPointViewStatus = sharedPreferences.getString(getString(R.string.connection_display_key), "");
    }

    private void updatePeriodically(boolean onPause) {
        if (onPause) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(this, Long.parseLong(mainActivity.refreshingTimer) * Utils.MILLISECONDS);
                    onRefresh();
                }
            }, Utils.MILLISECONDS);
        }
    }

    private void updateWiFiList(List<ScanResult> resultList) {
        List<ScanResult> scanResults = sortResult(resultList);
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        accessPointAdapter.notifyDataSetChanged();
    }

    private List<ScanResult> sortResult(List<ScanResult> resultList) {
        switch (sortingOption) {
            case "Signal Strength":
                resultList = sortByStrength(resultList);
                break;
            case "SSID":
                resultList = sortBySSID(resultList);
                break;
            case "Channel":
                resultList = sortByChannel(resultList);
                break;
        }
        return resultList;
    }

    private void filterByFrequency(String title, Utils.FrequencyBand frequencyBand) {
        mainMenu.getItem(0).setTitle(title);
        List<ScanResult> scanResults = sortResult(mainActivity.getData());
        scanResultList.clear();
        for (ScanResult result : scanResults) {
            if (Utils.getFrequencyBand(result) == frequencyBand)
                scanResultList.add(result);
        }
        accessPointAdapter.notifyDataSetChanged();
    }

    private List<ScanResult> sortByStrength(List<ScanResult> resultList) {
        Map<Integer, ScanResult> sortedByStrength = new TreeMap<>(Collections.reverseOrder());
        for (ScanResult itemData : resultList) {
            sortedByStrength.put(itemData.level, itemData);
        }
        return new ArrayList<>(sortedByStrength.values());
    }

    private List<ScanResult> sortBySSID(List<ScanResult> resultList) {
        Map<String, ScanResult> sortedBySSID = new TreeMap<>();
        for (ScanResult itemData : resultList) {
            sortedBySSID.put(itemData.SSID, itemData);
        }
        return new ArrayList<>(sortedBySSID.values());
    }

    private List<ScanResult> sortByChannel(List<ScanResult> resultList) {
        Map<Integer, ScanResult> sortedByChannel = new TreeMap<>();
        for (ScanResult itemData : resultList) {
            int[] frequencies = Utils.getFrequencies(itemData);
            if (frequencies.length == 1) {
                sortedByChannel.put(Utils.getChannel(frequencies[0]), itemData);
            }
        }
        return new ArrayList<>(sortedByChannel.values());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Utils.FILTER_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    updateWiFiList(data.getParcelableArrayListExtra("resultList"));
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                updateWiFiList(mainActivity.getData());
                mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
            }
        }
    }
}