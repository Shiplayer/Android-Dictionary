package com.advanced.java.ship.dictionary.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.advanced.java.ship.dictionary.R;
import com.advanced.java.ship.dictionary.TranslatedWord;

import java.util.List;

public class WordListActivity extends AppCompatActivity {
    private ListView list;
    private List<TranslatedWord> list_words;
    private ArrayWordAdapter adapter;
    private String logTag = "WordListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        list = (ListView)findViewById(R.id.listView);
        Intent intent = getIntent();
        if(intent.hasExtra("list_words")) {
            list_words = intent.getParcelableArrayListExtra("list_words");
            for (TranslatedWord tr : list_words) {
                Log.i(logTag, tr.toString());
            }
        } else {
            Log.w(logTag, "list_words not found");
        }
        adapter = new ArrayWordAdapter(this, list_words);
        list.setAdapter(adapter);
        Button button = (Button)findViewById(R.id.btn_remove_words);
        assert button != null;
        button.setOnClickListener((v) -> {
            if(list_words.size() > 0) {
                adapter.remove(list_words.get(0));
            }
        });

    }

    class ArrayWordAdapter extends ArrayAdapter<TranslatedWord> {
        private Context context;
        private List<TranslatedWord> list;

        public ArrayWordAdapter(Context context, List<TranslatedWord> list){
            super(context, R.layout.list_item_view, list);
            this.context = context;
            this.list = list;
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(logTag + "Adapter", list.get(position).toString());
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, null);
            }
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            checkBox.setText(list.get(position).getWord());
            //checkBox.setChecked(false);
            /*convertView.setOnClickListener(v -> {
                if(!checkBox.isChecked())
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);
            });*/

            ((TextView)convertView.findViewById(R.id.tsView)).setText(list.get(position).getTranscription());

            return convertView;
        }

    }
}
