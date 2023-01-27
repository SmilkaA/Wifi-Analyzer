package com.example.wifi.ui.access_points;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccessPointsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AccessPointsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}