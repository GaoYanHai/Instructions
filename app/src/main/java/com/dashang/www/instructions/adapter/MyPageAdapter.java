package com.dashang.www.instructions.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyPageAdapter extends PagerAdapter {

    private ArrayList<String> mList;
    private final Context mContext;

    public MyPageAdapter(Context cx) {
        mContext = cx.getApplicationContext();
    }

    public void getData(ArrayList<String> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(position));
        imageView.setImageBitmap(bitmap);
        container.addView(imageView, 0);

        return imageView;

    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //移除页面
        container.removeView((View) object);
    }
}
