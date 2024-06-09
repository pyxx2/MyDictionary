package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable{

    Handler handler;
    public static int flag=0;//0为收藏模式，1为delete模式
    public static boolean dellike=false;//true为删除收藏，false无事发生
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Dictionary.class);
                startActivity(intent);
            }
        });

        handler = new Handler(Looper.myLooper()) {
            public void handleMessage(@NonNull Message msg) {
                Log.i(MotionEffect.TAG, "handleMessage:收到消息" + msg.what);
                if (msg.what == 6) {
                    String englishPhrase = msg.getData().getString("englishPhrase");
                    String chineseTranslation = msg.getData().getString("chineseTranslation");
                    // 更新UI组件
                    TextView textView = findViewById(R.id.sentence); // 英文短语的TextView
                    TextView textView1 = findViewById(R.id.sentence2); // 中文翻译的TextView
                    textView.setText(englishPhrase);
                    textView1.setText(chineseTranslation);
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

        int id = 0;
        try {
            Document doc = Jsoup.connect("https://www.klceducation.edu.my/zh-hans/news-feed/article/share/20-positive-english-sentences#:~:text=%E4%B8%BA%E4%BA%86%E8%AE%A9%E4%BD%A0%E6%B0%B8%E8%BF%9C%E9%83%BD%E5%85%83%E6%B0%94%E6%BB%A1%E6%BB%A1%E7%9A%84%EF%BC%8C%E5%B0%8F%E7%BC%96%E4%B8%BA%E4%BD%A0%E5%87%86%E5%A4%87%E4%BA%8620%E4%B8%AA%E6%AD%A3%E8%83%BD%E9%87%8F%E8%8B%B1%E8%AF%AD%E5%8F%A5%E5%AD%90%EF%BC%8C%E8%AE%A9%E4%BD%A0%E5%9C%A8%E9%81%87%E5%88%B0%E6%8C%AB%E6%8A%98%E6%97%B6%E8%83%BD%E5%8F%8A%E6%97%B6%E8%87%AA%E6%88%91%E9%BC%93%E5%8A%B1%E3%80%82%20Just%20pull%20yourself%20together%20%E6%89%93%E8%B5%B7%E7%82%B9%E7%B2%BE%E7%A5%9E%E6%9D%A5%20Every%20day,I%20am%20a%20happy-go-luck%20kind%20of%20guy%20%E6%88%91%E6%98%AF%E4%B8%80%E4%B8%AA%E4%B9%90%E5%A4%A9%E6%B4%BE").get();
            Elements listItems = doc.select("ol > li.MsoNormal"); // 选择包含列表项的元素
            Random random = new Random();
            int randomIndex = random.nextInt(listItems.size())%20; // 生成一个随机数作为索引
            Element listItem = listItems.get(randomIndex); // 获取随机选中的列表项
            String englishPhrase = listItem.textNodes().get(0).text().trim(); // 获取英文短语
            String chineseTranslation = listItem.select("span[lang=ZH-CN]").text(); // 获取中文翻译
            Log.i("PhraseExtraction", "English: " + englishPhrase + " => Chinese: " + chineseTranslation);
//            TextView textView=findViewById(R.id.sentence);
//            TextView textView1=findViewById(R.id.sentence2);
//            textView1.setText(chineseTranslation);
//            textView.setText(englishPhrase);
// 创建一个新的Message对象并设置what值
            Message msg = handler.obtainMessage(6);
// 将英文短语和中文翻译放入Bundle中
            Bundle bundle = new Bundle();
            bundle.putString("englishPhrase", englishPhrase);
            bundle.putString("chineseTranslation", chineseTranslation);
            msg.setData(bundle);
// 发送消息
            handler.sendMessage(msg);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.i(MotionEffect.TAG,"run:msg已发送");
    }
}