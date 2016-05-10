package com.advanced.java.ship.dictionary;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anton on 10.05.2016.
 */
public class GetTranslationOfWords extends AsyncTask<String, Void, String[]> {

    @Override
    protected String[] doInBackground(String... params) {
        Log.i("params length", String.valueOf(params.length));
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://translate.yandex.net/api/v1.5/tr.json/translate ? \n" +
                    "key=trnsl.1.1.20140929T144049Z.28ba2db3ff3933ac.dec41e434ee63bce4feeaba22b4c0d0f40407988\n" +
                    " & text=hello\n" +
                    " & lang=ru").openConnection();
            /*
            Host: translate.yandex.net
Accept:
            Content-Length: 17
            Content-Type: application/x-www-form-urlencoded
             */
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "translate.yandex.net");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Length", "17");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();

            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while(bf.ready()){
                Log.i("text in AsyncTask", bf.readLine());
            }
            /*JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            jsonReader.beginObject();
            jsonReader.beginArray();
            Log.i("json object", String.valueOf(jsonReader.hasNext()));
            jsonReader.endArray();
            jsonReader.endObject();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] buf = new String[1];
        buf[0] = params[0] + " from AsyncTask";
        return buf;
    }
}
