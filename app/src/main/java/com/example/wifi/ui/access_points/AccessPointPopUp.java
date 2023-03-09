package com.example.wifi.ui.access_points;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.wifi.R;
import com.example.wifi.Utils;

public class AccessPointPopUp extends DialogFragment {

    private final ScanResult itemData;
    private final Context context;

    public AccessPointPopUp(Context context, ScanResult item) {
        this.context = context;
        this.itemData = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.access_point_popup_view, container, false);
        initViewComponent(view);
        return view;
    }

    private void initViewComponent(View view) {
        TextView ssidItem = view.findViewById(R.id.ssid_in_detailed);
        ssidItem.setText(new StringBuilder().append(itemData.SSID).append(" (").append(itemData.BSSID).append(")"));

        TextView levelItem = view.findViewById(R.id.level_in_detailed);
        levelItem.setText(context.getString(R.string.level_value, String.valueOf(itemData.level)));
        Utils.setLevelTextColor(itemData.level, levelItem);

        TextView primaryFrequencyItem = view.findViewById(R.id.primaryFrequency_in_detailed);
        primaryFrequencyItem.setText(getString(R.string.wifi_channel_width, String.valueOf(itemData.frequency)));

        ImageView levelImage = view.findViewById(R.id.levelImage_in_detailed);
        levelImage.setImageDrawable(Utils.getWifiIcon(context, itemData));
        levelImage.setVisibility(View.VISIBLE);

        TextView channelWidthItem = view.findViewById(R.id.width_in_detailed);
        channelWidthItem.setText(context.getString(R.string.wifi_channel_width, String.valueOf(Utils.getChannelWidth(itemData))));

        TextView channelItem = view.findViewById(R.id.channel_in_detailed);
        channelItem.setText(Utils.getChannels(itemData));

        TextView frequencyItem = view.findViewById(R.id.wiFiBand);
        frequencyItem.setText((context.getString(R.string.wifi_frequency, Utils.getFrequencyItemText(itemData))));

        TextView frequencyRangeItem = view.findViewById(R.id.channel_frequency_range_in_detailed);
        frequencyRangeItem.setText(Utils.getFrequencyRangeItemText(itemData));

        TextView distanceItem = view.findViewById(R.id.distan_in_detailedce);
        distanceItem.setText(context.getString(R.string.wifi_distance, Utils.getDistanse(itemData)));

        TextView timestampItem = view.findViewById(R.id.timestamp);
        timestampItem.setText(Utils.getTimestamp(itemData));

        TextView is801Item = view.findViewById(R.id.flag80211mc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (itemData.is80211mcResponder()) {
                is801Item.setVisibility(View.VISIBLE);
            } else {
                is801Item.setVisibility(View.GONE);
            }
        }

        TextView wifiStandardsItem = view.findViewById(R.id.wiFiStandard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wifiStandardsItem.setText(itemData.getWifiStandard());
        }

        TextView capabilitiesItem = view.findViewById(R.id.capabilitiesLong);
        capabilitiesItem.setText(itemData.capabilities);

        TextView vendorItem = view.findViewById(R.id.vendorLong);
        vendorItem.setText(Utils.getVendorName(context, itemData));
    }
}
