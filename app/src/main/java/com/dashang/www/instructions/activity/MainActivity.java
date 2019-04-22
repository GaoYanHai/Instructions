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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        // ToastUtil.showShort(getApplicationContext(),"点击了中间位置！");
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


    public void showPopwindow(){
        View popView = View.inflate(this, R.layout.activity_popwindow, null);
        final PopupWindow popupWindow = new PopupWindow(popView,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);  //获得焦点
        popupWindow.setTouchable(true);  //可以触摸
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

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

                //Log.e(TAG, "onClick: 点击了下"+currentItem );
                mVp_main.setCurrentItem(currentItem-1);
               // Log.e(TAG, "onClick: 点击了下"+currentItem );
//                Log.e(TAG, "onClick: 点击了上" );
                //popupWindow.dismiss();
            }
        });
        popView.findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下一张

                int currentItem = mVp_main.getCurrentItem();
               // Log.e(TAG, "onClick: 点击了下"+currentItem );
                mVp_main.setCurrentItem(currentItem+1);
               // Log.e(TAG, "onClick: 点击了下"+currentItem );
                //popupWindow.dismiss();
            }
        });

        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }

        //显示popwindow
        popupWindow.showAtLocation(this.findViewById(R.id.v_pop), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        popView.startAnimation(animation);
    }

}
