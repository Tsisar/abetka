package ua.tsisar.abetka;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import ua.tsisar.abetka.preference.LoadPreference;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LoadPreference loadPreference = new LoadPreference(activity);
                loadPreference.setOrientation();
                loadPreference.setLanguage();
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

        });

    }
}