package com.example.stepappv4.ui.CalorieCalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalorieCalculatorViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalorieCalculatorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calorie fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
