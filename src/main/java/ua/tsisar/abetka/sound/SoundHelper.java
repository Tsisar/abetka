package ua.tsisar.abetka.sound;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Random;

import ua.tsisar.abetka.preference.LoadPreference;
import ua.tsisar.abetka.R;

import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_LANGUAGE_EN;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_LANGUAGE_RU;
import static ua.tsisar.abetka.preference.PreferenceConstants.VALUE_LANGUAGE_UK;

public class SoundHelper {
    private Resources resources;
    private boolean child;
    private LoadPreference loadPreference;
    private int count;

    public SoundHelper(Context context){
        super();
        loadPreference = new LoadPreference(context);
        child = loadPreference.isVoiceChild();
        resources = context.getResources();
        count = resources.obtainTypedArray(R.array.letters_array).length();
    }

    public  ArrayList<Integer> getMainSounds(int position, int offset){
        ArrayList<Integer> sounds = new ArrayList<>();

        String language = loadPreference.getLanguage();
        boolean child = loadPreference.isVoiceChild();
        boolean letter = loadPreference.playLetter();
        boolean noise = loadPreference.playNoise();
        boolean name = loadPreference.playName();

        switch (language) {
            case VALUE_LANGUAGE_UK:
                if(letter) {
                    if(noise) {
                        sounds.add(child ? R.raw.vletter : R.raw.letter);
                    }
                    sounds.add(getLetter(position));
                }

                if((position % count != 30 && noise) || (!letter && !name)) {
                    if(letter) {
                        sounds.add(child ? R.raw.vnoise : R.raw.noise);
                    }
                    sounds.add(getNoise(position));
                }

                if(name || (!letter && !noise)) {
                    sounds.add(getName(position, offset));
                }
                break;

            case VALUE_LANGUAGE_RU:
                if(letter) {
                    if(noise)
                        sounds.add(R.raw.letter);
                    sounds.add(getLetter(position));
                }
                if((position % count != 27 && position % count != 28 && noise) || (!letter && !name)) {
                    if(letter)
                        sounds.add(R.raw.noise);
                    sounds.add(getNoise(position));
                }
                if(name || (!letter && !noise)) {
                    sounds.add(getName(position, offset));
                }
                break;
            case VALUE_LANGUAGE_EN:
                if(letter) {
                    sounds.add(getLetter(position));
                }
                if(name){
                    sounds.add(getName(position, offset));
                }
                break;
        }
        return sounds;
    }

    public int getCorrectResSound(){
        return resources.obtainTypedArray(child
                ?R.array.game_child_raw_ok_array:R.array.game_raw_ok_array)
                .getResourceId(new Random().nextInt(3), 0);
    }

    public ArrayList<Integer> getIncorrectResSound(int position){
        ArrayList<Integer> sounds = new ArrayList<>();

        if(child) {
            sounds.add(resources.obtainTypedArray(R.array.game_child_raw_no_array)
                    .getResourceId(new Random().nextInt(2), 0));
            sounds.add(R.raw.vright_answer);
        }else {
            sounds.add(resources.obtainTypedArray(R.array.game_raw_no_array)
                    .getResourceId(new Random().nextInt(2), 0));
            sounds.add(R.raw.right_answer);
        }

        sounds.add(getNoise(position));

        return sounds;
    }

    public ArrayList<Integer> getTimeOutResSounds(int position){
        ArrayList<Integer> sounds = new ArrayList<>();

        if(child) {
            sounds.add(R.raw.vtime_out);
            sounds.add(R.raw.vright_answer);
        }else {
            sounds.add(R.raw.time_out);
            sounds.add(R.raw.right_answer);
        }
        sounds.add(getNoise(position));

        return sounds;
    }

    public int getLetter(int position){
        try {
            return resources.obtainTypedArray(child ? R.array.sound_child_letter_id_array
                    : R.array.sound_letter_id_array).getResourceId(position % count, 0);
        } catch (ArrayIndexOutOfBoundsException e){
            return R.raw.silent;
        }
    }

    public int getNoise(int position){
        try {
            return resources.obtainTypedArray(child ? R.array.sound_child_noise_id_array
                    : R.array.sound_noise_id_array).getResourceId(position % count, 0);
        } catch (ArrayIndexOutOfBoundsException e){
            return R.raw.silent;
        }
    }

    public int getName(int position, int offset) {
        try {
            return resources.obtainTypedArray(resources.obtainTypedArray(child
                    ?R.array.sound_child_name_array:R.array.sound_name_array)
                    .getResourceId(position % count, R.array.sound_name_array_000))
                    .getResourceId(offset, R.raw.silent);
        } catch (ArrayIndexOutOfBoundsException e){
            return R.raw.silent;
        }

    }
    
}
