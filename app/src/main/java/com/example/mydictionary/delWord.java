package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class delWord extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private MyDatabaseHelper databaseHelper;
    private ItemData[]  wordList;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_word);
        Button button=findViewById(R.id.back2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                MainActivity.flag=0;
                intent.setClass(delWord.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        //创建数据库
        databaseHelper=new MyDatabaseHelper(this);

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerview3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView=findViewById(R.id.searchView2);
        searchView.setIconifiedByDefault(false);

        // 设置SearchView的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 用户提交搜索
                Search(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // 用户输入时实时更新搜索结果
                if(TextUtils.isEmpty(newText)){
                    getAllWordsFromDatabase();
                }else{
                    Search(newText);
                }
                return true;
            }
        });
    }
    private void  getAllWordsFromDatabase(){
        ArrayList<Word>allWords=databaseHelper.getAllwords();
        for (int i = 0; i < allWords.size(); i++) {
            Word word = allWords.get(i);
            Log.i(TAG, "getAllWordsFromDatabase: "+word.getEnglish());
        }
        updateRecyclerView(allWords);
    }
    private void Search(String query){
        ArrayList<Word>searchResult=databaseHelper.searchWords(query);
        updateRecyclerView(searchResult);
    }
    private void updateRecyclerView(ArrayList<Word> words) {
        ItemData[] itemsArray = new ItemData[words.size()];
        adapter = new MyRecyclerAdapter(itemsArray);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            itemsArray[i] = new ItemData(word.getChinese(), word.getEnglish());
            Log.i(TAG, "updateRecyclerView: "+word.getEnglish());
        }
        adapter.updateData(itemsArray);
    }
}
