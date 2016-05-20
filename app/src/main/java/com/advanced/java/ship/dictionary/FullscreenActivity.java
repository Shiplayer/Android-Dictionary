package com.advanced.java.ship.dictionary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.advanced.java.ship.dictionary.Activity.AddNewWordActivity;
import com.advanced.java.ship.dictionary.Database.SQLWords;
import com.advanced.java.ship.dictionary.Threads.WordProcessing;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 15000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private static final int REQUEST_CODE_DIALOG_ACTIVITY = 1;
    //private String getWord = "";
    private String engWord;
    private final Handler mHideHandler = new Handler();
    private TextView dummy_content;
    private View mContentView;
    private SQLWords sqlWords;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    /*private final View.OnTouchListener mDelayHideTouchListener = (view, motionEvent) -> {
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS);
        }
        return false;
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        sqlWords = new SQLWords(this);
        List<TranslatedWord> list = sqlWords.read();

        for(TranslatedWord tr : list){
            Log.i("TranslatedWord", tr.toString());
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        dummy_content = (TextView) findViewById(R.id.dummy_content);
        /*GetTranslationOfWords test = new GetTranslationOfWords();
        test.execute("accept");
        String[] buf;
        try {
            buf = test.get();
            if(buf != null)
                Log.i("AsyncTask", buf[0]);
            else
                Log.e("AsyncTask", "return null");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/


        // Set up the user interaction to manually show or hide the system UI.
        assert mContentView != null;
        //mContentView.setOnClickListener(view -> toggle());

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        Button btn = (Button)findViewById(R.id.dummy_button);
//        AlertDialog dialog = buildDialog();
        assert btn != null;

        // why code is showing many dialogs?

        Button btn_new = (Button)findViewById(R.id.button);
        assert btn_new != null;
        btn_new.setOnClickListener((view) -> {
            Intent intent = new Intent(this, AddNewWordActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DIALOG_ACTIVITY);
/*            AddNewWordDialog dialogFragment = new AddNewWordDialog();
            dialogFragment.show(getSupportFragmentManager(), "test");*/
        });
    }

    private Runnable addingWord = () -> {
        String word = engWord;
        WordProcessing wp = new WordProcessing(word, this);
        Thread t = new Thread(wp);
        t.start();
        if(t.isAlive()){
            try {
                Log.i("Thread", "wait Thread");
                t.join();
                Log.i("Thread", "join in addingWord");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(t.isInterrupted()){
                //Toast.makeText(getApplicationContext(), "interrupted", Toast.LENGTH_LONG).show();
                Log.i("Thread_addingWord", "time is over");
                return;
            }
            Log.i("Thread_addingWord", "word = " + wp.getWord());
            sqlWords.write(wp.getTranslatedWord());
            //dummy_content.setText(wp.getTranslatedWord().getTranslate()[0]);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_DIALOG_ACTIVITY){
            if(resultCode == RESULT_OK){
                if(data.hasExtra("word"))
                    if(data.getStringExtra("word") != null) {
                        Log.i("word_from_activity", data.getStringExtra("word"));
                        engWord = data.getStringExtra("word");
                        new Thread(addingWord).start();
                    }
                    else
                        Log.i("word_from_activity", "extra with the name word is null ");
                else
                    Log.i("word_from_activity", "extra with the name word not fount");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        Log.i("app status", "hide");
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        Log.i("app status", "show");
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        mHideHandler.postDelayed(mHideRunnable, AUTO_HIDE_DELAY_MILLIS);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private boolean mDoublePressBack = false;

    @Override
    public void onBackPressed(){
        if (mDoublePressBack){
            super.onBackPressed();
        }
        toggle();
        mDoublePressBack = true;
        mHideHandler.postDelayed(() -> mDoublePressBack = false, 500);
    }

    /*private AlertDialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
        LayoutInflater inflater = FullscreenActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog, null);
        EditText editText = (EditText)v.findViewById(R.id.edit_word);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("key", String.valueOf(keyCode));
                return true;
            }
        });

        builder.setView(inflater.inflate(R.layout.dialog, null)).setPositiveButton(R.string.add, (dialog, which) -> {
            Log.i("dialog", "in Activity");
            Log.i("which", String.valueOf(which));
            getWord = editText.getText().toString();
            Log.i("word", "test");
            Log.i("editText.getText()", String.valueOf(editText.getText()));
        }).setNegativeButton(R.string.cancel, (dialog, which)-> {
            dialog.dismiss();
        });
        return builder.create();
    }*/

}
