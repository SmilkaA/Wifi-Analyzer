package com.example.wifi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.widget.TextView;

import com.example.wifi.ui.vendors.VendorModel;
import com.example.wifi.ui.vendors.VendorsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class Utils {

    public static final int MILLISECONDS = 1000;
    public static final int FILTER_FRAGMENT = 1;
    public static final String PREFERENCES_PATH = "com.example.wifi.preferences";

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    public enum FrequencyBand {
        TWO_FOUR_GHZ,
        FIVE_GHZ,
        SIX_GHZ,
        UNKNOWN
    }

    public static final Map<Integer, Integer> CHANNELS_24GHZ_BAND;
    public static final Map<Integer, Integer> CHANNELS_5GHZ_BAND;
    public static final Map<Integer, Integer> CHANNELS_6GHZ_BAND;

    public static final int START_24GHZ_BAND = 2412;
    public static final int END_24GHZ_BAND = 2484;

    public static final int START_5GHZ_BAND = 4915;
    public static final int END_5GHZ_BAND = 5865;

    public static final int START_6GHZ_BAND = 5940;
    public static final int END_6GHZ_BAND = 7100;


    public static final int MIN_FOR_4_SIGNAL_WIFI_LEVEL = -35;
    public static final int MIN_FOR_3_SIGNAL_WIFI_LEVEL = -55;
    public static final int MIN_FOR_2_SIGNAL_WIFI_LEVEL = -80;
    public static final int MIN_FOR_1_SIGNAL_WIFI_LEVEL = -90;

    public static final int CHANNELS_RANGE_INDEX = 20;
    public static final int FIRST_CHANNEL_RANGE_INDEX = 10;

    public static final double FREQUENCY_24 = 2.4;
    public static final int FREQUENCY_5 = 5;
    public static final int FREQUENCY_6 = 6;

    public static final int CHANNELS_MULTIPLIER = 6;
    public static final int RANDOM_COLOR_BOUND_INDEX = 256;

    public static final String INTENT_LIST_KEY ="resultList";

    static {
        Map<Integer, Integer> aMap = new HashMap<>();
        aMap.put(2412, 1);
        aMap.put(2417, 2);
        aMap.put(2422, 3);
        aMap.put(2427, 4);
        aMap.put(2432, 5);
        aMap.put(2437, 6);
        aMap.put(2442, 7);
        aMap.put(2447, 8);
        aMap.put(2452, 9);
        aMap.put(2457, 10);
        aMap.put(2462, 11);
        aMap.put(2467, 12);
        aMap.put(2472, 13);
        CHANNELS_24GHZ_BAND = Collections.unmodifiableMap(aMap);

        aMap = new HashMap<>();
        aMap.put(4915, 183);
        aMap.put(4920, 184);
        aMap.put(4925, 185);
        aMap.put(4935, 187);
        aMap.put(4940, 188);
        aMap.put(4945, 189);
        aMap.put(4960, 192);
        aMap.put(4980, 196);
        aMap.put(5035, 7);
        aMap.put(5040, 8);
        aMap.put(5045, 9);
        aMap.put(5055, 11);
        aMap.put(5060, 12);
        aMap.put(5080, 16);
        aMap.put(5160, 32);
        aMap.put(5170, 34);
        aMap.put(5180, 36);
        aMap.put(5190, 38);
        aMap.put(5200, 40);
        aMap.put(5210, 42);
        aMap.put(5220, 44);
        aMap.put(5230, 46);
        aMap.put(5240, 48);
        aMap.put(5250, 50);
        aMap.put(5260, 52);
        aMap.put(5270, 54);
        aMap.put(5280, 56);
        aMap.put(5290, 58);
        aMap.put(5300, 60);
        aMap.put(5310, 62);
        aMap.put(5320, 64);
        aMap.put(5340, 68);
        aMap.put(5480, 96);
        aMap.put(5500, 100);
        aMap.put(5510, 102);
        aMap.put(5520, 104);
        aMap.put(5530, 106);
        aMap.put(5540, 108);
        aMap.put(5550, 110);
        aMap.put(5560, 112);
        aMap.put(5570, 114);
        aMap.put(5580, 116);
        aMap.put(5590, 118);
        aMap.put(5600, 120);
        aMap.put(5610, 122);
        aMap.put(5620, 124);
        aMap.put(5630, 126);
        aMap.put(5640, 128);
        aMap.put(5660, 132);
        aMap.put(5670, 134);
        aMap.put(5680, 136);
        aMap.put(5690, 138);
        aMap.put(5700, 140);
        aMap.put(5710, 142);
        aMap.put(5720, 144);
        aMap.put(5745, 149);
        aMap.put(5755, 151);
        aMap.put(5765, 153);
        aMap.put(5775, 155);
        aMap.put(5785, 157);
        aMap.put(5795, 159);
        aMap.put(5805, 161);
        aMap.put(5825, 165);
        aMap.put(5845, 169);
        aMap.put(5865, 173);
        CHANNELS_5GHZ_BAND = Collections.unmodifiableMap(aMap);

        aMap = new HashMap<>();
        int channel = 1;
        for (int i = START_6GHZ_BAND; i <= END_6GHZ_BAND; i += 20) {
            aMap.put(i, channel);
            channel += 4;
        }

        channel = 3;
        for (int i = 5950; i <= 7070; i += 40) {
            aMap.put(i, channel);
            channel += 8;
        }

        channel = 7;
        for (int i = 5970; i <= 7010; i += 80) {
            aMap.put(i, channel);
            channel += 16;
        }

        channel = 15;
        for (int i = 6010; i <= 6970; i += 160) {
            aMap.put(i, channel);
            channel += 32;
        }

        channel = 31;
        for (int i = 6090; i <= 7050; i += 320) {
            aMap.put(i, channel);
            channel += 64;
        }

        CHANNELS_6GHZ_BAND = Collections.unmodifiableMap(aMap);
    }

    public static FrequencyBand getFrequencyBand(ScanResult sr) {
        return getFrequencyBand(getFrequencies(sr)[0]);
    }

    public static FrequencyBand getFrequencyBand(int frequency) {
        if (CHANNELS_24GHZ_BAND.containsKey(frequency)) {
            return FrequencyBand.TWO_FOUR_GHZ;
        } else if (CHANNELS_5GHZ_BAND.containsKey(frequency)) {
            return FrequencyBand.FIVE_GHZ;
        } else if (CHANNELS_6GHZ_BAND.containsKey(frequency)) {
            return FrequencyBand.SIX_GHZ;
        } else {
            return FrequencyBand.UNKNOWN;
        }
    }

    public static int[] getFrequencies(ScanResult sr) {
        if (android.os.Build.VERSION.SDK_INT < 23 || sr.channelWidth == ScanResult.CHANNEL_WIDTH_20MHZ) {
            return new int[]{sr.frequency};
        } else {
            if (sr.channelWidth == ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ) {
                return new int[]{sr.centerFreq0, sr.centerFreq1};
            } else {
                return new int[]{sr.centerFreq0};
            }
        }
    }

    public static String getChannels(ScanResult itemData) {
        StringBuilder channel = new StringBuilder();
        int[] frequencies = Utils.getFrequencies(itemData);
        if (frequencies.length == 1) {
            channel.append(Utils.getChannel(frequencies[0]));
        } else if (frequencies.length >= 1) {
            channel.append(Utils.getChannel(frequencies[0])).append("(").append(Utils.getChannel(frequencies[1])).append(")");
        }
        return String.valueOf(channel);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getWifiIcon(Context context, ScanResult itemData) {
        Drawable picture;
        if (itemData.level >= MIN_FOR_4_SIGNAL_WIFI_LEVEL) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_4_bar", null, context.getPackageName()));
        } else if (itemData.level >= MIN_FOR_3_SIGNAL_WIFI_LEVEL) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_3_bar", null, context.getPackageName()));
        } else if (itemData.level >= MIN_FOR_2_SIGNAL_WIFI_LEVEL) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_2_bar", null, context.getPackageName()));
        } else if (itemData.level >= MIN_FOR_1_SIGNAL_WIFI_LEVEL) {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_1_bar", null, context.getPackageName()));
        } else {
            picture = context.getResources().getDrawable(context.getResources().getIdentifier("@drawable/ic_signal_wifi_0_bar", null, context.getPackageName()));
        }
        return picture;
    }

    public static void setLevelTextColor(int level, TextView levelItem) {
        if (level > MIN_FOR_4_SIGNAL_WIFI_LEVEL) {
            levelItem.setTextColor(Color.GREEN);
        } else if (level > MIN_FOR_3_SIGNAL_WIFI_LEVEL) {
            levelItem.setTextColor(Color.YELLOW);
        } else if (level > MIN_FOR_2_SIGNAL_WIFI_LEVEL) {
            levelItem.setTextColor(Color.YELLOW);
        } else if (level > MIN_FOR_1_SIGNAL_WIFI_LEVEL) {
            levelItem.setTextColor(Color.RED);
        } else {
            levelItem.setTextColor(Color.RED);
        }
    }

    public static String getVendorName(Context context, ScanResult itemData) {
        List<VendorModel> vendorList = VendorsAdapter.readFile(context);
        String macAddress = itemData.BSSID;
        for (VendorModel vendor : vendorList) {
            for (String address : vendor.getMacAddresses()) {
                if (address.contains(macAddress.replace(":", "").substring(0, 6).toUpperCase(Locale.ROOT))) {
                    return vendor.getVendorName();
                }
            }
        }
        return "";
    }

    public static String getFrequencyItemText(ScanResult itemData) {
        String frequencyItemText = "";
        Utils.FrequencyBand fBand = Utils.getFrequencyBand(itemData);
        if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ) {
            frequencyItemText = String.valueOf(FREQUENCY_24);
        } else if (fBand == Utils.FrequencyBand.FIVE_GHZ) {
            frequencyItemText = String.valueOf(FREQUENCY_5);
        } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
            frequencyItemText = String.valueOf(FREQUENCY_6);
        }
        return frequencyItemText;
    }

    public static String getFrequencyRangeItemText(ScanResult itemData) {
        String frequencyRangeItemText = "";
        Utils.FrequencyBand fBand = Utils.getFrequencyBand(itemData);

        if (fBand == Utils.FrequencyBand.TWO_FOUR_GHZ) {
            int frequency = START_24GHZ_BAND + ((Integer.parseInt(Utils.getChannels(itemData)) - 1) * 5);
            if (!Utils.getChannels(itemData).equals("1")) {
                frequencyRangeItemText = (frequency - CHANNELS_RANGE_INDEX) + " - " + (frequency + CHANNELS_RANGE_INDEX);
            } else {
                frequencyRangeItemText = (frequency - FIRST_CHANNEL_RANGE_INDEX) + " - " + (frequency + FIRST_CHANNEL_RANGE_INDEX);
            }
        } else if (fBand == Utils.FrequencyBand.FIVE_GHZ) {
            int frequency = START_5GHZ_BAND + (Integer.parseInt(Utils.getChannels(itemData)) * 5);
            if (!Utils.getChannels(itemData).equals("1")) {
                frequencyRangeItemText = (frequency - CHANNELS_RANGE_INDEX) + " - " + (frequency + CHANNELS_RANGE_INDEX);
            } else {
                frequencyRangeItemText = (frequency - FIRST_CHANNEL_RANGE_INDEX) + " - " + (frequency + FIRST_CHANNEL_RANGE_INDEX);
            }
        } else if (fBand == Utils.FrequencyBand.SIX_GHZ) {
            frequencyRangeItemText = START_6GHZ_BAND + " - " + END_6GHZ_BAND;
        }
        return frequencyRangeItemText;
    }

    public static String getDistanse(ScanResult itemData) {
        //distance calculation formula
        double exp = (27.55 - (CHANNELS_RANGE_INDEX * Math.log10(Double.parseDouble(getFrequencyItemText(itemData)))) + Math.abs(itemData.level)) / 20.0;
        return String.valueOf(Math.pow(10.0, exp) / 1000).substring(0, 5);
    }

    public static String getTimestamp(ScanResult itemData) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss.SSS", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(new Date(itemData.timestamp / 1000));
    }


    public static int getChannel(int frequency) {
        if (CHANNELS_24GHZ_BAND.containsKey(frequency)) {
            return CHANNELS_24GHZ_BAND.get(frequency);
        } else if (CHANNELS_5GHZ_BAND.containsKey(frequency)) {
            return CHANNELS_5GHZ_BAND.get(frequency);
        } else if (CHANNELS_6GHZ_BAND.containsKey(frequency)) {
            return CHANNELS_6GHZ_BAND.get(frequency);
        } else {
            return -1;
        }
    }

    public static int getChannelWidth(ScanResult sr) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            return 20;
        }

        switch (sr.channelWidth) {
            case ScanResult.CHANNEL_WIDTH_40MHZ:
                return 40;
            case ScanResult.CHANNEL_WIDTH_80MHZ:
            case ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ:
                return 80;
            case ScanResult.CHANNEL_WIDTH_160MHZ:
                return 160;
            case ScanResult.CHANNEL_WIDTH_20MHZ:
            default:
                return 20;
        }
    }

    public static Map<Integer, Integer> sortMapByValues(Map<Integer, Integer> channelsBand) {
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

    public static int getRandomColor() {
        Random randomGenerator = new Random();
        int r = randomGenerator.nextInt(RANDOM_COLOR_BOUND_INDEX);
        int g = randomGenerator.nextInt(RANDOM_COLOR_BOUND_INDEX);
        int b = randomGenerator.nextInt(RANDOM_COLOR_BOUND_INDEX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Color.rgb(r, g, b);
        }
        return Color.BLUE;
    }
}
