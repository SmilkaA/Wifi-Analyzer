package com.example.wifi.ui.time_graph;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentTimeGraphBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;
import com.example.wifi.ui.access_points.AccessPointPopUp;
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

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTimeGraphBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.timeGraphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);

        accessPointMainView = binding.timeGraphAccessPoint;
        accessPointMainView.setVisibility(View.GONE);
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        scanResultList = mainActivity.getData();

        graphView = binding.timeGraphView;
        initGraphView();
        series = new ArrayList<>();

        fillGraph();

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
        scanResultList = mainActivity.getData();
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);
        fillGraph();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initGraphView() {
        graphView.animate();
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMinY(-100);
        graphView.getViewport().setMaxY(0);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.signal_strength));
        graphView.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.scan_count));
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public void fillGraph() {
        for (ScanResult scanResult : scanResultList) {
            try {
                series.get(scanResultList.indexOf(scanResult));
            } catch (Exception e) {
                series.add(new LineGraphSeries<>());
            }
            series.get(scanResultList.indexOf(scanResult)).appendData(
                    new DataPoint(scanCount, scanResult.level), true, scanCount);
            if (series.get(scanResultList.indexOf(scanResult)).getTitle() == null) {
                series.get(scanResultList.indexOf(scanResult)).setTitle(scanResult.SSID);
            }

            if (!graphView.getSeries().contains(series.get(scanResultList.indexOf(scanResult)))) {
                graphView.addSeries(series.get(scanResultList.indexOf(scanResult)));
                series.get(scanResultList.indexOf(scanResult)).setColor(Utils.getRandomColor());
            }
            series.get(scanResultList.indexOf(scanResult)).setOnDataPointTapListener((series, dataPoint) -> new AccessPointPopUp(requireActivity(), scanResult).show(getChildFragmentManager(), "ok"));
        }
        scanCount++;
    }
}