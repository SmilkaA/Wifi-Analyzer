package com.example.wifi.ui.channels_rate;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.wifi.R;
import com.example.wifi.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelRateAdapter extends BaseAdapter {
    private final List<ScanResult> wifiData;
    private final List<Integer> channelsFrequency;
    private final List<Integer> channelsNumbers;
    private final Map<Integer, Integer> countedChannels = new HashMap<>();
    private static LayoutInflater inflater = null;

    public ChannelRateAdapter(Context context, List<ScanResult> data, Map<Integer, Integer> channelsBand) {
        if (data != null) {
            this.wifiData = data;
        } else {
            this.wifiData = new ArrayList<>();
        }
        channelsBand = Utils.sortMapByValues(channelsBand);
        this.channelsFrequency = new ArrayList<>(channelsBand.keySet());
        this.channelsNumbers = new ArrayList<>(channelsBand.values());

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return channelsNumbers.size();
    }

    @Override
    public Integer getItem(int i) {
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
            view = inflater.inflate(R.layout.channel_rating_list_details, viewGroup, false);
        }
        int channelNumber = channelsNumbers.get(i);
        initViewComponent(channelNumber, view);
        return view;
    }

    private void initViewComponent(int channelNumber, View view) {
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
    }

    private boolean wifiAcceptsChannel(ScanResult wifi, int checkChannel) {
        Utils.FrequencyBand fBand = Utils.getFrequencyBand(wifi);
        int channel = 0;
        int[] frequencies = Utils.getFrequencies(wifi);
        if (frequencies.length == 1) {
            channel = Utils.getChannel(frequencies[0]);
        }
        int frequencyMin = Utils.START_24GHZ_BAND - Utils.CHANNELS_RANGE_INDEX;
        int frequencyMax = Utils.START_24GHZ_BAND + Utils.CHANNELS_RANGE_INDEX;
        if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ
                && channelsFrequency.size() == Utils.CHANNELS_24GHZ_BAND.size()) {
            if (!(channel == 1)) {
                frequencyMin += ((channel - 1) * Utils.CHANNELS_MULTIPLIER);
                frequencyMax += ((channel - 1) * Utils.CHANNELS_MULTIPLIER);
            }
            return (frequencyMin <= channelsFrequency.get(checkChannel - 1))
                    && (channelsFrequency.get(checkChannel - 1) <= frequencyMax);
        } else if (fBand == Utils.FrequencyBand.FIVE_GHZ
                && channelsFrequency.size() == Utils.CHANNELS_5GHZ_BAND.size()) {
            if (!(channel == 1)) {
                frequencyMin = Utils.START_5GHZ_BAND + (channel * Utils.CHANNELS_MULTIPLIER)
                        - Utils.CHANNELS_RANGE_INDEX;
                frequencyMax = Utils.CHANNELS_MULTIPLIER * Utils.START_5GHZ_BAND
                        + (channel * Utils.CHANNELS_MULTIPLIER) + Utils.CHANNELS_RANGE_INDEX;
            } else {
                frequencyMin = Utils.START_5GHZ_BAND + (channel * Utils.CHANNELS_MULTIPLIER)
                        - Utils.FIRST_CHANNEL_RANGE_INDEX;
                frequencyMax = Utils.CHANNELS_MULTIPLIER * Utils.START_5GHZ_BAND
                        + (channel * Utils.CHANNELS_MULTIPLIER) + Utils.FIRST_CHANNEL_RANGE_INDEX;
            }
            return frequencyMin <= channelsFrequency.get(checkChannel - 1) &&
                    channelsFrequency.get(checkChannel - 1) <= frequencyMax;
        } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
            return (Utils.START_6GHZ_BAND <= channelsFrequency.get(checkChannel - 1))
                    && (channelsFrequency.get(checkChannel - 1) <= Utils.END_6GHZ_BAND);
        }
        return false;
    }

    public Map<Integer, Integer> getCountedChannels() {
        return countedChannels;
    }

    public String getBestChannels(Map<Integer, Integer> channelsRate) {
        return Utils.sortMapByValues(channelsRate).keySet()
                .toString().replace('[', ' ').replace(']', ' ');
    }
}
