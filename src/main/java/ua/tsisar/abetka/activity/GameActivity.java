package ua.tsisar.abetka.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ua.tsisar.abetka.AlphabetItem;
import ua.tsisar.abetka.db.DBHelper;
import ua.tsisar.abetka.game.Game;
import ua.tsisar.abetka.preference.LoadPreference;
import ua.tsisar.abetka.sound.SoundHelper;
import ua.tsisar.abetka.sound.SoundManager;
import ua.tsisar.abetka.adapter.GameAdapter;
import ua.tsisar.abetka.game.GameListener;
import ua.tsisar.abetka.content.CorrectAnswerContent;
import ua.tsisar.abetka.content.FinishContent;
import ua.tsisar.abetka.content.GameContent;
import ua.tsisar.abetka.content.GameContent.GameContentListener;
import ua.tsisar.abetka.content.IncorrectAnswerContent;
import ua.tsisar.abetka.content.PauseContent;
import ua.tsisar.abetka.R;
import ua.tsisar.abetka.content.TopBarContent;

public class GameActivity extends AppCompatActivity implements GameListener, GameContentListener{

    private  final static int FIRST_HINT = 1;
    private  final static int SECOND_HINT = 2;
    private  final static int THIRD_HINT = 3;

    private Game game;

    private ProgressBar progressBar;
    private AlphabetItem alphabetItem;

    private TopBarContent topBarContent;
    private GameContent gameContent;
    private PauseContent pauseContent;
    private CorrectAnswerContent correctAnswerContent;
    private IncorrectAnswerContent incorrectAnswerContent;
    private FinishContent finishContent;

    private SoundHelper soundHelper;
    private SoundManager soundManager;

    private int currentLetter;
    private int currentOffset;
    private int hintPosition;
    private boolean isShowHint;

    private int hintState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        LoadPreference loadPreference = new LoadPreference(this);
        isShowHint = loadPreference.isShowHint();

        game = new Game(this);
        topBarContent = new TopBarContent(this);
        gameContent = new GameContent(this);
        pauseContent = new PauseContent(this);
        correctAnswerContent = new CorrectAnswerContent(this);
        incorrectAnswerContent = new IncorrectAnswerContent(this);
        finishContent = new FinishContent(this);

        progressBar = findViewById(R.id.game_ProgressBar);
        progressBar.setMax(getResources().obtainTypedArray(R.array.letters_array).length());

        if(!isShowHint) {
            findViewById(R.id.game_hint_button).setVisibility(View.GONE);
        }
        soundHelper = new SoundHelper(this);
        soundManager = new SoundManager(this);

        if(savedInstanceState != null){
            game.restoreInstanceState(savedInstanceState);
        }else {
            game.newLetter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(game.getState() == Game.GAME_STATE_PLAY) {
            game.pause();
        }else if (game.getState() == Game.GAME_STATE_CUR_RES){
            game.newLetter();
            game.pause();
        }
        soundManager.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        soundManager.release();
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(game != null){
            game.saveInstanceState(outState);
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if(game != null){
//            game.restoreInstanceState(savedInstanceState);
//        }
    }

    @Override
    public void onBackPressed() {
        if(game.getState() == Game.GAME_STATE_PLAY){
            game.pause();
        }else if(game.getState() == Game.GAME_STATE_FINISH){
            game = new Game(this);
            game.newLetter();
        }else if(game.getState() == Game.GAME_STATE_CUR_RES){
            game.newLetter();
        }else{
            game.start();
        }
    }

    // Промальовуємо час гри
    @Override
    public void onTimeUpdate(int time) {
        topBarContent.timeUpdate(time);
    }

    // Промальовуємо поточну літеру
    @Override
    public void onDrawCurrentLetter(int letter, int offset, ArrayList<Integer> list) {
        alphabetItem = new AlphabetItem(getResources(), letter, offset);
        hintState = FIRST_HINT;
        currentLetter = letter;
        currentOffset = offset;
        hintPosition = getHintPosition(list);

        gameContent.drawCurrentLetter(alphabetItem,
                new GameAdapter(this, R.layout.item_grid_view_game, list));
    }

    // Прогрес гри
    @Override
    public void onProgressBarUpdate(int progress) {
        progressBar.setProgress(progress);
    }

    // Гру розпочато, робимо контент гри видимим
    @Override
    public void onGameStart() {
        pauseContent.hide();
        correctAnswerContent.hide();
        incorrectAnswerContent.hide();
        finishContent.hide();
        gameContent.show();
    }

    // Гру поставлено на паузу, робимо контент паузи видимим
    @Override
    public void onGamePause(int answer, int star) {
        gameContent.hide();
        correctAnswerContent.hide();
        incorrectAnswerContent.hide();
        finishContent.hide();
        pauseContent.show(answer, star);
    }

    // вибрано літеру, або закінчився час
    // промальовуємо поточний результат
    @Override
    public void onGameCurrentResult(int result, int star) {
        gameContent.hide();
        topBarContent.topBarStarsAnimation(star);

        switch (result){
            case GAME_RES_CORRECT:
                correctAnswerContent.show(star);
                soundManager.play(soundHelper.getCorrectResSound());
                break;
            case GAME_RES_INCORRECT:
                incorrectAnswerContent.show(alphabetItem);
                soundManager.play(soundHelper.getIncorrectResSound(currentLetter));
                break;
            case GAME_RES_TIME_OUT:
                incorrectAnswerContent.show(alphabetItem);
                soundManager.play(soundHelper.getTimeOutResSounds(currentLetter));
                break;
        }
    }

    // Гру закінчино промальовуємо та зберігаємо кінцевий результат
    @Override
    public void onGameFinish(int answer, int star) {
        gameContent.hide();
        pauseContent.hide();
        correctAnswerContent.hide();
        incorrectAnswerContent.hide();
        finishContent.show(answer, star);
    }

    @Override
    public void onGameSaveResult(int answer, int star) {
        new DBHelper(this).saveHeightResult(answer, star);
    }


    // Клік по ітему гряди, передаємо позицію до гри
    @Override
    public void onItemClick(int position) {
        game.gridItemClick(position);
    }

    // Клік "вихід", зупиняємо гру, прибиваємо актівіті
    public void onClickExit(View view){
        game.cancel();
        finish();
    }

    // Клік "продовжити"
    public void onClickReturn(View view){
        soundManager.stop();
        if(game.getState() == Game.GAME_STATE_FINISH){
            game = new Game(this);
            game.newLetter();
        }else if(game.getState() == Game.GAME_STATE_CUR_RES){
            game.newLetter();
        }else{
            game.start();
        }
    }

    public void onClickShowHint(View view){
        if(hintState == FIRST_HINT && soundManager.isMute()){
            hintState++;
        }

        if(isShowHint) {
            switch (hintState){
                case SECOND_HINT:
                    gameContent.secondHint(hintPosition);
                    break;
                case THIRD_HINT:
                    gameContent.thirdHint(hintPosition);
                    break;
            }

            if(hintState < THIRD_HINT) {
                hintState++;
            }
            soundManager.play(soundHelper.getName(currentLetter, currentOffset));
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_on_click));
        }
    }

    private int getHintPosition(ArrayList<Integer> arrayList){
        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i) == currentLetter)
                return i;
        }
        return 0;
    }
}
