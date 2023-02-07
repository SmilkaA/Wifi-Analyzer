package com.example.wifi.ui.access_points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.ui.vendors.VendorModel;
import com.example.wifi.ui.vendors.VendorsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccessPointAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScanResult> data;
    private static LayoutInflater inflater = null;

    public AccessPointAdapter(Context context, ArrayList<ScanResult> data) {
        this.context = context;
        this.data = data;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.access_point_detailed_view, null);
        }

        ScanResult itemData = data.get(i);
        TextView ssidItem = view.findViewById(R.id.ssid_in_detailed);
        ssidItem.setText(itemData.SSID + " (" + itemData.BSSID + ")");

        TextView levelItem = view.findViewById(R.id.level_in_detailed);
        levelItem.setText(context.getString(R.string.level_value, String.valueOf(itemData.level)));

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

        TextView vendorItem = view.findViewById(R.id.vendorShort_in_detailed);
        List<VendorModel> vendorList = VendorsAdapter.readFile(context);
        String macAddress = itemData.BSSID;
        for (VendorModel vendor : vendorList) {
            for (String address : vendor.getMacAddresses()) {
                if (address.contains(macAddress.replace(":", "").substring(0, 6).toUpperCase(Locale.ROOT))) {
                    vendorItem.setText(vendor.getVendorName());
                }
            }
        }

        TextView capabilitiesItem = view.findViewById(R.id.capabilities_in_detailed);
        capabilitiesItem.setText(itemData.capabilities);

        TextView frequencyItem = view.findViewById(R.id.primaryFrequency_in_detailed);
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

        return view;
    }
}
