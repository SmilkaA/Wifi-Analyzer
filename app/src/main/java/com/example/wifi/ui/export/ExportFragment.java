package com.example.wifi.ui.export;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.databinding.FragmentExportBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExportFragment extends Fragment {

    private FragmentExportBinding binding;
    private final String title = "Access points for " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy - HH:mm :"));
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentExportBinding.inflate(inflater, container, false);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, title + getAccessPointData());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, title);
        startActivity(shareIntent);
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

    private String getAccessPointData() {
        return String.valueOf(mainActivity.getData());
    }
}