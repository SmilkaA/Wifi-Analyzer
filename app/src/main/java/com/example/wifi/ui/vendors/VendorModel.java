package com.example.wifi.ui.vendors;

import java.util.List;

public class VendorModel {
    private String vendorName;
    private List<String> macAddresses;

    public VendorModel(String vendorName, List<String> macAddresses) {
        this.vendorName = vendorName;
        this.macAddresses = macAddresses;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<String> getMacAddresses() {
        return macAddresses;
    }

    public void setMacAddresses(List<String> macAddresses) {
        this.macAddresses = macAddresses;
    }
}
