package com.example.wifi.ui.channels_rate;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentChannelRateBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private boolean isUpdating = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = FragmentChannelRateBinding.inflate(inflater, container, false);
        swipeRefreshLayout = binding.channelRateRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelRateMainAccessPoint;
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        bestChannels = binding.bestChannels;
        channelsRatingList = binding.channelRatingList;
        scanResultList = new ArrayList<>();
        channelRateAdapter = new ChannelRateAdapter(requireActivity(), scanResultList, Utils.CHANNELS_24GHZ_BAND);
        bestChannels = binding.bestChannels;
        bestChannels.setText(channelRateAdapter.getBestChannels(channelRateAdapter.getCountedChannels()));
        channelsRatingList.setAdapter(channelRateAdapter);

        updateChannelsRate();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        if (isUpdating) {
            updatePeriodically(true);
        }
        super.onResume();
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
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
                return true;
            case R.id.action_scanner:
                if (isUpdating) {
                    mainMenu.getItem(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    updatePeriodically(false);
                    isUpdating = false;
                } else {
                    mainMenu.getItem(2).getIcon().clearColorFilter();
                    updatePeriodically(true);
                    isUpdating = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void updateChannelsRate() {
        List<ScanResult> scanResults = mainActivity.getData();
        scanResultList.clear();
        scanResultList.addAll(scanResults);
        channelRateAdapter.notifyDataSetChanged();
    }
}