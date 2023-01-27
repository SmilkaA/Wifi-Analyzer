package com.example.wifi.ui.port_authority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.databinding.FragmentPortAuthorityBinding;

public class PortAuthorityFragment extends Fragment {

    private FragmentPortAuthorityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PortAuthorityViewModel portAuthorityViewModel =
                new ViewModelProvider(this).get(PortAuthorityViewModel.class);

        binding = FragmentPortAuthorityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        portAuthorityViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}