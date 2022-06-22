package com.yashjain.javaquiz;

import android.app.Application;

import io.requestly.android.core.Requestly;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Requestly Initialization
        new Requestly.Builder(MainApplication.this,
                "oDaHcz6EYw9Ps6WQwjw3")
                .build();
    }
}
