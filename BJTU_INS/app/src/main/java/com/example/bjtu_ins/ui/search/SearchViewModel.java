package com.example.bjtu_ins.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("搜索页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}