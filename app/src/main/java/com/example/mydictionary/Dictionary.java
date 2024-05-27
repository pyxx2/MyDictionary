package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Dictionary extends AppCompatActivity implements Runnable{
    Handler handler;
    MyRecyclerAdapter myRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        //添加单词
        Button button=findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Dictionary.this,addWord.class);
                startActivity(intent);
            }
        });

        handler=new Handler(Looper.myLooper()){
            public void handleMessage(@NonNull Message msg){
                Log.i(MotionEffect.TAG,"handleMessage:收到消息"+msg.what);
                if(msg.what==6){
                    Toast.makeText(Dictionary.this,"数据已更新",Toast.LENGTH_SHORT).show();

//                    listItems=(ArrayList<HashMap<String, String>>) msg.obj;
//                    //准备数据,不需要准备页面布局
//
//                    myAdapter=new MyAdapter(Dictionary.this,R.layout.activity_rate_list3,listItems);
//                    setListAdapter(myAdapter);
                    Toast.makeText(Dictionary.this,"insert over",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

        //开启子线程
        Thread t = new Thread(this);
        t.start();

    }


    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }
        int id=0;
        try {
            Document doc = Jsoup.connect("https://www.englishspeak.com/zh-cn/english-words").get();
            Element table = doc.select("table.table.table-striped").first();
            Elements trs = table.select("tbody > tr");
            for (Element tr : trs) {
                Element td = tr.child(0); // 获取每个<tr>的第一个<td>
                String englishWord = td.select("a").text(); // 获取<a>标签的文本
                String chineseTranslation = td.ownText(); // 获取<td>中除了<a>标签以外的文本
                Log.i("WordsExtraction", "English: " + englishWord + " => Chinese: " + chineseTranslation);
            }

                //创建数据，加载到数据库中
//                RateManager rateManager=new RateManager(RateListActivity3.this);
//                RateItem item=new RateItem();
//
//                for(int i=0;i+7<ratemsg.length;i+=3){
//                    String name=ratemsg[i+1];
//                    String rate=ratemsg[i+2];
//                    Log.i(MotionEffect.TAG, "run: "+name+"==>"+rate);
//                    HashMap<String,String>map=new HashMap<>();
//                    map.put("ItemTitle",name);
//                    map.put("ItemDetail",rate);
//                    //放入数据库中
//                    item.setCname(name);
//                    item.setCval(rate);
//                    rateManager.add(item);
//                    Log.i(MotionEffect.TAG, "run: "+map);
//                    listItems.add(map);
//                }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        //发送数据回主线程
        Message msg=handler.obtainMessage(6);
        //msg.obj=listItems;
        handler.sendMessage(msg);
        Log.i(MotionEffect.TAG,"run:msg已发送");
    }
}