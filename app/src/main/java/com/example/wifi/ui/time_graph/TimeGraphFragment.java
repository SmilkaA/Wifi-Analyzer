package com.example.wifi.ui.time_graph;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentTimeGraphBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;
import com.example.wifi.ui.access_points.AccessPointPopUp;
import com.example.wifi.ui.filter.FilterPopUp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class TimeGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentTimeGraphBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointMainView accessPointMainView;
    private MainActivity mainActivity;
    private GraphView graphView;
    private List<ScanResult> scanResultList;
    private int scanCount = 1;
    private List<LineGraphSeries<DataPoint>> series;
    private Menu mainMenu;
    private String maxSignalStrength;
    private String legendDisplay;
    private boolean isUpdating = true;
    private String mainAccessPointViewStatus;
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        initSharePreferences();

        binding = FragmentTimeGraphBinding.inflate(inflater, container, false);
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);

        swipeRefreshLayout = binding.timeGraphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.timeGraphAccessPoint;
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        accessPointMainView.setOnClickListener(view ->
                new AccessPointPopUp(requireActivity(), mainActivity.getData().get(0)).show(getChildFragmentManager(), "ok"));
        mainActivity.showMainAccessPoint(mainAccessPointViewStatus, accessPointMainView);

        scanResultList = mainActivity.getData();

        graphView = binding.timeGraphView;
        initGraphView();
        series = new ArrayList<>();

        fillGraph(scanResultList);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        initSharePreferences();
        if (isUpdating) {
            updatePeriodically(true);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        fillGraph(scanResultList);
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
                filterByFrequency(getString(R.string.wifi_band_2ghz), Utils.FrequencyBand.TWO_FOUR_GHZ);
                return true;
            case R.id.action_wifi_band_5ghz:
                filterByFrequency(getString(R.string.wifi_band_5ghz), Utils.FrequencyBand.FIVE_GHZ);
                return true;
            case R.id.action_wifi_band_6ghz:
                filterByFrequency(getString(R.string.wifi_band_6ghz), Utils.FrequencyBand.SIX_GHZ);
                return true;
            case R.id.action_filter:
                DialogFragment dialogFragment = new FilterPopUp(getContext(), scanResultList);
                dialogFragment.setTargetFragment(this, Utils.FILTER_FRAGMENT);
                if (getFragmentManager() != null) {
                    dialogFragment.show(getFragmentManager().beginTransaction(), getTag());
                }
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

    private void initSharePreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        maxSignalStrength = sharedPreferences.getString(getString(R.string.graph_maximum_key), "");
        legendDisplay = sharedPreferences.getString(getString(R.string.time_graph_legend_key), "");
        mainAccessPointViewStatus = sharedPreferences.getString(getString(R.string.connection_display_key), "");
    }

    public void initGraphView() {
        graphView.animate();
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMinY(-100);
        graphView.getViewport().setMaxY(Integer.parseInt(maxSignalStrength.replace("dBm", "")));
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.signal_strength));
        graphView.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.scan_count));
        graphView.getLegendRenderer().setVisible(true);
        switch (legendDisplay) {
            case "Top":
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                break;
            case "Bottom":
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
                break;
            case "Middle":
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.MIDDLE);
                break;
            case "Hide":
                graphView.getLegendRenderer().setVisible(false);
                break;
        }
    }

    public void fillGraph(List<ScanResult> results) {
        for (ScanResult scanResult : results) {
            try {
                series.get(results.indexOf(scanResult));
            } catch (Exception e) {
                series.add(new LineGraphSeries<>());
            }
            series.get(results.indexOf(scanResult)).appendData(
                    new DataPoint(scanCount, scanResult.level), true, scanCount);
            if (series.get(results.indexOf(scanResult)).getTitle() == null) {
                series.get(results.indexOf(scanResult)).setTitle(scanResult.SSID);
            }

            if (!graphView.getSeries().contains(series.get(results.indexOf(scanResult)))) {
                graphView.addSeries(series.get(results.indexOf(scanResult)));
                series.get(results.indexOf(scanResult)).setColor(Utils.getRandomColor());
            }
            series.get(results.indexOf(scanResult)).setOnDataPointTapListener((series, dataPoint) -> new AccessPointPopUp(requireActivity(), scanResult).show(getChildFragmentManager(), "ok"));
        }
        scanCount++;
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

    private void filterByFrequency(String title, Utils.FrequencyBand frequencyBand) {
        mainMenu.getItem(0).setTitle(title);
        scanResultList.clear();
        for (ScanResult result : mainActivity.getData()) {
            if (Utils.getFrequencyBand(result) == frequencyBand)
                scanResultList.add(result);
        }
        graphView.removeAllSeries();
        fillGraph(scanResultList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Utils.FILTER_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                if(data!=null) {
                    graphView.getLegendRenderer().resetStyles();
                    initGraphView();
                    fillGraph(data.getParcelableArrayListExtra(Utils.INTENT_LIST_KEY));
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                graphView.removeAllSeries();
                initGraphView();
                fillGraph(mainActivity.getData());
            }
        }
    }
}