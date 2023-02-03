package com.example.wifi.ui.channels_graph;

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
import com.example.wifi.databinding.FragmentChannelGraphBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;

public class ChannelGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private FragmentChannelGraphBinding binding;
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
        ChannelGraphViewModel channelGraphViewModel =
                new ViewModelProvider(this).get(ChannelGraphViewModel.class);

        binding = FragmentChannelGraphBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.channelGraphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelGraphAccessPoint;
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