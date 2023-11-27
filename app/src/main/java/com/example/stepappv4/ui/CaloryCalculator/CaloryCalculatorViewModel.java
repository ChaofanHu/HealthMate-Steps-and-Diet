package com.example.stepappv4.ui.CaloryCalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CaloryCalculatorViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CaloryCalculatorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calory fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
