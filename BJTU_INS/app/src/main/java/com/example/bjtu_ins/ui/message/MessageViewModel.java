package com.example.bjtu_ins.ui.message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MessageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("消息页面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}