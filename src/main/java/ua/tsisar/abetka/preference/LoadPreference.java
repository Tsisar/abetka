package ua.tsisar.abetka.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static ua.tsisar.abetka.preference.PreferenceConstants.DEFAULT_LANGUAGE;
import static ua.tsisar.abetka.preference.PreferenceConstants.DEFAULT_NUMBER;
import static ua.tsisar.abetka.preference.PreferenceConstants.DEFAULT_ORIENTATION;
import static ua.tsisar.abetka.preference.PreferenceConstants.DEFAULT_SORTING;
import static ua.tsisar.abetka.preference.PreferenceConstants.DEFAULT_VOICE;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_BUTTONS;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_HINT;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_LANGUAGE;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_NUMBER;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_ORIENTATION;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_ORDER;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_SOUND_SPEAK;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_SOUND_STATE;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_VOICE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_LANGUAGE_UK;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_DATE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_STARS;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_SOUND_SPEAK_LETTER;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_SOUND_SPEAK_NAME;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_SOUND_SPEAK_NOISE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_VOICE;

public class LoadPreference {

    private final Context context;
    private SharedPreferences sharedPreferences;

    public LoadPreference(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void setOrientation(String orientation) {
        final int ORIENTATION_PORTRAIT = 1;
        final int ORIENTATION_LANDSCAPE = 0;

        int intOrientation = Integer.parseInt(orientation);
        Activity activity = (Activity) context;

        switch (intOrientation) {
            case ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case ORIENTATION_PORTRAIT:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
        }
        //return intOrientation;
    }

    public void setOrientation() {
        setOrientation(sharedPreferences.getString(KEY_ORIENTATION, DEFAULT_ORIENTATION));
    }

    private void setLanguage(String language){
        if (language.equals(DEFAULT_LANGUAGE)) {
            language = Locale.getDefault().getLanguage();
            //language = getLocale().getLanguage();
        }
        Locale locale = new Locale(language);
       // Locale.setDefault(locale);
        Configuration config = new Configuration();
        setLocale(config, locale);
        context.getResources().updateConfiguration(config,
            context.getResources().getDisplayMetrics());
    }


    @SuppressWarnings("deprecation")
    private void setLocale(Configuration config, Locale locale){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }else{
            config.locale = locale;
        }
    }

    public void setLanguage(){
        setLanguage(sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE));
    }

    public String getLanguage(){
        String language = sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
        if (language.equals(DEFAULT_LANGUAGE))
            language = Locale.getDefault().getLanguage();
        return language;
    }

    public boolean getSoundMode() {
        return sharedPreferences.getBoolean(KEY_SOUND_STATE, true);
    }

    public boolean isShowHint() {
        return sharedPreferences.getBoolean(KEY_HINT, true);
    }

    public boolean isButtonsVisible() {
        return sharedPreferences.getBoolean(KEY_BUTTONS, true);
    }

    private String getVoice() {
        return sharedPreferences.getString(KEY_VOICE, DEFAULT_VOICE);
    }

    private ArrayList<String> getSpeak() {
        Set<String> set = sharedPreferences.getStringSet(KEY_SOUND_SPEAK, new HashSet<String>());
        return new ArrayList<>(set);
    }

    public String getStatisticsOrder() {
        return sharedPreferences.getString(KEY_ORDER, DEFAULT_SORTING);
    }

    public String getStatisticsNumber() {
        return sharedPreferences.getString(KEY_NUMBER, DEFAULT_NUMBER);
    }

//    public String getOrderBy(String order){
//        return orderBy(order);
//    }

    public String getOrderBy(){
        return orderBy(sharedPreferences.getString(KEY_ORDER, DEFAULT_SORTING));
    }

    public boolean isVoiceChild(){
        return getVoice().equals(VALUE_VOICE) && getLanguage().equals(VALUE_LANGUAGE_UK);
    }

    public boolean playLetter(){
        return getSpeak().contains(VALUE_SOUND_SPEAK_LETTER);
    }

    public boolean playNoise(){
        return getSpeak().contains(VALUE_SOUND_SPEAK_NOISE);
    }

    public boolean playName(){
        return getSpeak().contains(VALUE_SOUND_SPEAK_NAME);
    }

    private String orderBy(String order){
        switch (order){
            case VALUE_ORDER_STARS:
                return  "stars desc, answer desc, date desc";

            case VALUE_ORDER_DATE:
                return  "date desc, answer desc, stars desc";

            default:
                return  "answer desc, stars desc, date desc";
        }
    }

}
