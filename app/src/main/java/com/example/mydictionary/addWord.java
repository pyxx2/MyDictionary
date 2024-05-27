package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addWord extends AppCompatActivity {

    private MyDatabaseHelper mSQlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Button button=findViewById(R.id.back);
        Button button2=findViewById(R.id.add2);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(addWord.this,Dictionary.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText chi=findViewById(R.id.chi);
                EditText eng=findViewById(R.id.eng);

                String chinese = chi.getText().toString().trim();
                String english = eng.getText().toString().trim();   //获取输入
                if(!TextUtils.isEmpty(chinese)&&!TextUtils.isEmpty(english)){
                        mSQlite.add(chinese,english);
                        Intent intent = new Intent();
                        Toast.makeText(addWord.this,"已成功添加",Toast.LENGTH_SHORT).show();
                        intent.setClass(addWord.this,Dictionary.class);
                        startActivity(intent);
                        finish();
                }else {Toast.makeText(addWord.this,"信息不完备，添加失败",Toast.LENGTH_SHORT).show();}
            }
        });
        mSQlite = new MyDatabaseHelper(addWord.this);
    }
}