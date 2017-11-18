package ua.tsisar.abetka.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import java.util.HashSet;
import java.util.Set;

import ua.tsisar.abetka.R;
import ua.tsisar.abetka.dialog.DialogExit;

import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_SOUND_SPEAK;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //TODO Видалити в наступній версії
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> set = sharedPreferences.getStringSet(KEY_SOUND_SPEAK, new HashSet<String>());
        if(set.remove("noice")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(KEY_SOUND_SPEAK, set);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialogExit = new DialogExit();
        dialogExit.show(getSupportFragmentManager(), "dialogExit");
    }

    public void startActivity(View view){
        view.startAnimation(new AlphaAnimation(1F, 0.2F));

        Intent intent = new Intent(this, getActivityClass(view.getId()));
        startActivity(intent);
    }

    private Class getActivityClass(int viewId){
        switch (viewId){
            case R.id.image_button_main:
                return MainActivity.class;
            case  R.id.image_button_grid:
                return GridActivity.class;
            case R.id.image_button_game:
                return GameActivity.class;
            case R.id.image_button_coffee:
                return CoffeeActivity.class;
            case R.id.image_button_settings:
                return SettingsActivity.class;
        }
        return HomeActivity.class;
    }
}
