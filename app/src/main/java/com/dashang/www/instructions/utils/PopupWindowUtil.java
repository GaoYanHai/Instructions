package com.dashang.www.instructions.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;


public class PopupWindowUtil {
    private Context context;

    public void PopupWindowUtil(Context context){
        this.context = context;
    }

    public void showPopupWindow(int layout){

        View popView = View.inflate(context, layout, null);
        PopupWindow popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //窗口消失监听

            }
        });

        //从底部向上的动画
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                1, Animation.RELATIVE_TO_PARENT, 0);
        //插值器  怎样变化  先快后慢
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }

        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,0);
        popView.startAnimation(animation);  //开始动画


    }

}
