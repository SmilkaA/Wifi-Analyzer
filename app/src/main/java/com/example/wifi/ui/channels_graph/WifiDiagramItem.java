package com.example.wifi.ui.channels_graph;

public class WifiDiagramItem {
    String SSID;
    String BSSID;
    int frequency;
    int channelWidth;
    int dBm;
    int color;

    public WifiDiagramItem(String SSID, String BSSID, int frequency, int channelWidth, int dBm) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.frequency = frequency;
        this.channelWidth = channelWidth;
        this.dBm = dBm;
    }

    public WifiDiagramItem(WifiDiagramItem wdi) {
        this.SSID = wdi.SSID;
        this.BSSID = wdi.BSSID;
        this.frequency = wdi.frequency;
        this.channelWidth = wdi.channelWidth;
        this.dBm = wdi.dBm;
        this.color = wdi.color;
    }
}
