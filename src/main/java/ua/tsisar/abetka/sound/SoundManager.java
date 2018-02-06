package ua.tsisar.abetka.sound;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ua.tsisar.abetka.preference.LoadPreference;

import static android.content.Context.AUDIO_SERVICE;

public class SoundManager implements MediaPlayer.OnCompletionListener {

    private Activity activity;
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> sounds;
    private boolean soundOn;

    public SoundManager(Activity activity){
        this.activity = activity;
        mediaPlayer = getMediaPlayer(activity);
        mediaPlayer.setOnCompletionListener(this);
        sounds = new ArrayList<>();
        soundOn = new LoadPreference(activity).getSoundMode();
    }

    private static MediaPlayer getMediaPlayer(Context context){

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            @SuppressLint("PrivateApi") Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            @SuppressLint("PrivateApi") Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            @SuppressLint("PrivateApi") Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            @SuppressLint("PrivateApi") Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Class[] classes = new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener};
            Constructor constructor = cSubtitleController.getConstructor(classes);

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field field = cSubtitleController.getDeclaredField("mHandler");

            field.setAccessible(true);
            try {
                field.set(subtitleInstance, new Handler());
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                field.setAccessible(false);
            }

            Method setSubtitleAnchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setSubtitleAnchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaplayer;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(sounds.size() > 1) {
            sounds.remove(0);
            play(sounds.get(0));
        }
    }

    // програти звук з R.raw...
    public void play(int sound){
        if(soundOn && mediaPlayer != null) {
            try {
                AssetFileDescriptor assetFileDescriptor = activity.getResources().openRawResourceFd(sound);

                mediaPlayer.reset();
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                assetFileDescriptor.close();
                //mediaPlayer.prepare();
                //mediaPlayer.start();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // програти звук з R.raw...
    public void play(ArrayList<Integer> sounds){
        if(soundOn && sounds.size() != 0) {
            this.sounds = sounds;
            play(sounds.get(0));
        }
    }

    public void stop(){
        if(sounds != null)
            sounds.clear();
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    public void release() {
        if (mediaPlayer != null) {
//            stop();
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isMute(){
        return mediaPlayer == null || !soundOn || getVolume() == 0;
        //return false;
    }

    private int getVolume(){
        AudioManager audioManager = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
