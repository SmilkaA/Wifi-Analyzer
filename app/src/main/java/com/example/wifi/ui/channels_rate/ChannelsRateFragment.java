package com.example.wifi.ui.channels_rate;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.databinding.FragmentChannelRateBinding;
import com.example.wifi.ui.access_points.AccessPointAdapter;
import com.example.wifi.ui.access_points.AccessPointMainView;

import java.util.ArrayList;

public class ChannelsRateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentChannelRateBinding binding;
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
        ChannelsRateViewModel channelsRateViewModel =
                new ViewModelProvider(this).get(ChannelsRateViewModel.class);

        binding = FragmentChannelRateBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.channelRateRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelRateMainAccessPoint;
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