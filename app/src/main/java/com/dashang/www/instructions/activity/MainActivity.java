package com.dashang.www.instructions.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import com.dashang.www.instructions.R;
import com.dashang.www.instructions.adapter.MyPageAdapter;
import com.dashang.www.instructions.utils.ToastUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mVp_main;
    private int screenWidth;
    private int screenHeight;
    private static final String TAG = "MainActivity";
    private PopupWindow popupWindowBottom;
    private PopupWindow popupWindowTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉头 不能用
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        WindowManager windowManager = this.getWindowManager();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

    }

    public void initView() {
        mVp_main = findViewById(R.id.vp_main);
        MyPageAdapter myPageAdapter = new MyPageAdapter(this);
        myPageAdapter.getData(getData());
        mVp_main.setAdapter(myPageAdapter);

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
                        // ToastUtil.showShort(getApplicationContext(),"点击了中间位置！");
                    }
                }else if (x<mMiddleX/2){
                    if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                        int currentItem = mVp_main.getCurrentItem();
                        if (currentItem!=0){
                            mVp_main.setCurrentItem(currentItem-1);
                        }

                        //ToastUtil.showShort(getApplicationContext(),"点击了左位置！");
                    }

                }else if (x>(screenWidth-mMiddleX/2)){
                    if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                        int currentItem = mVp_main.getCurrentItem();
                        mVp_main.setCurrentItem(currentItem+1);
                        //ToastUtil.showShort(getApplicationContext(),"点击了右位置！");
                    }

                }
                return false;
            }
        });

    }

    public ArrayList<String> getData() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("/sdcard/images/1.jpg");
        strings.add("/sdcard/images/2.jpg");
        strings.add("/sdcard/images/3.jpg");
        return strings;
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

            }
        });
        popView.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下一张
                int currentItem = mVp_main.getCurrentItem();
                mVp_main.setCurrentItem(currentItem + 1);

            }
        });

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
}
