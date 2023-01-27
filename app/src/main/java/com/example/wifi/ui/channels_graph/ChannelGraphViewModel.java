package com.example.wifi.ui.channels_graph;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChannelGraphViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChannelGraphViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}