package com.example.wifi.ui.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.wifi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.GONE);
        setHasOptionsMenu(true);
        setPreferencesFromResource(R.xml.settings, rootKey);
        fillCountriesListPreference();
        fillLanguagesListPreference();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
    }

    public void fillCountriesListPreference() {
        final ListPreference listPreference = (ListPreference) findPreference("country_code_key");
        String[] countryCodes = Locale.getISOCountries();
        String[] countries = new String[countryCodes.length];
        for (int i = 0; i < countryCodes.length; i++) {
            Locale countryName = new Locale("", countryCodes[i]);
            countries[i] = countryName.getDisplayCountry();
        }
        listPreference.setEntries(countries);
        listPreference.setDefaultValue("US");
        listPreference.setEntryValues(countryCodes);
    }

    private void fillLanguagesListPreference() {
        final ListPreference listPreference = (ListPreference) findPreference("language_key");
        String[] languages = new String[]{"English", "Ukrainian"};
        listPreference.setEntries(languages);
        listPreference.setDefaultValue("English");
        listPreference.setEntryValues(languages);
    }
}