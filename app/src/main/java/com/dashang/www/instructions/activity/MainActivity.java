package com.dashang.www.instructions.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.dashang.www.instructions.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mVp_main;
    private int screenWidth;
    private int screenHeight;
    private String path = "/sdcard/images/";
    private static final String TAG = "MainActivity";
    private PopupWindow popupWindowBottom;
    private PopupWindow popupWindowTop;
    private ProgressBar mPb_progress;
    private TextView mTv_numberpb;
    private RecyclerView mRlv_recycler;
    private DrawerLayout mDl_layout;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉头 不能用
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
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
                if (x > mMiddleX / 2 && x < (screenWidth - mMiddleX / 2) && y > mMiddleY / 2 && y < (screenHeight - mMiddleY / 2)) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        showPopwindow();
                        showPopwindowTop();
                        mPb_progress.setProgress(mVp_main.getCurrentItem() + 1);
                        //设置进度数字
                        mTv_numberpb.setText("第" + mPb_progress.getProgress() + "页" + "/" + "共" + mPb_progress.getMax() + "页");
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
                mTv_numberpb.setText("第" + mPb_progress.getProgress() + "页" + "/" + "共" + mPb_progress.getMax() + "页");

            }
        });
        popView.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下一张
                int currentItem = mVp_main.getCurrentItem();

                mVp_main.setCurrentItem(currentItem + 1);
                mPb_progress.setProgress(mVp_main.getCurrentItem() + 1);
                mTv_numberpb.setText("第" + mPb_progress.getProgress() + "页" + "/" + "共" + mPb_progress.getMax() + "页");

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
    public  ArrayList<String> getData() {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            ToastUtil.showLong(this,"文件不存在或已被删除！");
            return null;
        }
        ArrayList<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }



}
