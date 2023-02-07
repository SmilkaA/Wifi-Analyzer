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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentChannelRateBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;

import java.util.ArrayList;
import java.util.List;

public class ChannelsRateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentChannelRateBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private TextView bestChannels;
    private ListView channelsRatingList;
    private ChannelRateAdapter channelRateAdapter;
    private ArrayList<ScanResult> scanResultList;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChannelRateBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.channelRateRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelRateMainAccessPoint;
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        bestChannels = binding.bestChannels;
        channelsRatingList = binding.channelRatingList;
        scanResultList = new ArrayList<>();
        //for 2.4 only
        channelRateAdapter = new ChannelRateAdapter(getActivity(), scanResultList, Utils.CHANNELS_24GHZ_BAND);
        channelsRatingList.setAdapter(channelRateAdapter);
        bestChannels = binding.bestChannels;
        bestChannels.setText(channelRateAdapter.getBestChannels(channelRateAdapter.getCountedChannels()));

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
        updateChannelsRate();
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        bestChannels.setText(channelRateAdapter.getBestChannels(channelRateAdapter.getCountedChannels()));
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateChannelsRate() {
        List<ScanResult> scanResults = mainActivity.getData();
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        channelRateAdapter.notifyDataSetChanged();
    }
}