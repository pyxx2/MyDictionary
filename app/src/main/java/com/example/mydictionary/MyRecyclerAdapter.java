package com.example.mydictionary;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>  {

    private ItemData[] listdata;
    private MyDatabaseHelper mSQlite;
    public MyRecyclerAdapter(ItemData[] listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }
    private static final int REQUEST_CODE = 1;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemData itemData = listdata[position];
        holder.cw.setText(itemData.getChinese());
        holder.ew.setText(itemData.getEnglish());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实例化
                mSQlite=new MyDatabaseHelper(view.getContext());
                final String english=itemData.getChinese();
                final String chinese=itemData.getEnglish();
                Log.i(TAG, "onClick: "+chinese+"###"+english);
                if(MainActivity.flag==1 && MainActivity.dellike==false){//删除主页的单词
                    //删除数据库里面的数据
                    mSQlite.deleteWord(chinese, english);
                    holder.linearLayout.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    // 动画完成后移除条目
                                    removeItem(holder.getAdapterPosition());
                                    Toast.makeText(view.getContext(), "已删除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .start();
                }
                if (MainActivity.flag==0 && MainActivity.dellike==false) {//必须在主页的收藏模式添加单词
                    //添加单词至收藏库
                    if(!mSQlite.checkMyWord(english)){
                        Log.i(TAG, "onClick: "+english);
                        mSQlite.add2(chinese,english);
                        Toast.makeText(view.getContext(), "已成功收藏单词", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(view.getContext(), "请勿重复收藏", Toast.LENGTH_SHORT).show();
                    }

                }
                if(MainActivity.dellike==true){
                    //删除收藏数据库里面的数据
                    mSQlite.deleteMyLike(chinese, english);
                    holder.linearLayout.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    // 动画完成后移除条目
                                    removeItem(holder.getAdapterPosition());
                                    Toast.makeText(view.getContext(), "已删除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .start();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cw;
        public TextView ew;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cw = (TextView) itemView.findViewById(R.id.cw);
            this.ew = (TextView) itemView.findViewById(R.id.ew);
            this.linearLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
    // 更新数据
    public void updateData(ItemData[] newData) {
        this.listdata = newData;
        notifyDataSetChanged(); // 通知适配器数据集已更改
    }
    public void removeItem(int position) {
        // 删除数据
        ArrayList<ItemData> list = new ArrayList<>(Arrays.asList(listdata));
        list.remove(position);
        listdata = list.toArray(new ItemData[0]);
        // 通知适配器条目被删除
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listdata.length);
    }
}

