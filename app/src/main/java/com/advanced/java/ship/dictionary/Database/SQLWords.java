package com.advanced.java.ship.dictionary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.advanced.java.ship.dictionary.TranslatedWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 17.05.2016.
 */
public class SQLWords {
    private final SQLWordsHelper sqlWordsHelper;

    public SQLWords(Context context){
        sqlWordsHelper = new SQLWordsHelper(context, 2);
    }

    public boolean write(TranslatedWord translatedWord){
        String trWord = getStringTrWord(translatedWord.getTranslate(), SQLWordsHelper.COLUMN_TYPE_TRWORD);
        if(trWord != null) {
            SQLiteDatabase db = sqlWordsHelper.getWritableDatabase();

            if(checkValue(SQLWordsHelper.COLUMN_NAME_WORD, translatedWord.getWord())){
                return false;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLWordsHelper.COLUMN_NAME_ID, translatedWord.getId());
            contentValues.put(SQLWordsHelper.COLUMN_NAME_WORD, translatedWord.getWord());
            contentValues.put(SQLWordsHelper.COLUMN_NAME_TRWORD, trWord);
            contentValues.put(SQLWordsHelper.COLUMN_NAME_TSWORD, translatedWord.getTranscription());

            long rowId = db.insert(SQLWordsHelper.TABLE_NAME, null, contentValues);
            Log.i("SQLWords", "rowId = " + rowId);
            return true;
        } else
            return false;
    }

    private String getStringTrWord(String[] tr, String stLength){
        int length = Integer.valueOf(stLength.substring(stLength.indexOf("(") + 1, stLength.indexOf(")")));
        if(tr.length > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for(String buf : tr){
                if(stringBuilder.length() + buf.length() + 1 <= length)
                    stringBuilder.append(buf).append(",");
                else {
                    Log.w("SQLWords", "length of translated word more than " + length);
                    break;
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } else{
            Log.w("SQLWords", "getStringTrWord returned null");
            return null;
        }
    }

    public List<TranslatedWord> read(){
        List<TranslatedWord> list = new ArrayList<>();
        SQLiteDatabase db = sqlWordsHelper.getReadableDatabase();
        String[] projection = {
                SQLWordsHelper.COLUMN_NAME_ID,
                SQLWordsHelper.COLUMN_NAME_WORD,
                SQLWordsHelper.COLUMN_NAME_TRWORD,
                SQLWordsHelper.COLUMN_NAME_TSWORD
        };
        Cursor cursor = db.query(SQLWordsHelper.TABLE_NAME, projection, null, null, null, null, null);
        TranslatedWord buf;
        if(cursor.moveToFirst()) {
            do {
                buf = new TranslatedWord();
                buf.setId(cursor.getLong(cursor.getColumnIndexOrThrow(SQLWordsHelper.COLUMN_NAME_ID)));
                buf.setWord(cursor.getString(cursor.getColumnIndexOrThrow(SQLWordsHelper.COLUMN_NAME_WORD)));
                buf.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(SQLWordsHelper.COLUMN_NAME_TRWORD)));
                buf.setTranscription(cursor.getString(cursor.getColumnIndexOrThrow(SQLWordsHelper.COLUMN_NAME_TSWORD)));
                list.add(buf);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    private boolean checkValue(String columnName, String value){
        SQLiteDatabase db = sqlWordsHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLWordsHelper.TABLE_NAME + " where " + columnName + " = '" + value + "'", null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        Log.w("SQLWords", "this word is already exist");
        cursor.close();
        return true;
    }

    public void removeAll(){
        Log.i("Table", "remove");
        SQLiteDatabase db = sqlWordsHelper.getWritableDatabase();
        Log.i("Table", String.valueOf(db.delete(SQLWordsHelper.TABLE_NAME, null, null)));
    }
}
