package com.example.wifi.ui.access_points;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wifi.R;

import java.util.ArrayList;

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

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.access_point_detailed_view, null);
        }

        ScanResult itemData = data.get(i);
//TODO add more fields
        // ssid
        TextView ssidItem = view.findViewById(R.id.ssid);
        ssidItem.setText(itemData.SSID);
        return view;
    }
}
