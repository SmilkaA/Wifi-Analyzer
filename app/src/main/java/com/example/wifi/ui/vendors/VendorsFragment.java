package com.example.wifi.ui.vendors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wifi.databinding.FragmentVendorsBinding;

import java.util.List;

public class VendorsFragment extends Fragment {

    private FragmentVendorsBinding binding;
    private ListView vendorsListView;
    private List<VendorModel> vendorList;
    private VendorsAdapter vendorsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVendorsBinding.inflate(inflater, container, false);

        vendorsListView = binding.vendorsList;
        vendorsAdapter = new VendorsAdapter(getActivity(), vendorList);
        vendorsListView.setAdapter(vendorsAdapter);

        binding.vendorSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                vendorsAdapter.updateAdapter(searchText);
                vendorsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}