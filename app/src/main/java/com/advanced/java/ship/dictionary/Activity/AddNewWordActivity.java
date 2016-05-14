package com.advanced.java.ship.dictionary.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.advanced.java.ship.dictionary.R;

public class AddNewWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
        Button btnAdd = (Button)findViewById(R.id.btn_add);
        assert btnAdd != null;
        EditText editText = (EditText)findViewById(R.id.edit_word);
        assert editText != null;
        btnAdd.setOnClickListener((view) ->{
            Log.i("activity", "editText = " + editText.getText());
            finish();
        });
        Button btnCancel = (Button)findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener((view) ->{
            Log.i("activity", "exit");
            finish();
        });
        //getActionBar().hide();
    }
}
