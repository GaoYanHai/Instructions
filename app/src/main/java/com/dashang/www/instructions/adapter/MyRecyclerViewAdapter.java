package com.dashang.www.instructions.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashang.www.instructions.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> {

    private Context context;
    private ArrayList<String> list;
    //声明监听的接口变量
    private OnItemClickListener onItemClickListener;

    public MyRecyclerViewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    //创建viewholder的  引入item的布局
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catalog_item, null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        //取出文件的名字
        int star = list.get(i).lastIndexOf("/");
        int end = list.get(i).lastIndexOf(".");
        String pathname = list.get(i).substring(star + 1, end);
        myHolder.textView.setText(pathname);
        //设置item的点击事件 通过接口暴露给外界
        myHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    //接口
                    onItemClickListener.onclick(view, i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_catalog_item);
        }

    }

    /**
     * 按钮点击事件需要的方法
     */
    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface OnItemClickListener {
        void onclick(View view, int position);
    }


}








