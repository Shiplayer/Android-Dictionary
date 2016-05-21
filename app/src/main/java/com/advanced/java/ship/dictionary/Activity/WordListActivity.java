package com.advanced.java.ship.dictionary.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.advanced.java.ship.dictionary.R;
import com.advanced.java.ship.dictionary.TranslatedWord;

import java.util.List;

public class WordListActivity extends AppCompatActivity {
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        list = (ListView)findViewById(R.id.listView);


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
            return null;
        }
    }
}
