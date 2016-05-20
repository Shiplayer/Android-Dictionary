package com.advanced.java.ship.dictionary.Threads;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.advanced.java.ship.dictionary.GetTranslationOfWords;
import com.advanced.java.ship.dictionary.TranslatedWord;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Anton on 15.05.2016.
 */
public class WordProcessing implements Runnable {
    private String word;
    private TranslatedWord translatedWord;
    private Context mContext;

    public WordProcessing(String word, Context mContext){
        this.word = word;
        this.mContext = mContext;
        translatedWord = null;
    }

    @Override
    public void run() {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            //Toast.makeText(getApplicationContext(), "NetworkInfo is null", Toast.LENGTH_LONG).show();
            Log.i("NetworkInfo", "Network is null");
            return;
        }
        if(!networkInfo.isAvailable()){
            //Toast.makeText(getApplicationContext(), "Network is not available", Toast.LENGTH_LONG).show();
            Log.i("Network", "Network is not Available");
            return;
        }
        GetTranslationOfWords task = new GetTranslationOfWords();
        Log.i("WordProcessing", "asyncTask is started");
        task.execute(word);
        try {
            Log.i("Thread", "wait Thread");
            translatedWord = task.get(5000, TimeUnit.MILLISECONDS);
            Log.i("Thread", "join");

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            task.cancel(true);
            e.printStackTrace();
            return;
        }
        assert translatedWord != null;
        Log.i("WordProcessing", "result = " + translatedWord.getTranscription());

    }

    public String getWord() {
        return word;
    }

    public TranslatedWord getTranslatedWord() {
        return translatedWord;
    }
}
