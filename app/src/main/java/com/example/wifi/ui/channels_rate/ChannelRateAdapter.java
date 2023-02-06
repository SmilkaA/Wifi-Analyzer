package com.example.wifi.ui.channels_rate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.wifi.R;
import com.example.wifi.Utils;

import java.util.ArrayList;

public class ChannelRateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScanResult> data;
    private static LayoutInflater inflater = null;

    public ChannelRateAdapter(Context context, ArrayList<ScanResult> data) {
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

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.channel_rating_list_details, null);
        }

        ScanResult itemData = data.get(i);
        Object[] channels24ghz = Utils.CHANNELS_24GHZ_BAND.values().toArray();
        RatingBar ratingBarItem = view.findViewById(R.id.channelRating);
        ratingBarItem.setRating(4F);
        ratingBarItem.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        TextView channelNumberItem = view.findViewById(R.id.channelNumber);
        channelNumberItem.setText(itemData.SSID);
        TextView accessPointCountItem = view.findViewById(R.id.accessPointCount);
        accessPointCountItem.setText(itemData.SSID);
        return view;
    }
}
