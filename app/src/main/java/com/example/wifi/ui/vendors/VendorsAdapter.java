package com.example.wifi.ui.vendors;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wifi.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VendorsAdapter extends BaseAdapter {
    private List<VendorModel> vendorList;
    private Context context;
    private static LayoutInflater inflater = null;

    public VendorsAdapter(Context context, List<VendorModel> vendorList) {
        this.vendorList = readFile(context);
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vendorList.size();
    }

    @Override
    public Object getItem(int i) {
        return vendorList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.vendors_list_details, null);
        }

        VendorModel vendor = vendorList.get(i);
        TextView vendorNameItem = view.findViewById(R.id.vendor_name);
        vendorNameItem.setText(vendor.getVendorName());

        TextView macAddressesItem = view.findViewById(R.id.vendor_macs);
        StringBuilder macAddresses = new StringBuilder();
        for (String macAddress : vendor.getMacAddresses()) {
            macAddresses.append(macAddress).append(" ");
        }
        macAddressesItem.setText(macAddresses.toString());

        return view;
    }

    public List<VendorModel> readFile(Context activity) {
        Resources r = activity.getResources();
        List<VendorModel> vendors = new ArrayList<>();
        InputStream inputStream = r.openRawResource(R.raw.data);
        InputStreamReader isr = new InputStreamReader(inputStream,
                StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(isr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bufferedReader.lines().forEach(line -> {
                String vendorName = line.substring(0, line.indexOf('|'));
                String macAddressesLine = line.substring(line.indexOf('|') + 1);
                List<String> macAddresses = new ArrayList<>();
                for (int i = 0; i < macAddressesLine.length(); i += 6) {
                    int length = 6;
                    macAddresses.add(macAddressesLine.substring(i, length + i));
                }
                vendors.add(new VendorModel(vendorName, macAddresses));
            });
        }
        try {
            inputStream.close();
        } catch (IOException ignored) {
        }
        return vendors;
    }
}
