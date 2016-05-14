package com.advanced.java.ship.dictionary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.advanced.java.ship.dictionary.Activity.AddNewWordActivity;

import java.util.concurrent.ExecutionException;

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
    private String getWord = "";
    private final Handler mHideHandler = new Handler();
    private View mContentView;
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
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
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
        btn.setOnClickListener((view) -> {
            Log.i("dialog", "show");
//            if(!dialog.isShowing())
//                dialog.show();
            mHideHandler.postDelayed(() -> Log.i("test word", "word = " + getWord), 5000);
            mHideHandler.removeCallbacks(mHideRunnable);
        });

        Button btn_new = (Button)findViewById(R.id.button);
        assert btn_new != null;
        btn_new.setOnClickListener((view) -> {
            Intent intent = new Intent(this, AddNewWordActivity.class);
            startActivity(intent);
/*            AddNewWordDialog dialogFragment = new AddNewWordDialog();
            dialogFragment.show(getSupportFragmentManager(), "test");*/
        });
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
