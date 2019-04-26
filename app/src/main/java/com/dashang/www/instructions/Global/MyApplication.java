package com.dashang.www.instructions.Global;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
* Create By gyh
* Time 2019/4/26
* 处理全局异常 有异常记录退出
* */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置默认未捕获异常处理程序
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                Log.e(TAG, "uncaughtException: "+throwable );

                String path = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "logcat.log";
                File file = new File(path);

                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    throwable.printStackTrace(printWriter);
                    printWriter.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                //如果有服务器 可以上传到服务器中

                //退出应用
                System.exit(0);

            }
        });

    }
}
