package com.advanced.java.ship.dictionary;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anton on 10.05.2016.
 */
public class GetTranslationOfWords extends AsyncTask<String, Void, TranslatedWord> {

    @Override
    protected TranslatedWord doInBackground(String... params) {
        Log.i("params length", String.valueOf(params.length));
        TranslatedWord word = null;
        try {
            URL url = new URL("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?" +
                    "key=" + new APIkeys().getApiDict() +
                    "&text=" + params[0] +
                    "&lang=en-ru");
            Log.i("ulr", url.toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            //HttpsURLConnection connection = (HttpsURLConnection) new URL("https://yandex.ru").openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "translate.yandex.net");
            connection.connect();

            Log.i("response_code", String.valueOf(connection.getResponseCode()));
            Log.i("wtf", "wtf");
            Log.i("response_message", connection.getResponseMessage());

            String json = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String buf;
            while((buf = bf.readLine()) != null){
                json += buf;
            }
            //JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            //jsonReader.beginObject();
            /*while(jsonReader.hasNext()) {
                if(Objects.equals(jsonReader.nextName(), "text")) {
                    jsonReader.beginArray();
                    while(jsonReader.hasNext())
                        Log.i("json object", String.valueOf(jsonReader.nextString()));
                    jsonReader.endArray();
                } else
                    jsonReader.skipValue();
            }*/
            //Log.i("test",jsonReader.toString());
            try {
                JSONObject object = new JSONObject(json);
                word = new TranslatedWord(object);
                Log.i("word", word.getWord());
                String[] tr = word.getTranslate();
                for(int i = 0; i < word.getLengthTranslate(); i++)
                    Log.i("translated word", tr[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return word;
    }
}
