package ua.tsisar.abetka.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.ArrayMap;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.Set;

import ua.tsisar.abetka.BuildConfig;
import ua.tsisar.abetka.db.DBHelper;
import ua.tsisar.abetka.game.GameResult;
import ua.tsisar.abetka.preference.LoadPreference;
import ua.tsisar.abetka.R;
import ua.tsisar.abetka.dialog.DialogRestart;
import ua.tsisar.abetka.preference.SwitchPreferenceExt;
import ua.tsisar.abetka.dialog.DialogAbout;
import ua.tsisar.abetka.dialog.DialogStatistics;

import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_ABOUT;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_BEST_RESULT;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_LANGUAGE;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_ORIENTATION;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_ORDER;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_SOUND_SPEAK;
import static ua.tsisar.abetka.preference.PreferenceConstants.KEY_SOUND_STATE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_LANGUAGE_EN;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_DATE;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_ORDER_STARS;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_SOUND_SPEAK_NOISE;


public class PreferenceFragment extends PreferenceFragmentCompat
        implements OnPreferenceChangeListener, OnPreferenceClickListener{

    private Preference bestResult;
    private SwitchPreferenceExt soundState;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        findPreference(KEY_ORIENTATION).setOnPreferenceChangeListener(this);
        findPreference(KEY_LANGUAGE).setOnPreferenceChangeListener(this);

        Preference soundSpeak = findPreference(KEY_SOUND_SPEAK);
        soundSpeak.setOnPreferenceChangeListener(this);
        soundSpeak.setSummary(
                setToString(PreferenceManager.getDefaultSharedPreferences(
                        soundSpeak.getContext()).getStringSet(
                        soundSpeak.getKey(), null)));

        soundState = (SwitchPreferenceExt) findPreference(KEY_SOUND_STATE);

        bestResult = findPreference(KEY_BEST_RESULT);
        bestResult.setOnPreferenceClickListener(this);
        bestResult.setSummary(getBestResult());

        Preference about = findPreference(KEY_ABOUT);
        about.setOnPreferenceClickListener(this);
        about.setSummary(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));

        findPreference(KEY_ORDER).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case KEY_BEST_RESULT:
                DialogStatistics dialogStatistics = new DialogStatistics();
                dialogStatistics.show(getFragmentManager(), "dialogStatistics");
                dialogStatistics.onDialogStatisticsListener(new DialogStatistics.DialogStatisticsListener(){
                    @Override
                    public void onClearStatistics() {
                        bestResult.setSummary(getBestResult());
                    }
                });
                return true;

            case KEY_ABOUT:
                DialogAbout dialogAbout = new DialogAbout();
                dialogAbout.show(getFragmentManager(), "dialogAbout");
                return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        switch (preference.getKey()) {
            case KEY_ORIENTATION:
            case KEY_LANGUAGE:
                DialogRestart dialogRestart = new DialogRestart();
                dialogRestart.show(getFragmentManager(), "dialogRestart");
                // set orientation
                return true;

            case KEY_SOUND_SPEAK:
                Set set = (Set)newValue;
                if(set.size() == 0) {
                    soundState.performClick();
                    return false;
                }
                preference.setSummary(setToString(set));
                return true;

            case KEY_ORDER:
                bestResult.setSummary(getBestResult((String) newValue));
                return true;
        }
        return false;
    }

    private String setToString(Set set){
        ArrayMap<String, String> arrayMap = getArrayMap(
                getResources().getStringArray(R.array.pref_sound_speak_entry_values),
                getResources().getStringArray(R.array.pref_sound_speak_entries));

//        String res = "";
//        for (Object in : set){
//            if(res.length() > 1)
//                res += ", ";
//            res += arrayMap.get(in);
//        }

        if(new LoadPreference(getContext()).getLanguage().equals(VALUE_LANGUAGE_EN)){
            set.remove(VALUE_SOUND_SPEAK_NOISE);
        }

        StringBuilder res = new StringBuilder();
        for (Object in : set){
            if(res.length() > 1)
                res.append(", ");
            res.append(arrayMap.get(in));
        }

        return res.toString();
    }

    private ArrayMap<String, String> getArrayMap(String[] key, String[] value){
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        int i = 0;
        while (i < key.length && i < value.length){
            arrayMap.put(key[i], value[i]);
            i++;
        }
        return arrayMap;
    }

    private String getBestResult(String order){
        return bestRes(order);
    }

    private String getBestResult(){
        return bestRes(new LoadPreference(getActivity()).getStatisticsOrder());
    }

    private String bestRes(String order){
        String result = getString(R.string.dialog_null_row);
        ArrayList<GameResult> gameResults = new DBHelper(getActivity()).getHeightResults();

        if(gameResults.size() >= 1) {
            GameResult gameResult = gameResults.get(0);
            result = String.format(getString(getFormatId(order)),
                    gameResult.getAnswer(), gameResult.getStars(), gameResult.getDate());
        }

        return result;
    }

    private int getFormatId(String order){
        switch (order){
            case VALUE_ORDER_STARS:
                return R.string.pref_summary_info_best_result_stars;

            case VALUE_ORDER_DATE:
                return R.string.pref_summary_info_best_result_date;

            default:
                return R.string.pref_summary_info_best_result_answers;
        }
    }
}