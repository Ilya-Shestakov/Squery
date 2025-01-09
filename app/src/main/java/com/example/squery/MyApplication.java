package com.example.squery;

import android.app.Application;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApplication extends Application {

    private boolean isAppInForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleObserver());
    }

    public boolean isAppInForeground() {
        return isAppInForeground;
    }

    private class AppLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void onStart() {
            isAppInForeground = true;
            // Приложение вернулось на передний план
            //Log.d("TAG", "onStart: app active");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void onStop() {
            isAppInForeground = false;
            // Приложение ушло в фон
            //Log.d("TAG", "onStop: app not active");

        }
    }
}
