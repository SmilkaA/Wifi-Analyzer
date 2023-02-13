package com.example.wifi.ui.access_points;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.databinding.FragmentAccessPointsBinding;

import java.util.ArrayList;
import java.util.List;

public class AccessPointsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentAccessPointsBinding binding;
    private ListView wifiListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ScanResult> scanResultList;
    private AccessPointAdapter accessPointAdapter;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private Menu mainMenu;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        binding = FragmentAccessPointsBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.accessPointsRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        scanResultList = new ArrayList<>();

        wifiListView = binding.accessPointsView;
        accessPointAdapter = new AccessPointAdapter(requireActivity(), scanResultList);
        wifiListView.setAdapter(accessPointAdapter);
        wifiListView.setOnItemClickListener((arg0, arg1, position, arg3) ->
                new AccessPointPopUp(requireActivity(),accessPointAdapter.getItem(position)).show(getChildFragmentManager(), "ok"));
        accessPointMainView = binding.viewOfCurrentlyConnectedWifi;
        accessPointMainView.setVisibility(View.GONE);
        accessPointMainView.setOnClickListener(view ->
                new AccessPointPopUp(requireActivity(),accessPointAdapter.getItem(0)).show(getChildFragmentManager(), "ok"));
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        updateWiFiList();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                return true;
            case R.id.action_wifi_band_5ghz:
                mainMenu.getItem(0).setTitle(R.string.wifi_band_5ghz);
                return true;
            case R.id.action_wifi_band_6ghz:
                mainMenu.getItem(0).setTitle(R.string.wifi_band_6ghz);
                return true;
            case R.id.action_filter:
                mainActivity.openFilterTab();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateWiFiList() {
        List<ScanResult> scanResults = mainActivity.getData();
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        accessPointAdapter.notifyDataSetChanged();
    }
}