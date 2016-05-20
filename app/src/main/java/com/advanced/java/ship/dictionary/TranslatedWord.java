package com.advanced.java.ship.dictionary;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 12.05.2016.
 */
public class TranslatedWord {
    private long id = 0;
    private String word;
    private String transcription;
    private String[] translate;

    public TranslatedWord(){}

    public TranslatedWord(JSONObject object) throws JSONException {
        List<String> listTranslate = new ArrayList<>();
        JSONArray jsonArray = object.getJSONArray("def");
        if(jsonArray.length() > 1)
            Log.w("jsonArray", "methods jsonArray.length returned more than 1 (" + jsonArray.length() + ")");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        this.word = jsonObject.getString("text");
        if(jsonObject.has("ts"))
            this.transcription = jsonObject.getString("ts");
        else
            this.transcription = "";
        JSONArray tr = jsonObject.getJSONArray("tr");
        for(int i = 0; i < tr.length(); i++){
            JSONObject jWord = tr.getJSONObject(i);
            listTranslate.add(jWord.getString("text"));
        }
        translate = listTranslate.toArray(new String[listTranslate.size()]);
    }

    public void setTranslate(String[] translate) {
        this.translate = translate;
    }

    public void setTranslate(String translate){
        this.translate = translate.split(",");
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getTranscription() {
        return transcription;
    }

    public String[] getTranslate() {
        return translate;
    }

    public int getLengthTranslate(){
        return translate.length;
    }

    @Override
    public String toString() {
        return "(Id = " + id + ", Word = " + word + ", translate = " + Arrays.toString(translate) + ")";

    }
}
