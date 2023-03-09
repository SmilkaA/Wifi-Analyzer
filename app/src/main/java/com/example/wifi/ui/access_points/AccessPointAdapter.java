package com.example.wifi.ui.access_points;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.R;
import com.example.wifi.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccessPointAdapter extends BaseAdapter {
    private final Context context;
    private final List<ScanResult> data;
    private static LayoutInflater inflater = null;
    private final String listViewDisplay;

    public AccessPointAdapter(Context context, List<ScanResult> data, String listViewDisplay) {
        this.context = context;
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
        this.listViewDisplay = listViewDisplay;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ScanResult getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.access_point_detailed_view, viewGroup,false);
        }
        initViewComponent(data.get(i), view);
        return view;
    }

    private void initViewComponent(ScanResult itemData, View view) {
        TextView ssidItem = view.findViewById(R.id.ssid_in_detailed);
        ssidItem.setText(new StringBuilder().append(itemData.SSID).append(" (").append(itemData.BSSID).append(")"));

        TextView levelItem = view.findViewById(R.id.level_in_detailed);
        levelItem.setText(context.getString(R.string.level_value, String.valueOf(itemData.level)));
        Utils.setLevelTextColor(itemData.level, levelItem);

        ImageView levelImage = view.findViewById(R.id.levelImage_in_detailed);
        levelImage.setImageDrawable(Utils.getWifiIcon(context, itemData));
        if (listViewDisplay.equals("Complete")) {
            levelImage.setVisibility(View.VISIBLE);
        }

        TextView channelWidthItem = view.findViewById(R.id.width_in_detailed);
        channelWidthItem.setText(context.getString(R.string.wifi_channel_width, String.valueOf(Utils.getChannelWidth(itemData))));

        TextView channelItem = view.findViewById(R.id.channel_in_detailed);
        channelItem.setText(Utils.getChannels(itemData));

        TextView vendorItem = view.findViewById(R.id.vendorShort_in_detailed);
        vendorItem.setText(Utils.getVendorName(context, itemData));

        TextView capabilitiesItem = view.findViewById(R.id.capabilities_in_detailed);
        capabilitiesItem.setText(itemData.capabilities);

        TextView frequencyItem = view.findViewById(R.id.primaryFrequency_in_detailed);
        frequencyItem.setText((context.getString(R.string.wifi_frequency, Utils.getFrequencyItemText(itemData))));

        TextView frequencyRangeItem = view.findViewById(R.id.channel_frequency_range_in_detailed);
        frequencyRangeItem.setText(Utils.getFrequencyRangeItemText(itemData));

        TextView distanceItem = view.findViewById(R.id.distan_in_detailedce);
        distanceItem.setText(context.getString(R.string.wifi_distance, Utils.getDistanse(itemData)));
    }
}
