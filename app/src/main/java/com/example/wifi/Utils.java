package com.example.wifi;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final String PREF_SORTING_OPTION = "PREF_SORTING_OPTION";
    public static final String PREF_WLAN_ENABLED_BY_APP = "PREF_WLAN_ENABLED_BY_APP";
    public static final String PREF_SELECTED_TAB = "PREF_SELECTED_TAB";

    public static final String PREF_FILTER_SSID_ENABLED = "PREF_FILTER_SSID_ENABLED";
    public static final String PREF_FILTER_SSID = "PREF_FILTER_SSID";

    public static final String PREF_FILTER_CHANNEL_ENABLED = "PREF_FILTER_CHANNEL_ENABLED";
    public static final String PREF_FILTER_CHANNEL = "PREF_FILTER_CHANNEL";

    public static final String PREF_FILTER_CAPABILI_ENABLED = "PREF_FILTER_CAPABILI_ENABLED";
    public static final String PREF_FILTER_CAPABILI = "PREF_FILTER_CAPABILI";

    public static final String PREF_SETTING_SCAN_DELAY = "PREF_SETTING_SCAN_DELAY";

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

    private static final List<String> CHANNELS_24GHZ_COUNTRIES = Arrays.asList("AS", "CA", "CO", "DO", "FM", "GT", "GU", "MP", "MX", "PA", "PR", "UM", "US", "UZ", "VI");
    private static final List<String> CHANNELS_5GHZ_COUNTRIES = Arrays.asList(
            "AT",      // ETSI Austria
            "BE",      // ETSI Belgium
            "CH",      // ETSI Switzerland
            "CY",      // ETSI Cyprus
            "CZ",      // ETSI Czechia
            "DE",      // ETSI Germany
            "DK",      // ETSI Denmark
            "EE",      // ETSI Estonia
            "ES",      // ETSI Spain
            "FI",      // ETSI Finland
            "FR",      // ETSI France
            "GR",      // ETSI Greece
            "HU",      // ETSI Hungary
            "IE",      // ETSI Ireland
            "IS",      // ETSI Iceland
            "IT",      // ETSI Italy
            "LI",      // ETSI Liechtenstein
            "LT",      // ETSI Lithuania
            "LU",      // ETSI Luxembourg
            "LV",      // ETSI Latvia
            "MT",      // ETSI Malta
            "NL",      // ETSI Netherlands
            "NO",      // ETSI Norway
            "PL",      // ETSI Poland
            "PT",      // ETSI Portugal
            "RO",      // ETSI Romania
            "SE",      // ETSI Sweden
            "SI",      // ETSI Slovenia
            "SK",      // ETSI Slovakia
            "IL"       // ETSI Israel
    );

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
            case ScanResult.CHANNEL_WIDTH_20MHZ:
                return 20;
            case ScanResult.CHANNEL_WIDTH_40MHZ:
                return 40;
            case ScanResult.CHANNEL_WIDTH_80MHZ:
            case ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ:
                return 80;
            case ScanResult.CHANNEL_WIDTH_160MHZ:
                return 160;
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
}
