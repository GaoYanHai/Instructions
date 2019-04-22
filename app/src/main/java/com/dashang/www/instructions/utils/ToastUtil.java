package com.dashang.www.instructions.utils;

import android.content.Context;
import android.widget.Toast;

//可能的问题 Toast没有重置
public class ToastUtil {
    private static boolean isShow = true; //开关
    private static Toast mToast = null;  //全局唯一的对象

    //取消显示
    public void cancelToast(){
        if (isShow&&mToast!=null){
            mToast.cancel();
        }
    }


    //短时间显示
    public static void showShort(Context context ,String msg){
        if (isShow){
            if (mToast==null){
                mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
            }else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }


    //长时间显示
    public static void showLong(Context context ,String msg){
        if (isShow){
            if (mToast==null){
                mToast = Toast.makeText(context,msg,Toast.LENGTH_LONG);
            }else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }

}
