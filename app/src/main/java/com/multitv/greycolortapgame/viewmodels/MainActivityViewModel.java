package com.multitv.greycolortapgame.viewmodels;

import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private static final int ONE_SECOND = 1000;
    private static final int TWO_SECOND = 1500;
    private MutableLiveData<Integer> scoreCounterData = new MutableLiveData<>();
    private MutableLiveData<Long> mElapsedTime = new MutableLiveData<>();
    private MutableLiveData<Boolean> displayAlert = new MutableLiveData<>();

    private long mInitialTime;
    private final Timer timer;
    private int counter = 0;
    private int oldCounter = 0;

    public MainActivityViewModel() {
        mInitialTime = SystemClock.elapsedRealtime();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final long newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000;
                mElapsedTime.postValue(newValue);
                if (counter != 0 && oldCounter == counter)
                    displayAlert.postValue(true);
                oldCounter = counter;
            }
        }, ONE_SECOND, TWO_SECOND);
    }

    public LiveData<Long> getElapsedTime() {
        return mElapsedTime;
    }

    public LiveData<Integer> getScoreCounterData() {
        return scoreCounterData;
    }

    public LiveData<Boolean> getDisplayAlert() {
        return displayAlert;
    }

    public void updateCounter(boolean init) {
        if (init)
            counter = -1;
        scoreCounterData.setValue(++counter);
    }

    public void displayAlert(boolean show) {
        displayAlert.setValue(show);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer.cancel();
    }

    private int random;

    public int generateRandomNumber() {
        int rndm = (int) (Math.random() * 4);
        if (random == rndm)
            return generateRandomNumber();
        else
            random = rndm;
        return rndm;
    }
}
