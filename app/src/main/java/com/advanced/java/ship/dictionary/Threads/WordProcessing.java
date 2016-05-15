package com.advanced.java.ship.dictionary.Threads;

import android.os.AsyncTask;
import android.util.Log;

import com.advanced.java.ship.dictionary.GetTranslationOfWords;
import com.advanced.java.ship.dictionary.TranslatedWord;

import java.util.concurrent.ExecutionException;

/**
 * Created by Anton on 15.05.2016.
 */
public class WordProcessing implements Runnable {
    private String word;

    public WordProcessing(String word){
        this.word = word;
    }

    @Override
    public void run() {
        Log.i("Thread", "sleep");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("Thread", "wake up");
        GetTranslationOfWords task = new GetTranslationOfWords();
        Log.i("WordProcessing", "asyncTask is started");
        task.execute(word);
        TranslatedWord trWord = null;
        try {
            trWord = task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        assert trWord != null;
        Log.i("WordProcessing", "result = " + trWord.getTranscription());

    }

    public String getWord() {
        return word;
    }
}
