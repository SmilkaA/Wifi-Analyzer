package com.example.wifi.ui.channels_rate;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.wifi.R;
import com.example.wifi.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChannelRateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScanResult> wifiData;
    private List<Integer> channelsFrequency;
    private List<Integer> channelsNumbers;
    private Map<Integer, Integer> countedChannels = new HashMap<>();
    private static LayoutInflater inflater = null;

    public ChannelRateAdapter(Context context, ArrayList<ScanResult> data, Map<Integer, Integer> channelsBand) {
        this.context = context;
        this.wifiData = data;
        channelsBand = sortByValues(channelsBand);
        this.channelsFrequency = new ArrayList<>(channelsBand.keySet());
        this.channelsNumbers = new ArrayList<>(channelsBand.values());

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return channelsNumbers.size();
    }

    @Override
    public Object getItem(int i) {
        return channelsNumbers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.channel_rating_list_details, null);
        }

        int channelNumber = channelsNumbers.get(i);
        TextView channelNumberItem = view.findViewById(R.id.channelNumber);
        channelNumberItem.setText(String.valueOf(channelNumber));
        TextView accessPointCountItem = view.findViewById(R.id.accessPointCount);
        int accessPointCount = 0;
        for (ScanResult wifi : wifiData) {
            if (wifiAcceptsChannel(wifi, channelNumber)) {
                accessPointCount++;
            }
        }
        accessPointCountItem.setText(String.valueOf(accessPointCount));
        RatingBar ratingBarItem = view.findViewById(R.id.channelRating);
        ratingBarItem.setNumStars(wifiData.size());
        ratingBarItem.setStepSize(1F);
        ratingBarItem.setProgress(ratingBarItem.getNumStars() - accessPointCount);
        countedChannels.put(channelNumber, accessPointCount);
        return view;
    }

    private boolean wifiAcceptsChannel(ScanResult wifi, int checkChannel) {
        Utils.FrequencyBand fBand = Utils.getFrequencyBand(wifi);
        int channel = 0;
        int[] frequencies = Utils.getFrequencies(wifi);
        if (frequencies.length == 1) {
            channel = Utils.getChannel(frequencies[0]);
        }
        int frequencyMin;
        int frequencyMax;
        if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ && channelsFrequency.size() == 13) {
            if (!(channel == 1)) {
                frequencyMin = 2412 + ((channel - 1) * 5) - 20;
                frequencyMax = 2412 + ((channel - 1) * 5) + 20;
            } else {
                frequencyMin = 2402;
                frequencyMax = 2422;
            }
            return (frequencyMin <= channelsFrequency.get(checkChannel - 1)) && (channelsFrequency.get(checkChannel - 1) <= frequencyMax);
        } else if (fBand == Utils.FrequencyBand.FIVE_GHZ && channelsFrequency.size() == 65) {
            if (!(channel == 1)) {
                frequencyMin = 5000 + (channel * 5) - 20;
                frequencyMax = 25000 + (channel * 5) + 20;
            } else {
                frequencyMin = 5000 + (channel * 5) - 10;
                frequencyMax = 25000 + (channel * 5) + 10;
            }
            return frequencyMin <= channelsFrequency.get(checkChannel - 1) &&
                    channelsFrequency.get(checkChannel - 1) <= frequencyMax;
        } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
            //TODO: find out how to calculate it
            return false;
        }
        return false;
    }

    private Map<Integer, Integer> sortByValues(Map<Integer, Integer> channelsBand) {
        LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : channelsBand.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        for (int num : list) {
            for (Map.Entry<Integer, Integer> entry : channelsBand.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }
        return sortedMap;
    }

    public Map<Integer, Integer> getCountedChannels() {
        return countedChannels;
    }

    public String getBestChannels(Map<Integer, Integer> channelsRate) {
        return sortByValues(channelsRate).keySet()
                .toString().replace('[', ' ').replace(']', ' ');
    }
}
