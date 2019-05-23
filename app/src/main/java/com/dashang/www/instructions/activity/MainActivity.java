package com.dashang.www.instructions.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dashang.www.instructions.R;
import com.dashang.www.instructions.adapter.MyPageAdapter;
import com.dashang.www.instructions.adapter.MyRecyclerViewAdapter;
import com.dashang.www.instructions.utils.SpUtils;
import com.dashang.www.instructions.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mVp_main;
    private int screenWidth;
    private int screenHeight;
    private static final String TAG = "MainActivity";
    private PopupWindow popupWindowBottom;
    private PopupWindow popupWindowTop;
    private ProgressBar mPb_progress;
    private TextView mTv_numberpb;
    private RecyclerView mRlv_recycler;
    private DrawerLayout mDl_layout;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private boolean isComplete;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                SpUtils.putBoolean(MainActivity.this,"isComplete",true);
                initView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉头 不能用
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        isComplete = SpUtils.getBoolean(this,"isComplete",false);
        try {
            String fileNames[] = this.getAssets().list("instruct");
            String path = "/sdcard/images/";
            File file=new File(path);
            File[] files=file.listFiles();

            if (isComplete==false||files.length!=fileNames.length){
                copyFilesFassets(this);
            }else {
                initView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

    }

    public void initView() {
        mDl_layout = findViewById(R.id.dl_layout);
        //关闭手势滑动  只能点击出现
        mDl_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //去掉阴影效果  效果设置为透明的
        mDl_layout.setScrimColor(Color.TRANSPARENT);
        mRlv_recycler = findViewById(R.id.rlv_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //设置纵向显示
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRlv_recycler.setLayoutManager(linearLayoutManager);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, getData());
        mRlv_recycler.setAdapter(myRecyclerViewAdapter);
        //添加分割线
        mRlv_recycler.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        mVp_main = findViewById(R.id.vp_main);
        MyPageAdapter myPageAdapter = new MyPageAdapter(this);
        myPageAdapter.getData(getData());
        mVp_main.setAdapter(myPageAdapter);
        //回调方法  从adapter中传递出被点击的item的值
        myRecyclerViewAdapter.SetOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onclick(View view, int position) {
                // ToastUtil.showShort(getApplicationContext(),"点击了"+position);
                mVp_main.setCurrentItem(position);
                mDl_layout.closeDrawer(Gravity.RIGHT);
            }
        });


        mVp_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float mMiddleX = screenWidth / 2;
                float mMiddleY = screenHeight / 2;
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                //点击屏幕中间弹出popupwindow
//                if (x > mMiddleX / 2 && x < (screenWidth - mMiddleX / 2) && y > mMiddleY / 2 && y < (screenHeight - mMiddleY / 2)) {
                if (x > mMiddleX / 2 && x < (screenWidth - mMiddleX / 2) ) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        showPopwindow();
                        showPopwindowTop();
                        mPb_progress.setProgress(mVp_main.getCurrentItem() + 1);
                        //设置进度数字
                        mTv_numberpb.setText(getResources().getString(R.string.di) + mPb_progress.getProgress() + " "+getResources().getString(R.string.page) + "/" + getResources().getString(R.string.sum)+" " + mPb_progress.getMax() +" "+getResources().getString(R.string.page) );
                        // ToastUtil.showShort(getApplicationContext(),"点击了中间位置！");
                    }
                } else if (x < mMiddleX / 2) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        int currentItem = mVp_main.getCurrentItem();
                        if (currentItem != 0) {
                            mVp_main.setCurrentItem(currentItem - 1);
                        }
                        //ToastUtil.showShort(getApplicationContext(),"点击了左位置！");
                    }

                } else if (x > (screenWidth - mMiddleX / 2)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        int currentItem = mVp_main.getCurrentItem();
                        mVp_main.setCurrentItem(currentItem + 1);

                        //ToastUtil.showShort(getApplicationContext(),"点击了右位置！");
                    }

                }
                return false;
            }
        });

    }


    public void showPopwindow() {
        View popView = View.inflate(this, R.layout.activity_popwindow, null);
        popupWindowBottom = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowBottom.setFocusable(true);  //获得焦点
        popupWindowBottom.setTouchable(true);  //可以触摸
        popupWindowBottom.setOutsideTouchable(true);

        popupWindowBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindowTop.dismiss();
            }
        });
        popupWindowBottom.setBackgroundDrawable(new BitmapDrawable());

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                1, Animation.RELATIVE_TO_PARENT, 0);
        //动画变化的速率  开始较慢  后来变快
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        popView.findViewById(R.id.tv_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //上一张
                int currentItem = mVp_main.getCurrentItem();
                mVp_main.setCurrentItem(currentItem - 1);
                mPb_progress.setProgress(mVp_main.getCurrentItem() + 1);
                mTv_numberpb.setText(getResources().getString(R.string.di) + mPb_progress.getProgress() + " "+getResources().getString(R.string.page) + "/" + getResources().getString(R.string.sum)+" " + mPb_progress.getMax() +" "+getResources().getString(R.string.page) );
//                mTv_numberpb.setText("第" + mPb_progress.getProgress() + "页" + "/" + "共" + mPb_progress.getMax() + "页");

            }
        });
        popView.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下一张
                int currentItem = mVp_main.getCurrentItem();

                mVp_main.setCurrentItem(currentItem + 1);
                mPb_progress.setProgress(mVp_main.getCurrentItem() + 1);
                mTv_numberpb.setText(getResources().getString(R.string.di) + mPb_progress.getProgress() + " "+getResources().getString(R.string.page) + "/" + getResources().getString(R.string.sum)+" " + mPb_progress.getMax() +" "+getResources().getString(R.string.page) );
//                mTv_numberpb.setText("第" + mPb_progress.getProgress() + "页" + "/" + "共" + mPb_progress.getMax() + "页");

            }
        });


        //设置进度条
        mPb_progress = popView.findViewById(R.id.pb_progress);
        mPb_progress.setMax(getData().size());
        //设置进度条上的数字进度
        mTv_numberpb = popView.findViewById(R.id.tv_numberpb);


        if (popupWindowBottom.isShowing()) {
            popupWindowBottom.dismiss();
        }

        //显示popwindow
        popupWindowBottom.showAtLocation(this.findViewById(R.id.v_pop), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popView.startAnimation(animation);
    }


    public void showPopwindowTop() {

        View popView = View.inflate(this, R.layout.activity_popwindowtop, null);
        popupWindowTop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowTop.setFocusable(false);  //获得焦点
        popupWindowTop.setTouchable(true);  //可以触摸
        popupWindowTop.setOutsideTouchable(false);

        popupWindowTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        popupWindowTop.setBackgroundDrawable(new BitmapDrawable());

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                -1, Animation.RELATIVE_TO_PARENT, 0);
        //动画变化的速率  开始较慢  后来变快
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);
        popView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回退按钮
                finish();
            }
        });

        popView.findViewById(R.id.tv_catalog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //目录按钮
                mDl_layout.openDrawer(Gravity.RIGHT);
                popupWindowTop.dismiss();
                popupWindowBottom.dismiss();
            }
        });

        if (popupWindowTop.isShowing()) {
            popupWindowTop.dismiss();
        }

        //显示popwindow
        popupWindowTop.showAtLocation(this.findViewById(R.id.v_pop), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        popView.startAnimation(animation);
    }

    //退出之前结束掉
    @Override
    protected void onDestroy() {
        super.onDestroy();
            popupWindowBottom.dismiss();
            popupWindowTop.dismiss();
    }


    //获取文件夹下的所有文件
    public ArrayList<String> getData() {
        String path = "/sdcard/images/";
        File file=new File(path);
        File[] files=file.listFiles();
        ArrayList<String> s = new ArrayList<>();
        if (!file.exists()){
            file.mkdirs();  // 文件不存在 创建文件
            Log.e("error","空目录");
            ToastUtil.showLong(this,"文件不存在或已被删除！");
            return null;
        }else {
            if (files.length>0){

                for(int i =0;i<files.length;i++){
                    if (checkIsImageFile(files[i].getAbsolutePath())){
                        s.add(files[i].getAbsolutePath());
                    }
                }
                return s;
            }

        }
        return null;
    }

    //复制到sd卡的目录中去
    public void copyFilesFassets(final Context context) {

        String path = "/sdcard/images/";
        File file=new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    String fileNames[] = context.getAssets().list("instruct");//获取assets目录下的所有文件及目录名
                    //如果是文件
                    for (String s:fileNames){
                        Log.e(TAG, "copyFilesFassets:   文件" +s);
                        is = context.getAssets().open("instruct/"+s);
                        fos = new FileOutputStream(new File("/sdcard/images/",s));
                        byte[] buffer = new byte[1024];
                        int byteCount=0;
                        while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                            fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                        }
                        Log.e(TAG, "copyFilesFassets:   读写完成" );

                    }

                    fos.flush();//刷新缓冲区
                    is.close();
                    fos.close();
                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();

    }



    //过滤文件夹
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名     tolowerCase 将字母转换成小写
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif") || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }




}
