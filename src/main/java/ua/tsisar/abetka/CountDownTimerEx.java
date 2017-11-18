package ua.tsisar.abetka;

import android.os.CountDownTimer;

public abstract class CountDownTimerEx {
    private long millisInFuture;
    private long millisPassed;
    private long countDownInterval;
    private CountDownTimer countDownTimer = null;

    protected CountDownTimerEx(long millisInFuture, long countDownInterval) {
        super();
        this.millisInFuture = millisInFuture;
        this.millisPassed = 0;
        this.countDownInterval = countDownInterval;
    }

    private void createCountDownTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisInFuture - millisPassed, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisPassed = millisInFuture - millisUntilFinished;
                CountDownTimerEx.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                millisPassed = 0;
                CountDownTimerEx.this.onFinish();
            }
        };
    }

    public synchronized final void cancel(){
        this.millisPassed = 0;
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    public synchronized final void pause() {
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    public synchronized final void start() {
        createCountDownTimer();
        countDownTimer.start();
    }

    public void setMillis(long millis){
        cancel();
        millisPassed = millis;
        start();
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();
}