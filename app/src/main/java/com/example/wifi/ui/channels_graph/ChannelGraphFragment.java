package com.example.wifi.ui.channels_graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentChannelGraphBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;
import com.example.wifi.ui.access_points.AccessPointPopUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChannelGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentChannelGraphBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private LevelDiagram24GHz levelDiagram24;
    private LevelDiagram5GHz levelDiagram5;
    private LevelDiagram6GHz levelDiagram6;
    private Menu mainMenu;
    private boolean isUpdating =true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChannelGraphBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        swipeRefreshLayout = binding.channelGraphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.channelGraphAccessPoint;
        accessPointMainView.setOnClickListener(view ->
                new AccessPointPopUp(requireActivity(), mainActivity.getData().get(0)).show(getChildFragmentManager(), "ok"));
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        levelDiagram24 = binding.levelDiagram24GHz;
        levelDiagram5 = binding.levelDiagram5GHz;
        levelDiagram6 = binding.levelDiagram6GHz;
        levelDiagram24.updateDiagram(mainActivity.getData());

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
                levelDiagram5.setVisibility(View.GONE);
                levelDiagram6.setVisibility(View.GONE);
                levelDiagram24.updateDiagram(mainActivity.getData());
                levelDiagram24.setVisibility(View.VISIBLE);
                mainMenu.getItem(0).setTitle(R.string.wifi_band_2ghz);
                return true;
            case R.id.action_wifi_band_5ghz:
                levelDiagram24.setVisibility(View.GONE);
                levelDiagram6.setVisibility(View.GONE);
                levelDiagram5.updateDiagram(mainActivity.getData());
                levelDiagram5.setVisibility(View.VISIBLE);
                mainMenu.getItem(0).setTitle(R.string.wifi_band_5ghz);
                return true;
            case R.id.action_wifi_band_6ghz:
                levelDiagram24.setVisibility(View.GONE);
                levelDiagram5.setVisibility(View.GONE);
                levelDiagram6.updateDiagram(mainActivity.getData());
                levelDiagram6.setVisibility(View.VISIBLE);
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
}