package ua.tsisar.abetka.game;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public interface GameListener {
    @IntDef({GAME_RES_TIME_OUT, GAME_RES_INCORRECT, GAME_RES_CORRECT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Result {}

    int GAME_RES_TIME_OUT = -1;
    int GAME_RES_INCORRECT = 0;
    int GAME_RES_CORRECT = 1;

    void onTimeUpdate(int time);
    void onDrawCurrentLetter(int letter, int offset, ArrayList<Integer> list);
    void onProgressBarUpdate(int progress);
    void onGameStart();
    void onGamePause(int answer, int star);
    void onGameCurrentResult(@Result int result, int star);
    void onGameFinish(int answer, int star);
    void onGameSaveResult(int answer, int star);
}
