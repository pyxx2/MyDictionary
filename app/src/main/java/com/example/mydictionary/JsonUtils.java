package com.example.mydictionary;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<Word> loadWordsFromJSON(Context context) {
        List<Word> wordList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("C-E.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String chinese = jsonObject.getString("chinese");
                String english = jsonObject.getString("english");
                Word word = new Word(chinese, english);
                wordList.add(word);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return wordList;
    }
}
