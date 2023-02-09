package com.example.wifi.ui.available_channels;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.Utils;
import com.example.wifi.databinding.FragmentAvailableChannelsBinding;
import com.example.wifi.ui.access_points.AccessPointMainView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableChannelsFragment extends Fragment {

    private FragmentAvailableChannelsBinding binding;
    private AccessPointMainView accessPointMainView;
    private TextView countryName;
    private TextView availableChannels2;
    private TextView availableChannels5;
    private TextView availableChannels6;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAvailableChannelsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        accessPointMainView = binding.availableChannelsAccessPointView;
        mainActivity.fillCurrentlyConnectedAccessPoint(accessPointMainView);

        countryName = binding.availableChannelsList.channelAvailableCountry;
        countryName.setText(getCountryName("UA"));
        availableChannels2 = binding.availableChannelsList.channelAvailableGhz2;
        availableChannels2.setText(getAvailableChannels24("UA"));
        availableChannels5 = binding.availableChannelsList.channelAvailableGhz5;
        availableChannels5.setText(getAvailableChannels5("UA"));
        availableChannels6 = binding.availableChannelsList.channelAvailableGhz6;
        availableChannels6.setText(getAvailableChannels6());

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getCountryName(String countryCode) {
        return countryCode;
    }

    private String getAvailableChannels24(String countryCode) {
        Map<Integer, Integer> channels = new HashMap<>(Utils.CHANNELS_24GHZ_BAND);
        List<String> countriesWithLessChannels = Arrays.asList("AS", "CA", "CO", "DO", "FM", "GT", "GU", "MP", "MX", "PA", "PR", "UM", "US", "UZ", "VI");
        if (countriesWithLessChannels.contains(countryCode)) {
            channels.values().remove(12);
            channels.values().remove(13);
        }
        channels = Utils.sortMapByValues(channels);
        return channels.values().toString().replace('[', ' ').replace(']', ' ');
    }

    private String getAvailableChannels5(String countryCode) {
        Map<Integer, Integer> channels = new HashMap<>(Utils.CHANNELS_5GHZ_BAND);
        if (countryCode.equals("AU") || countryCode.equals("CA")) {
            Log.d("111", String.valueOf(channels.values()));
            channels.values().remove(120);
            channels.values().remove(124);
            channels.values().remove(128);
        } else if (countryCode.equals("CN") || countryCode.equals("KR")) {
            channels.values().remove(100);
            channels.values().remove(104);
            channels.values().remove(108);
            channels.values().remove(112);
            channels.values().remove(116);
            channels.values().remove(120);
            channels.values().remove(124);
            channels.values().remove(128);
            channels.values().remove(132);
            channels.values().remove(136);
            channels.values().remove(140);
            channels.values().remove(144);
        } else if (countryCode.equals("JP") || countryCode.equals("TR") || countryCode.equals("ZA")) {
            channels.values().remove(149);
            channels.values().remove(153);
            channels.values().remove(157);
            channels.values().remove(161);
            channels.values().remove(165);
        }
        channels = Utils.sortMapByValues(channels);
        return channels.values().toString().replace('[', ' ').replace(']', ' ');
    }

    private String getAvailableChannels6() {
        return Utils.sortMapByValues(Utils.CHANNELS_6GHZ_BAND).values()
                .toString().replace('[', ' ').replace(']', ' ');
    }
}