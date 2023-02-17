package com.example.wifi.ui.access_points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentAccessPointsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AccessPointsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentAccessPointsBinding binding;
    private ListView wifiListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ScanResult> scanResultList;
    private AccessPointAdapter accessPointAdapter;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private Menu mainMenu;
    private SharedPreferences sharedPreferences;
    private String sortingOption;
    private String listViewDisplay;
    private boolean isUpdating = true;

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

        swipeRefreshLayout = binding.accessPointsRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        scanResultList = new ArrayList<>();

        wifiListView = binding.accessPointsView;
        accessPointAdapter = new AccessPointAdapter(requireActivity(), scanResultList, listViewDisplay);
        wifiListView.setAdapter(accessPointAdapter);
        wifiListView.setOnItemClickListener((arg0, arg1, position, arg3) ->
                new AccessPointPopUp(requireActivity(), accessPointAdapter.getItem(position)).show(getChildFragmentManager(), "ok"));
        accessPointMainView = binding.viewOfCurrentlyConnectedWifi;
        accessPointMainView.setVisibility(View.GONE);
        accessPointMainView.setOnClickListener(view ->
                new AccessPointPopUp(requireActivity(), accessPointAdapter.getItem(0)).show(getChildFragmentManager(), "ok"));
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        updateWiFiList();
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        if (isUpdating) {
            updatePeriodically(true);
        }
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        initFromSharedPreferences();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        updateWiFiList();
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
                mainMenu.getItem(0).setTitle(R.string.wifi_band_2ghz);
                filterByFrequency(Utils.FrequencyBand.TWO_FOUR_GHZ);
                return true;
            case R.id.action_wifi_band_5ghz:
                mainMenu.getItem(0).setTitle(R.string.wifi_band_5ghz);
                filterByFrequency(Utils.FrequencyBand.FIVE_GHZ);
                return true;
            case R.id.action_wifi_band_6ghz:
                mainMenu.getItem(0).setTitle(R.string.wifi_band_6ghz);
                filterByFrequency(Utils.FrequencyBand.SIX_GHZ);
                return true;
            case R.id.action_filter:
                mainActivity.openFilterTab();
                updateWiFiList();
            case R.id.action_scanner:
                if (isUpdating) {
                    updatePeriodically(false);
                    isUpdating = false;
                } else {
                    updatePeriodically(true);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initFromSharedPreferences() {
        sortingOption = sharedPreferences.getString("sort_by", "");
        listViewDisplay = sharedPreferences.getString("access_point_display", "");
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

    private void updateWiFiList() {
        List<ScanResult> scanResults = sortResult(mainActivity.getScanResultsList());
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        accessPointAdapter.notifyDataSetChanged();
    }

    private List<ScanResult> sortResult(List<ScanResult> resultList) {
        switch (sortingOption) {
            case "0":
                resultList = sortByStrength(resultList);
                break;
            case "1":
                resultList = sortBySSID(resultList);
                break;
            case "2":
                resultList = sortByChannel(resultList);
                break;
        }
        return resultList;
    }

    private void filterByFrequency(Utils.FrequencyBand frequencyBand) {
        List<ScanResult> scanResults = sortResult(mainActivity.getScanResultsList());
        scanResultList.clear();
        for (ScanResult result : scanResults) {
            if (Utils.getFrequencyBand(result) == frequencyBand)
                scanResultList.add(result);
        }
        accessPointAdapter.notifyDataSetChanged();
    }

    private List<ScanResult> sortByStrength(List<ScanResult> resultList) {
        Map<Integer, ScanResult> sortedByStrength = new TreeMap<>();
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
}