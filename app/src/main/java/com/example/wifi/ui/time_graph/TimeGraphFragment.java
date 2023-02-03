package com.example.wifi.ui.time_graph;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.databinding.FragmentTimeGraphBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;

public class TimeGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentTimeGraphBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimeGraphViewModel timeGraphViewModel =
                new ViewModelProvider(this).get(TimeGraphViewModel.class);

        binding = FragmentTimeGraphBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.timeGraphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.timeGraphAccessPoint;
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

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
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        swipeRefreshLayout.setRefreshing(false);
    }
}