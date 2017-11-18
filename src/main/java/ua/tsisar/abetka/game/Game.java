package ua.tsisar.abetka.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;

import ua.tsisar.abetka.CountDownTimerEx;
import ua.tsisar.abetka.R;

public class Game{
    private static final String STATE = "state";
//    private static final String SIZE = "size";
    private static final String CURRENT_LETTER = "currentLetter";
    private static final String OFFSET = "offset";
    private static final String ANSWER = "answer";
    private static final String STARS = "stars";
    private static final String CURRENT_STARS = "currentStars";
    private static final String CURRENT_MILLIS = "currentMillis";
    private static final String DRAWN_LIST = "drawnList";
    private static final String GRID_LIST = "gridList";

    @IntDef({GAME_STATE_PLAY, GAME_STATE_PAUSE, GAME_STATE_CUR_RES, GAME_STATE_FINISH})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {}

    public static final int GAME_STATE_PLAY = 0;
    public static final int GAME_STATE_PAUSE = 1;
    public static final int GAME_STATE_CUR_RES = 3;
    public static final int GAME_STATE_FINISH = 4;

    private int state;

    private static final int GRID_SIZE = 4;

    private Context context;
    private GameListener listener;
    private int size;
    private CountDownTimerEx countDownTimer;
    private static final long millisInFuture = 10999;
    private static final long countDownInterval = 1000;

    private int currentLetter;
    private int offset;

    private ArrayList<Integer> drawnList = new ArrayList<>();
    private ArrayList<Integer> gridList = new ArrayList<>();

    private int answer;
    private int stars;
    private int currentStars;

    private long currentMillis;
    private Handler handler;

    private final String TAG = "myLogs";

    private int timeToStars(long time) {
        return (int) (time/1000+1)/3;
    }

    public Game(Context context){
        this.context = context;
        if (context instanceof GameListener) {
            this.listener = (GameListener) context;
        }
        // Розмір масиву
        this.size = context.getResources().obtainTypedArray(R.array.letters_array).length();
        // Таймер 10с
        this.countDownTimer = newTimer();
        this.handler = new Handler();
        // Кількість правильних відповідей
        this.answer = 0;
        // Кількість зірочок
        this.stars = 0;
    }

    private CountDownTimerEx newTimer(){
        return new CountDownTimerEx(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long time) {
                currentMillis = time;
                // Кожну секунду промальовуємо таймер і зірочки
                currentStars = timeToStars(time);
                listener.onTimeUpdate((int)time/1000);
            }

            @Override
            public void onFinish() {
                // Час вийшов
                // Промальовуємо результат
                listener.onTimeUpdate(0);
                drawCurrentResult(GameListener.GAME_RES_TIME_OUT);
            }
        };
    }

    public void newLetter(){
        // Старт гри
        // генеруємо наступну букву
        // якщо згенеровано
        // генеруємо масив з 4х букв для варіантів
        // промальовуємо це все
        // запускаємо таймер
        // якщо буква не генерується
        // кінець гри, промальовуємо фінальний результат
        if(genLetter()){
            genGridList();
            offset = genOffset(currentLetter);
            listener.onDrawCurrentLetter(currentLetter, offset, gridList);
            start();
            //countDownTimer.start();
        }else if(state != GAME_STATE_FINISH){
            Log.d(TAG, "onGameFinish");
            setState(GAME_STATE_FINISH);
            listener.onGameFinish(answer, stars);
            listener.onGameSaveResult(answer, stars);
        }
    }

    public void start(){
        handler.removeCallbacks(updateTime);
        setState(GAME_STATE_PLAY);
        countDownTimer.start();
        listener.onGameStart();
    }

    public void pause(){
        handler.removeCallbacks(updateTime);
        setState(GAME_STATE_PAUSE);
        countDownTimer.pause();
        listener.onGamePause(answer, stars);
    }

    public void cancel(){
        handler.removeCallbacks(updateTime);
        setState(GAME_STATE_FINISH);
        countDownTimer.cancel();
    }

    private void drawCurrentResult(int result){
        handler.postDelayed(updateTime, 5000);
        setState(GAME_STATE_CUR_RES);
        listener.onGameCurrentResult(result, currentStars);
    }

    public void gridItemClick(int position){
        // Клік по варіанту відповіді
        // превіряємо його та промальовуємо результат
        // якщо менше секунди - час вийшов
        if(currentMillis > 1000) {
            countDownTimer.cancel();
            drawCurrentResult(currentResult(position));
        }
    }

    private boolean genLetter(){
        if(drawnList.size() < size) {
            Random random = new Random();

            // генеруємо довільну букву (індекс)
            currentLetter = random.nextInt(size);
            // перевіряємо чи її нема в списку, якщо є генеруємо нову
            while (drawnList.contains(currentLetter)) {
                currentLetter = random.nextInt(size);
            }
            // додаємо в масив промальованих
            drawnList.add(currentLetter);
            listener.onProgressBarUpdate(drawnList.size());
//            Log.d(TAG, "letter: " + drawnList);
//            Log.d(TAG, "size: " + drawnList.size() + " of " + size);
            return true;
        }
        return false;
    }

    private void genGridList(){
        Random random = new Random();

        // очищуємо список варіантів
        gridList.clear();
        // генеруємо довільну букву (індекс)
        int letter = random.nextInt(size);

        for(int i = 0; i < 4; i++){
            // поки букву занаходимо в списку генеруємо нову
            while (gridList.contains(letter)){
                letter = random.nextInt(size);
            }
            // додаємо букву в список
            gridList.add(letter);
        }

        // якщо поточнаої букви немає в варінтах, закидуємо її на довільну позицію
        if(!gridList.contains(currentLetter)){
            gridList.set(random.nextInt(GRID_SIZE), currentLetter);
        }

//        Log.d(TAG, "grid list: " + gridList);
    }

    private int currentResult(int position){
        currentMillis = millisInFuture;
        // перевіряємо чи поточна буква співпадає з варіантом на позиції
        if(gridList.get(position).equals(currentLetter)){
            answer++;
            stars+=currentStars;
            return GameListener.GAME_RES_CORRECT;
        }
        return GameListener.GAME_RES_INCORRECT;
    }

//    public int getAnswer(){
//        return answer;
//    }
//
//    public int getStars(){
//        return stars;
//    }

    private void setState(@State int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }

    private final Runnable updateTime = new Runnable() {
        public void run() {
            newLetter();
        }
    };

    // Рандомом генеруємо відступ для поточної літери
    private int genOffset(int position){
        return new Random().nextInt(context.getResources().getIntArray(
                context.getResources().obtainTypedArray(R.array.illustration_array)
                        .getResourceId(position, R.array.illustration_array_000)).length);
    }

    public void saveInstanceState(Bundle outState) {
        outState.putInt(STATE, state);
//        outState.putInt(SIZE, size);
        outState.putInt(CURRENT_LETTER, currentLetter);
        outState.putInt(OFFSET, offset);
        outState.putInt(ANSWER, answer);
        outState.putInt(STARS, stars);
        outState.putInt(CURRENT_STARS, currentStars);
        outState.putLong(CURRENT_MILLIS, currentMillis);
        outState.putIntegerArrayList(DRAWN_LIST, drawnList);
        outState.putIntegerArrayList(GRID_LIST, gridList);
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        state = savedInstanceState.getInt(STATE);
//        size = savedInstanceState.getInt(SIZE);
        currentLetter = savedInstanceState.getInt(CURRENT_LETTER);
        offset = savedInstanceState.getInt(OFFSET);
        answer = savedInstanceState.getInt(ANSWER);
        stars = savedInstanceState.getInt(STARS);
        currentStars = savedInstanceState.getInt(CURRENT_STARS);
        currentMillis = savedInstanceState.getLong(CURRENT_MILLIS);
        drawnList = savedInstanceState.getIntegerArrayList(DRAWN_LIST);
        gridList = savedInstanceState.getIntegerArrayList(GRID_LIST);

        Log.d(TAG, "onRestoreState: " + state);

        countDownTimer.setMillis(millisInFuture - currentMillis);
        listener.onDrawCurrentLetter(currentLetter, offset, gridList);
        switch (state){
            case GAME_STATE_PLAY:
            case GAME_STATE_PAUSE:
            case GAME_STATE_CUR_RES:
                start();
                break;
            case GAME_STATE_FINISH:
                cancel();
                listener.onGameFinish(answer, stars);
                break;
        }

    }

}
