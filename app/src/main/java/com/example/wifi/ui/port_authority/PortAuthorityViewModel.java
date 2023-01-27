package com.example.wifi.ui.port_authority;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PortAuthorityViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PortAuthorityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}