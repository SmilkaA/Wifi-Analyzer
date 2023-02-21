package com.example.wifi.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.R;
import com.example.wifi.databinding.FragmentAboutAppBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutFragment extends Fragment {

    private FragmentAboutAppBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setVisibility(View.GONE);
        setHasOptionsMenu(true);

        AboutViewModel aboutViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutAppBinding.inflate(inflater, container, false);

        final TextView textView = binding.textHome;
        aboutViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(false);
        }
    }
}