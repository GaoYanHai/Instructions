package com.dashang.www.instructions.utils;

import android.content.Context;

public class SpUtils {
    private static android.content.SharedPreferences sp;

    //存储此节点的标识信息  写入sp中
    public static void putBoolean(Context context, String key, Boolean value) {
        if (sp == null) {
            //节点的文件名 读取的方式
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    //读取节点的默认值  读取sp的值
    public static boolean getBoolean(Context context, String key, Boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
}
