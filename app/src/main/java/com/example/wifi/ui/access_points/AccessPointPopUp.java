package com.example.wifi.ui.access_points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.example.wifi.ui.vendors.VendorModel;
import com.example.wifi.ui.vendors.VendorsAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AccessPointPopUp extends DialogFragment {

    private ScanResult itemData;
    private Context context;

    public AccessPointPopUp(Context context, ScanResult item) {
        this.context = context;
        this.itemData = item;
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.access_point_popup_view, container, false);

        TextView ssidItem = view.findViewById(R.id.ssid_in_detailed);
        ssidItem.setText(itemData.SSID + " (" + itemData.BSSID + ")");

        TextView levelItem = view.findViewById(R.id.level_in_detailed);
        levelItem.setText(context.getString(R.string.level_value, String.valueOf(itemData.level)));

        TextView primaryFrequencyItem = view.findViewById(R.id.primaryFrequency_in_detailed);
        primaryFrequencyItem.setText(itemData.frequency + "MHz");

        ImageView levelImage = view.findViewById(R.id.levelImage_in_detailed);
        Drawable picture;
        if (itemData.level > -35) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_4_bar", null, context.getPackageName()));
            picture.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            levelItem.setTextColor(Color.GREEN);
        } else if (itemData.level > -55) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_3_bar", null, context.getPackageName()));
            picture.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            levelItem.setTextColor(Color.YELLOW);
        } else if (itemData.level > -80) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_2_bar", null, context.getPackageName()));
            picture.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            levelItem.setTextColor(Color.YELLOW);
        } else if (itemData.level > -90) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_1_bar", null, context.getPackageName()));
            picture.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            levelItem.setTextColor(Color.RED);
        } else {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_0_bar", null, context.getPackageName()));
            picture.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            levelItem.setTextColor(Color.RED);
        }
        levelImage.setImageDrawable(picture);

        TextView channelWidthItem = view.findViewById(R.id.width_in_detailed);
        channelWidthItem.setText(Utils.getChannelWidth(itemData) + " MHz");

        TextView channelItem = view.findViewById(R.id.channel_in_detailed);
        String channel = "";
        int[] frequencies = Utils.getFrequencies(itemData);
        if (frequencies.length == 1) {
            channel = String.valueOf(Utils.getChannel(frequencies[0]));
        } else if (frequencies.length == 2) {
            channel = Utils.getChannel(frequencies[0]) + "(" + Utils.getChannel(frequencies[1]) + ")";
        }
        channelItem.setText(channel);

        TextView frequencyItem = view.findViewById(R.id.wiFiBand);
        TextView frequencyRangeItem = view.findViewById(R.id.channel_frequency_range_in_detailed);
        Utils.FrequencyBand fBand = Utils.getFrequencyBand(itemData);
        String frequencyItemText = "";
        String frequencyRangeItemText = "";
        if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ) {
            frequencyItemText = "2.4";
            int frequency = 2412 + ((Integer.parseInt(channel) - 1) * 5);
            if (!channel.equals("1")) {
                frequencyRangeItemText = (frequency - 20) + " - " + (frequency + 20);
            } else {
                frequencyRangeItemText = (frequency - 10) + " - " + (frequency + 10);
            }
        } else if (fBand == Utils.FrequencyBand.FIVE_GHZ) {
            frequencyItemText = "5";
            int frequency = 5000 + (Integer.parseInt(channel) * 5);
            if (!channel.equals("1")) {
                frequencyRangeItemText = (frequency - 20) + " - " + (frequency + 20);
            } else {
                frequencyRangeItemText = (frequency - 10) + " - " + (frequency + 10);
            }
        } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
            frequencyItemText = "6";
            frequencyRangeItemText = 5940 + " - " + 7100;
        }
        frequencyItem.setText(frequencyItemText + " GHz");
        frequencyRangeItem.setText(frequencyRangeItemText);

        TextView distanceItem = view.findViewById(R.id.distan_in_detailedce);
        double exp = (27.55 - (20 * Math.log10(Double.parseDouble(frequencyItemText))) + Math.abs(itemData.level)) / 20.0;
        double distanceM = Math.pow(10.0, exp);
        distanceItem.setText("~" + String.valueOf(distanceM / 1000).substring(0, 5) + "m");

        TextView timestampItem = view.findViewById(R.id.timestamp);
        timestampItem.setText(itemData.timestamp / 1000 + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss.SSS", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestampItem.setText(simpleDateFormat.format(new Date(itemData.timestamp / 1000)));

        TextView is801Item = view.findViewById(R.id.flag80211mc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(itemData.is80211mcResponder()){
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
        List<VendorModel> vendorList = VendorsAdapter.readFile(context);
        String macAddress = itemData.BSSID.replace(":", "").substring(0, 6).toUpperCase(Locale.ROOT);
        for (VendorModel vendor : vendorList) {
            for (String address : vendor.getMacAddresses()) {
                if (address.contains(macAddress)) {
                    vendorItem.setText(vendor.getVendorName());
                }
            }
        }
        return view;
    }

}
