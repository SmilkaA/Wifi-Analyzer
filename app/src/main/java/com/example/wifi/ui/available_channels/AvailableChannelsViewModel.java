package com.example.wifi.ui.available_channels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AvailableChannelsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AvailableChannelsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}