package com.example.wifi.ui.vendors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.databinding.FragmentVendorsBinding;

import java.util.List;

public class VendorsFragment extends Fragment {

    private FragmentVendorsBinding binding;
    private ListView vendorsListView;
    private List<VendorModel> vendorList;
    private VendorsAdapter vendorsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VendorsViewModel vendorsViewModel =
                new ViewModelProvider(this).get(VendorsViewModel.class);

        binding = FragmentVendorsBinding.inflate(inflater, container, false);
        vendorsListView = binding.vendorsList;
        vendorsAdapter = new VendorsAdapter(getActivity(), vendorList);
        vendorsListView.setAdapter(vendorsAdapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}