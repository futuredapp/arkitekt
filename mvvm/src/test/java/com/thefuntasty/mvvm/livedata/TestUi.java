package com.thefuntasty.mvvm.livedata;

public class TestUi {
    private UiData<String> data = new UiData<String>("asda");

    public void setData(UiData<String> data) {
        this.data.setValue(null);
    }
}
