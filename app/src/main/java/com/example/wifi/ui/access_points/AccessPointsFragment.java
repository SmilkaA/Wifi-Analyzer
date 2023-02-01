package com.example.wifi.ui.access_points;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.databinding.FragmentAccessPointsBinding;

import java.util.ArrayList;
import java.util.List;

public class AccessPointsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentAccessPointsBinding binding;
    private ListView wifiListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ScanResult> scanResultList;
    private AccessPointAdapter accessPointAdapter;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccessPointsBinding.inflate(inflater, container, false);
        wifiListView = binding.accessPointsView;
        swipeRefreshLayout = binding.accessPointsRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        scanResultList = new ArrayList<>();
        accessPointAdapter = new AccessPointAdapter(getActivity(), scanResultList);
        wifiListView.setAdapter(accessPointAdapter);

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
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateWiFiList() {
        List<ScanResult> scanResults = mainActivity.getData();
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        accessPointAdapter.notifyDataSetChanged();
    }
}