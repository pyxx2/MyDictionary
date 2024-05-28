package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Handler;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Dictionary extends AppCompatActivity{
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private ItemData[] wordList;
    private MyDatabaseHelper databaseHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        //创建数据库
        databaseHelper=new MyDatabaseHelper(this);

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 从JSON文件加载数据
        wordList = loadWordsFromJSON(this);
        // 创建适配器并设置给RecyclerView
        adapter = new MyRecyclerAdapter(wordList);
        recyclerView.setAdapter(adapter);
        Button button=findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {//，不要注册，返回主页面
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Dictionary.this,addWord.class);
                startActivity(intent);
            }
        });
        searchView=findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);

        // 设置SearchView的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 用户提交搜索时调用
                Log.i(TAG, "onQueryTextSubmit: 开始搜索" + query);
                ArrayList<Word> searchResult = databaseHelper.searchWords(query);
                Log.i(TAG, "onQueryTextSubmit: " + searchResult);
                // 将搜索结果转换为 ItemData 数组
                ItemData[] itemsArray = new ItemData[searchResult.size()];
                for (int i = 0; i < searchResult.size(); i++) {
                    Word word = searchResult.get(i);
                    itemsArray[i] = new ItemData(word.getChinese(), word.getEnglish(), word.getTimes());
                }
                // 更新 RecyclerView 的数据
                adapter.updateData(itemsArray);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果您希望在用户输入时实时更新搜索结果，也可以在这里调用搜索方法
                return true;
            }
        });
    }
    public ItemData[] loadWordsFromJSON(Context context) {
        List<ItemData> itemList = new ArrayList<>();
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
                int times = jsonObject.optInt("times", 0); // times，默认为0
                // 检查单词是否已存在于数据库中
                if (!databaseHelper.checkWordExists(english)) {
                    databaseHelper.add(chinese, english, times);
                }
                ItemData itemData = new ItemData(english, chinese, times);
                itemList.add(itemData);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        ItemData[] itemsArray = new ItemData[itemList.size()];
        itemsArray = itemList.toArray(itemsArray);
        return itemsArray;
    }
}