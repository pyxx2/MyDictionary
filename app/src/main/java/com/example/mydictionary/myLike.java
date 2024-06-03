package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class myLike extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private ItemData[] wordList;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);
        //创建数据库
        databaseHelper=new MyDatabaseHelper(this);
        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerview4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 初始化适配器但不设置数据
        adapter = new MyRecyclerAdapter(new ItemData[0]);
        recyclerView.setAdapter(adapter);
        //获取数据
        wordList = getMyWords();
        // 创建适配器并设置给RecyclerView
        adapter = new MyRecyclerAdapter(wordList);
        recyclerView.setAdapter(adapter);

        Button button=findViewById(R.id.back3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(myLike.this,Dictionary.class);
                startActivity(intent);
            }
        });
    }
    private ItemData[] getMyWords(){
        ArrayList<Word> allWords=databaseHelper.getMyWords();
        for (int i = 0; i < allWords.size(); i++) {
            Word word = allWords.get(i);
            Log.i(TAG, "getAllWordsFromDatabase: "+word.getEnglish());
        }
        ArrayList<Word> wordList = allWords; // 您的ArrayList
        ItemData[] itemsArray = new ItemData[wordList.size()];

        for (int i = 0; i < allWords.size(); i++) {
            Word word = allWords.get(i);
            // 假设 ItemData 有一个接受 Word 对象的构造函数
            itemsArray[i] = new ItemData(word.getChinese(), word.getEnglish(), word.getTimes());
        }
        // 现在 itemsArray 包含了 ItemData 对象
        updateRecyclerView(allWords);
        return itemsArray;
    }
    private void updateRecyclerView(ArrayList<Word> words) {
        ItemData[] itemsArray = new ItemData[words.size()];
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            itemsArray[i] = new ItemData(word.getChinese(), word.getEnglish(), word.getTimes());
        }
        adapter.updateData(itemsArray);
    }
}