package com.example.wifi.ui.channels_rate;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
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
    private Menu mainMenu;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChannelRateBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        swipeRefreshLayout = binding.channelRateRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelRateMainAccessPoint;
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        bestChannels = binding.bestChannels;
        channelsRatingList = binding.channelRatingList;
        scanResultList = new ArrayList<>();
        channelRateAdapter = new ChannelRateAdapter(requireActivity(), scanResultList, Utils.CHANNELS_24GHZ_BAND);
        channelsRatingList.setAdapter(channelRateAdapter);
        bestChannels = binding.bestChannels;
        updateChannelsRate();
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
                channelRateAdapter = new ChannelRateAdapter(requireActivity(), scanResultList, Utils.CHANNELS_24GHZ_BAND);
                channelsRatingList.setAdapter(channelRateAdapter);
                mainMenu.getItem(0).setTitle(R.string.wifi_band_2ghz);
                return true;
            case R.id.action_wifi_band_5ghz:
                channelRateAdapter = new ChannelRateAdapter(requireActivity(), scanResultList, Utils.CHANNELS_5GHZ_BAND);
                channelsRatingList.setAdapter(channelRateAdapter);
                mainMenu.getItem(0).setTitle(R.string.wifi_band_5ghz);
                return true;
            case R.id.action_wifi_band_6ghz:
                channelRateAdapter = new ChannelRateAdapter(requireActivity(), scanResultList, Utils.CHANNELS_6GHZ_BAND);
                channelsRatingList.setAdapter(channelRateAdapter);
                mainMenu.getItem(0).setTitle(R.string.wifi_band_6ghz);
                return true;
            case R.id.action_filter:
                mainActivity.openFilterTab();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateChannelsRate() {
        List<ScanResult> scanResults = mainActivity.getData();
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        channelRateAdapter.notifyDataSetChanged();
    }
}